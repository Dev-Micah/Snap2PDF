package com.micahnyabuto.snap2pdf

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import com.micahnyabuto.snap2pdf.core.navigation.AppNavHost
import com.micahnyabuto.snap2pdf.features.settings.SettingsViewModel
import com.micahnyabuto.snap2pdf.ui.theme.Snap2PDFTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: SettingsViewModel = koinViewModel()
            val isDarkMode = viewModel.isDarkMode.collectAsState()
            Snap2PDFTheme(darkTheme = isDarkMode.value) {
                AppNavHost(
                    activity = this,
                )
            }

        }

    }

}