package com.ooooonly.lma.ui.navigation

sealed class Screen(val route: String) {
    object Bot : Screen("bot")
    object Script : Screen("script")
    object Log : Screen("log")
    object About : Screen("about")
    object Extension : Screen("extension")
}

sealed class LeafScreen(
    private val route: String
) {
    fun createRoute(root: Screen) = "${root.route}/$route"

    object Bot : LeafScreen("bot")

    object Script : LeafScreen("script")

    object Log : LeafScreen("log")

    object EditBot: LeafScreen("editBot?botId={botId}") {
        fun createRoute(root: Screen, botId: Long?): String {
            return "${root.route}/editBot".let {
                if (botId != null) "$it?botId=$botId" else it
            }
        }
    }

    object ScriptStore : LeafScreen("scriptStore")
}