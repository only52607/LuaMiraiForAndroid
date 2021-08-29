package com.ooooonly.lma.utils

import android.content.Context
import com.github.javiersantos.appupdater.AppUpdaterUtils
import com.github.javiersantos.appupdater.enums.UpdateFrom

fun buildAppUpdaterUtils(context: Context) {
    AppUpdaterUtils(context)
        .setGitHubUserAndRepo("only52607", "LuaMiraiForAndroid")
        .setUpdateFrom(UpdateFrom.GITHUB)
}