package com.micahnyabuto.snap2pdf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.micahnyabuto.snap2pdf.core.navigation.AppNavHost
import com.micahnyabuto.snap2pdf.ui.theme.Snap2PDFTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Snap2PDFTheme {
                AppNavHost()
                }
            }
        }
}

