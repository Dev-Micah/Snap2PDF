package com.micahnyabuto.snap2pdf.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DocumentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(document: Document)

    @Delete
    suspend fun delete(document: Document)

    @Query("SELECT * FROM documents ORDER BY CreatedAt DESC")
    suspend fun getAllDocuments(): List<Document>
}