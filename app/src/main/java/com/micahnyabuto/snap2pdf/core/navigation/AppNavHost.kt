package com.micahnyabuto.snap2pdf.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.micahnyabuto.snap2pdf.features.splash.SplashScreen

@Composable
fun AppNavHost(){
    val navController = rememberNavController()
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = Destinations.Splash.route
    ){

        composable(Destinations.Splash.route){
            SplashScreen(navController=navController)
        }
        composable(Destinations.Main.route){
            MainNavGraph()
        }

    }
}