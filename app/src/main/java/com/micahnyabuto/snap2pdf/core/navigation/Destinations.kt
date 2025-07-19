package com.micahnyabuto.snap2pdf.core.navigation

sealed class Destinations(val route: String){
    object Home: Destinations("home")
    object History: Destinations("history")

    object Settings: Destinations("settings")

    object Files: Destinations("files")

    object Snap: Destinations("snap")
    object Splash: Destinations("splash")

    object Search: Destinations("search")

    object Main: Destinations("main")

    object Profile: Destinations("profile")

    object Preview: Destinations("preview/{uri}")

}