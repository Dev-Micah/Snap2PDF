package com.micahnyabuto.snap2pdf.core.navigation

import android.media.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavigation (
    val label: String,
    val selectedIcon:ImageVector,
    val unselectedIcon: ImageVector,
    val route : String
){
    Home(
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Filled.Home,
        route = ""
    ),
    History(
        label = "Home",
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Filled.History,
        route = ""
    ),
    Settings(
        label = "Home",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Filled.Settings,
        route = ""
    ),


}