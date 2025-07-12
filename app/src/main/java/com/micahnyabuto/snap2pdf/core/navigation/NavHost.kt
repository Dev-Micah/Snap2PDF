package com.micahnyabuto.snap2pdf.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.micahnyabuto.snap2pdf.features.history.HistoryScreen
import com.micahnyabuto.snap2pdf.features.home.HomeScreen
import com.micahnyabuto.snap2pdf.features.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController
){
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = Destinations.Home.route
    ){
        composable(Destinations.Home.route){
            HomeScreen()
        }
        composable(Destinations.History.route){
            HistoryScreen()
        }
        composable(Destinations.Settings.route){
            SettingsScreen()
        }
    }
}