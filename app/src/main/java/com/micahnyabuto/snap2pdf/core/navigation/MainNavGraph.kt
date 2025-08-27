package com.micahnyabuto.snap2pdf.core.navigation

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.micahnyabuto.snap2pdf.features.files.FilesScreen
import com.micahnyabuto.snap2pdf.features.home.HomeScreen
import com.micahnyabuto.snap2pdf.features.scanner.SavePDFScreen
import com.micahnyabuto.snap2pdf.features.scanner.ScannerViewModel
import com.micahnyabuto.snap2pdf.features.search.SearchScreen
import com.micahnyabuto.snap2pdf.features.settings.SettingsScreen
import org.koin.androidx.compose.koinViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavGraph(
    scannerLauncher: ActivityResultLauncher<IntentSenderRequest>,
    pdfUri: Uri?,
    imageUris: List<Uri>,
    activity: Activity,
    scannerViewModel: ScannerViewModel = koinViewModel()
){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route.orEmpty()



    val showBottomNavigation = currentRoute !in listOf(
        Destinations.Splash.route,
        Destinations.Search.route,
        Destinations.Snap.route,
        Destinations.Preview.route,
        Destinations.Save.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal),
        bottomBar = {
            if (showBottomNavigation) {
                Column {
                    HorizontalDivider(thickness = 0.5.dp)
                    NavigationBar(
                        tonalElevation = 0.dp,
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        BottomNavigation.entries.forEach { navigationItem ->

                            val isSelected = currentRoute == navigationItem.route

                            NavigationBarItem(
                                selected = isSelected,
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) navigationItem.selectedIcon else navigationItem.unselectedIcon,
                                        contentDescription = navigationItem.label
                                    )
                                },
                                label = {
                                    Text(
                                        text = navigationItem.label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.sp,
                                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                        )
                                    )
                                },
                                onClick = {
                                    if (currentRoute != navigationItem.route) {
                                        navController.navigate(navigationItem.route)
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                        elevation = 0.dp
                                    ),
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
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
                HomeScreen(
                    navController=navController,
                    scannerLauncher = scannerLauncher,
                    activity = activity,
                    scannerViewModel = scannerViewModel,
                )
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
            composable(Destinations.Save.route){
                SavePDFScreen(
                    pdfUri = scannerViewModel.pdfUri.value,
                    imageUris = scannerViewModel.imageUris.value,
                    onSaveComplete = {
                        navController.popBackStack(Destinations.Home.route, inclusive = false)
                    }
                )
            }

        }
    }
}