package com.micahnyabuto.snap2pdf

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.micahnyabuto.snap2pdf.core.navigation.AppNavHost
import com.micahnyabuto.snap2pdf.ui.theme.Snap2PDFTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Snap2PDFTheme {
                AppNavHost(
                    activity = this,
                )
            }

        }

    }

}