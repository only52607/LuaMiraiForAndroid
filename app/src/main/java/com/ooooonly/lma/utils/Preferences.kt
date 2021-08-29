package com.ooooonly.lma.utils

import android.content.SharedPreferences

fun SharedPreferences.getIntSafe(key: String, defValue: Int):Int {
    return try {
        return getInt(key, defValue)
    } catch (e: Exception) {
        try {
            getString(key, defValue.toString())?.toInt() ?: defValue
        } catch (e:Exception) {
            return defValue
        }
    }
}

fun SharedPreferences.getLongSafe(key: String, defValue: Long):Long {
    return try {
        return getLong(key, defValue)
    } catch (e: Exception) {
        try {
            getString(key, defValue.toString())?.toLong() ?: defValue
        } catch (e:Exception) {
            return defValue
        }
    }
}

fun SharedPreferences.getBooleanSafe(key: String, defValue: Boolean):Boolean {
    return try {
        return getBoolean(key, defValue)
    } catch (e: Exception) {
        try {
            getString(key, defValue.toString())?.toBoolean() ?: defValue
        } catch (e:Exception) {
            return defValue
        }
    }
}