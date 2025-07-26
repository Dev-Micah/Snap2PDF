package com.micahnyabuto.snap2pdf.di


import androidx.room.Room
import com.micahnyabuto.snap2pdf.core.data.local.DocumentDatabase
import com.micahnyabuto.snap2pdf.core.data.repository.DocumentRepository
import com.micahnyabuto.snap2pdf.core.data.repository.DocumentRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            DocumentDatabase::class.java,
            "documents_db"
        ).build()
    }

    single { get<DocumentDatabase>().documentDao() }

    single { DocumentRepositoryImpl(get()) }


}