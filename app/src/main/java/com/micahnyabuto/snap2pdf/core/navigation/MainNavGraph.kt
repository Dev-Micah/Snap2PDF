package com.micahnyabuto.snap2pdf.core.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.micahnyabuto.snap2pdf.features.camera.CameraPreviewScreen
import com.micahnyabuto.snap2pdf.features.camera.PreviewCapturedImageScreen
import com.micahnyabuto.snap2pdf.features.files.FilesScreen
import com.micahnyabuto.snap2pdf.features.history.HistoryScreen
import com.micahnyabuto.snap2pdf.features.home.HomeScreen
import com.micahnyabuto.snap2pdf.features.search.SearchScreen
import com.micahnyabuto.snap2pdf.features.settings.SettingsScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavGraph(){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route.orEmpty()
    var imageUri by remember { mutableStateOf<Uri?>(null) }


    val showBottomNavigation = currentRoute !in listOf(
        Destinations.Splash.route,
        Destinations.Search.route,
        Destinations.Snap.route,
        Destinations.Preview.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal),
        bottomBar = {
            if (showBottomNavigation) {
                Box {
                    // Bottom Bar with two sides
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        // Left side items
                        BottomNavigation.entries.take(2).forEach { navigationItem ->
                            NavigationBarItem(
                                selected = currentRoute == navigationItem.route,
                                onClick = { navController.navigate(navigationItem.route) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                    elevation = 0.dp
                                ),
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface
                            ),
                                icon = {
                                    Icon(
                                        imageVector = navigationItem.unselectedIcon,
                                        contentDescription = navigationItem.label,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                label = { Text(navigationItem.label, fontSize = 10.sp) }
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        // Right side items
                        BottomNavigation.entries.takeLast(2).forEach { navigationItem ->
                            NavigationBarItem(
                                selected = currentRoute == navigationItem.route,
                                onClick = { navController.navigate(navigationItem.route) },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                        elevation = 0.dp
                                    ),
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurface
                                ),
                                icon = {
                                    Icon(
                                        imageVector = navigationItem.unselectedIcon,
                                        contentDescription = navigationItem.label,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                label = { Text(navigationItem.label, fontSize = 10.sp) }
                            )
                        }
                    }

                    FloatingActionButton(
                        onClick = {
                            navController.navigate(Destinations.Snap.route) },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-2).dp),

                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Snap")
                    }
                }

            }
        }

    ){innerpadding->
        NavHost(
            navController =navController,
            startDestination = Destinations.Home.route,
            modifier = Modifier.padding(innerpadding)
        ){
            composable(Destinations.Home.route){
                HomeScreen(navController=navController)
            }
            composable(Destinations.History.route){
                HistoryScreen()
            }
            composable(Destinations.Settings.route){
                SettingsScreen()
            }
            composable(Destinations.Files.route){
                FilesScreen()
            }
            composable(Destinations.Search.route){
                SearchScreen(
                    navController=navController
                )
            }
            composable(Destinations.Snap.route){
                CameraPreviewScreen(
                    navController = navController,
                    onImageCaptured = {uri ->
                        navController.navigate("preview/${Uri.encode(uri.toString())}")

                    }
                )
            }
            composable(
                route = Destinations.Preview.route,
                arguments = listOf(navArgument("uri") { type = NavType.StringType })
            ) { backStackEntry ->
                val uri = backStackEntry.arguments?.getString("uri")?.let { Uri.parse(it) }
                if (uri != null) {
                    PreviewCapturedImageScreen(uri = uri, navController = navController)
                }
            }


        }
    }
}