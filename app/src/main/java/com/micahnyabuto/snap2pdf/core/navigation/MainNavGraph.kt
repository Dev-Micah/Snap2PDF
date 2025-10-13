package com.micahnyabuto.snap2pdf.core.navigation

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.micahnyabuto.snap2pdf.features.files.FilesScreen
import com.micahnyabuto.snap2pdf.features.home.HomeScreen
import com.micahnyabuto.snap2pdf.features.scanner.SavePDFScreen
import com.micahnyabuto.snap2pdf.features.scanner.ScannerViewModel
import com.micahnyabuto.snap2pdf.features.search.SearchScreen
import com.micahnyabuto.snap2pdf.features.settings.SettingsScreen
import kotlinx.coroutines.delay
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

    val context = LocalContext.current

    //scannerLauncher
    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val scanResult = GmsDocumentScanningResult.fromActivityResultIntent(result.data)
            val imageUris = scanResult?.pages?.mapNotNull { it.imageUri } ?: emptyList()
            val pdfUri = scanResult?.pdf?.uri

            scannerViewModel.setScanResults(pdfUri, imageUris)
            navController.navigate(Destinations.Save.route)
        }
    }




    val showBottomNavigation = currentRoute !in listOf(
        Destinations.Splash.route,
        Destinations.Search.route,
        Destinations.Snap.route,
        Destinations.Save.route,
        Destinations.View.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal),
        bottomBar = {
            if (showBottomNavigation) {
                Column {
                    HorizontalDivider(thickness = 0.5.dp)
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        // Left side items (first two)
                        BottomNavigation.entries.take(1).forEach { navigationItem ->
                            NavigationBarItem(
                                selected = currentRoute == navigationItem.route,
                                onClick = { navController.navigate(navigationItem.route) },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(0.dp),
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

                        // Spacer in the middle for FAB
                        FloatingActionButton(
                            onClick = {
                                // Launch the scanner
                                val options = GmsDocumentScannerOptions.Builder()
                                    .setGalleryImportAllowed(true)
                                    .setPageLimit(5)
                                    .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_JPEG, GmsDocumentScannerOptions.RESULT_FORMAT_PDF)
                                    .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
                                    .build()

                                val scanner = GmsDocumentScanning.getClient(options)
                                scanner.getStartScanIntent(activity)
                                    .addOnSuccessListener { intentSender ->
                                        val request = IntentSenderRequest.Builder(intentSender).build()
                                        scannerLauncher.launch(request)
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Scanner failed to launch", Toast.LENGTH_SHORT).show()
                                    }
                            },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier.padding(10.dp)
                                .offset(0.dp, (-11).dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Add",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )

                        }

                        // Right side items (last two)
                        BottomNavigation.entries.takeLast(1).forEach { navigationItem ->
                            NavigationBarItem(
                                selected = currentRoute == navigationItem.route,
                                onClick = { navController.navigate(navigationItem.route) },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(0.dp),
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
                    activity = activity,
                    scannerViewModel = scannerViewModel,
                )
            }
            composable(Destinations.Settings.route){
                SettingsScreen(
                    viewModel = koinViewModel()
                )
            }
//            composable(Destinations.Files.route){
//                FilesScreen()
//            }
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
//            composable(
//                Destinations.View.route + "view?uri={uri}",
//                arguments = listOf(navArgument("uri") { type = NavType.StringType })
//            ) { backStackEntry ->
//                val uriString = backStackEntry.arguments?.getString("uri")
//                val uri = uriString?.let { Uri.parse(it) }
//                if (uri != null) {
//                    PdfViewerScreen(documentUri = uri)
//                }
//            }



        }
    }
}