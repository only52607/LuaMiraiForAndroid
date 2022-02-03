package com.ooooonly.lma.utils

import net.mamoe.mirai.Bot

val Bot.nickOrNull: String?
    get() = try {
        bot.nick
    } catch (e: Exception) {
        null
    }

val Bot.avatarUrlOrNull: String?
    get() = try {
        bot.avatarUrl
    } catch (e: Exception) {
        null
    }

fun getAvatarUrlById(id: Long): String {
    return "https://q.qlogo.cn/headimg_dl?bs=qq&dst_uin=$id&src_uin=www.jlwz.cn&fid=blog&spec=100"
}

val Bot.avatarUrlStatic: String
    get() = getAvatarUrlById(id)