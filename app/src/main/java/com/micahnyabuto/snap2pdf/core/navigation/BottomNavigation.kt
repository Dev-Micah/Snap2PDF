package com.micahnyabuto.snap2pdf.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavigation (
    val label: String,
    val selectedIcon:ImageVector,
    val unselectedIcon: ImageVector,
    val route : String
){
    Home(
        label = "Files",
        selectedIcon = Icons.Filled.PictureAsPdf,
        unselectedIcon = Icons.Filled.PictureAsPdf,
        route = Destinations.Home.route
    ),
//    Files(
//        label = "Files",
//        selectedIcon = Icons.Default.Article,
//        unselectedIcon= Icons.Default.Article,
//        route= Destinations.Files.route
//    ),
    Settings(
        label = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Filled.Settings,
        route = Destinations.Settings.route
    )

}