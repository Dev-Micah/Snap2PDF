package com.micahnyabuto.snap2pdf.core.data.repository

import com.micahnyabuto.snap2pdf.core.data.local.Document
import com.micahnyabuto.snap2pdf.core.data.local.DocumentDao
import kotlinx.coroutines.flow.Flow

interface DocumentRepository {
    suspend fun addDocument(document: Document)
    suspend fun deleteDocument(document: Document)

    fun allDocuments(): Flow<List<Document>>
}

class DocumentRepositoryImpl(private val dao: DocumentDao): DocumentRepository{

    override suspend fun addDocument(document: Document) {
        dao.insert(document)
    }

    override suspend fun deleteDocument(document: Document) {
        dao.delete(document)
    }

    override fun allDocuments(): Flow<List<Document>> = dao.getAllDocuments()
}