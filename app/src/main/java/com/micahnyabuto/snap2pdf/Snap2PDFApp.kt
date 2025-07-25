package com.micahnyabuto.snap2pdf

import android.app.Application
import com.micahnyabuto.snap2pdf.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class Snap2PDFApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@Snap2PDFApp)
            modules(listOf(databaseModule))
        }
    }
}