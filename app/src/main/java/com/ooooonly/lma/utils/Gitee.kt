package com.ooooonly.lma.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.gildor.coroutines.okhttp.await
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import kotlin.io.path.Path

@OptIn(ExperimentalStdlibApi::class)
class GiteeFile(
    val owner: String,
    val repository: String,
    val path: String,
    val branch: String = "master",
    private val fileAssert: Boolean? = null,
    private val showParent: Boolean = false,
    private val rootLevel: Int = 0,
    var displayFileName: String? = null
) {
    companion object {
        private const val PROTOCOL = "https"
        private const val GITEE_HOST = "gitee.com"
        private val extraHeaders = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36"
        )
        private val okhttpClient = OkHttpClient.Builder().followRedirects(false).build()
        private val fileItemRegex =
            Regex("""data-path='(.*?)'\s*data-type='(.*?)'""")
    }

    private val String.trimSlash get() = replace(Regex("^/"), "").replace(Regex("/$"), "")

    private fun String.getFileName() = split("/").last()

    val pageUrl by lazy {
        URL("$PROTOCOL://$GITEE_HOST/$owner/$repository/tree/$branch/${path.trimSlash}")
    }

    val rawUrl by lazy {
        URL("$PROTOCOL://$GITEE_HOST/$owner/$repository/raw/$branch/${path.trimSlash}")
    }

    private lateinit var pageData: String

    private suspend fun preparePageData(useCache: Boolean = true) {
        if (this::pageData.isInitialized && useCache) return
        val response = okhttpClient.newCall(buildRequest {
            url(pageUrl)
            get()
            extraHeaders.forEach { (key, value) -> header(key, value) }
        }).await()
        pageData = withContext(Dispatchers.Default) {
            response.body?.string() ?: ""
        }
    }

    suspend fun listFiles(useCache: Boolean = true): List<GiteeFile> {
        preparePageData(useCache)
        if (isFile) throw IllegalAccessError("$pageUrl is not a directory")
        return buildList {
            if (showParent && rootLevel <= level) {
                this@GiteeFile.parent?.let {
                    add(it.apply {
                        displayFileName = ".."
                    })
                }
            }
            fileItemRegex.findAll(pageData).all {
                add(
                    GiteeFile(
                        owner,
                        repository,
                        it.groupValues[1],
                        branch,
                        fileAssert = it.groupValues[2] == "file",
                        showParent = showParent,
                        rootLevel = rootLevel
                    )
                )
            }
        }
    }

    val fileName: String get() = displayFileName ?: path.getFileName()

    val isFile: Boolean by lazy {
        fileAssert ?: pageData.contains("redirected")
    }

    val isDirectory: Boolean get() = !isFile

    val isRoot: Boolean get() = path.trimSlash.isBlank()

    val level: Int by lazy {
        if (isRoot) return@lazy 0
        path.split("/").size
    }

    val parent: GiteeFile? by lazy {
        if (isRoot) return@lazy null
        GiteeFile(
            owner,
            repository,
            path.trimSlash.replace(Regex("/.*?$"), ""),
            branch,
            fileAssert = false,
            showParent = showParent,
            rootLevel = rootLevel
        )
    }

    suspend fun getInputStream(): InputStream? {
        return okhttpClient.newCall(buildRequest {
            url(rawUrl)
            get()
            extraHeaders.forEach { (key, value) -> header(key, value) }
        }).await().body?.byteStream()
    }
}