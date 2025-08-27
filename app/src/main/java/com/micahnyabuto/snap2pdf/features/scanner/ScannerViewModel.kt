package com.micahnyabuto.snap2pdf.features.scanner

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ScannerViewModel : ViewModel() {
    private val _pdfUri = MutableStateFlow<Uri?>(null)
    val pdfUri: StateFlow<Uri?> = _pdfUri

    private val _imageUris = MutableStateFlow<List<Uri>>(emptyList())
    val imageUris: StateFlow<List<Uri>> = _imageUris

    fun setScanResults(pdf: Uri?, images: List<Uri>) {
        _pdfUri.value = pdf
        _imageUris.value = images
    }
}
