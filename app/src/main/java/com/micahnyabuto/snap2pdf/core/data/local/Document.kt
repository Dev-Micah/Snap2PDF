package com.micahnyabuto.snap2pdf.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documents")
data class Document(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val uri: String,
    val createdAt: Long = System.currentTimeMillis()
)
