@file:Suppress("unused")

package com.ooooonly.lma.mcl.mclinternal

import android.os.Build
import androidx.annotation.RequiresApi
import java.lang.reflect.Modifier
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import java.lang.reflect.Member as JReflectionMember

internal class ServiceList<T>(
    internal val classLoader: ClassLoader,
    internal val delegate: List<String>
)

internal object PluginServiceHelper {
    inline fun <reified T : Any> ClassLoader.findServices(): ServiceList<T> = findServices(T::class)

    fun <T : Any> ClassLoader.findServices(vararg serviceTypes: KClass<out T>): ServiceList<T> =
        serviceTypes.flatMap { serviceType ->
            getResourceAsStream("META-INF/services/" + serviceType.qualifiedName!!)?.let { stream ->
                stream.bufferedReader().useLines { lines ->
                    lines.filter(String::isNotBlank)
                        .filter { it[0] != '#' }
                        .toList()
                }
            }.orEmpty()
        }.let { ServiceList(this, it) }

    @RequiresApi(Build.VERSION_CODES.O)
    fun <T : Any> ServiceList<T>.loadAllServices(): List<T> {
        return delegate.mapNotNull { classLoader.loadService<T>(it) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun <T : Any> ClassLoader.loadService(
        classname: String,
    ): T? {
        @Suppress("UNCHECKED_CAST")
        return kotlin.runCatching {
            val clazz =
                Class.forName(classname, true, this).cast<Class<out T>>()
            clazz.kotlin.objectInstance
                ?: kotlin.runCatching {
                    clazz.declaredFields.firstOrNull { it.isStatic() && it.name == "INSTANCE" }?.get(null)
                }.getOrNull()
                ?: clazz.kotlin.createInstanceOrNull()
                ?: clazz.constructors.firstOrNull { it.parameterCount == 0 }?.newInstance()
                ?: error("Cannot find a no-arg constructor")
        }.getOrElse {
            throw ServiceLoadException("Could not load service ${classname}.", it)

            /*
            logger.error(
                { "Could not load PluginLoader ${pluginQualifiedName}." },
                PluginLoadException("Could not load PluginLoader ${pluginQualifiedName}.", it)
            )*/
        } as T?
    }
}

internal fun JReflectionMember.isStatic(): Boolean = this.modifiers.and(Modifier.STATIC) != 0

@Suppress("unused", "RedundantVisibilityModifier")
internal open class ServiceLoadException : RuntimeException {
    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

@OptIn(ExperimentalContracts::class)
@Suppress("UNCHECKED_CAST")
internal inline fun <reified R> Any.cast(): R {
    contract {
        returns() implies (this@cast is R)
    }
    return this as R
}

@JvmSynthetic
internal fun <T : Any> KClass<T>.createInstanceOrNull(): T? {
    val noArgsConstructor = constructors.singleOrNull { it.parameters.all(KParameter::isOptional) }
        ?: return null

    return noArgsConstructor.callBy(emptyMap())
}