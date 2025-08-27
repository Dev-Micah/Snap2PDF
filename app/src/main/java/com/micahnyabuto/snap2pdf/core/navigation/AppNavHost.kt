package com.micahnyabuto.snap2pdf.core.navigation

import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.micahnyabuto.snap2pdf.MainActivity
import com.micahnyabuto.snap2pdf.features.scanner.ScannerViewModel
import com.micahnyabuto.snap2pdf.features.splash.SplashScreen
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    activity: MainActivity,
){
    val scannerViewModel: ScannerViewModel = koinViewModel()
    val navController = rememberNavController()
    val pdfUri by scannerViewModel.pdfUri.collectAsState()
    val imageUris by scannerViewModel.imageUris.collectAsState()

    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val scanResult = GmsDocumentScanningResult.fromActivityResultIntent(result.data)
            val pdf = scanResult?.pdf?.uri
            val images = scanResult?.pages?.mapNotNull { it.imageUri } ?: emptyList()
            scannerViewModel.setScanResults(pdf, images)
        }
    }

    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = Destinations.Splash.route
    ){

        composable(Destinations.Splash.route){
            SplashScreen(navController=navController)
        }
        composable(Destinations.Main.route){
            MainNavGraph(
                scannerLauncher = scannerLauncher,
                activity = activity,
                scannerViewModel = scannerViewModel,
                pdfUri = pdfUri,
                imageUris = imageUris
            )
        }

    }
}