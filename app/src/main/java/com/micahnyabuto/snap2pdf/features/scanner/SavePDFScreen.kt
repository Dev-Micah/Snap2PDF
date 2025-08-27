package com.micahnyabuto.snap2pdf.features.scanner

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.micahnyabuto.snap2pdf.core.data.local.Document
import com.micahnyabuto.snap2pdf.features.home.DocumentViewModel
import com.micahnyabuto.snap2pdf.utils.FileUtils
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavePDFScreen(
    pdfUri: Uri?,
    imageUris: List<Uri>,
    documentViewModel: DocumentViewModel = koinViewModel(),
    onSaveComplete: () -> Unit = {}
) {
    val context = LocalContext.current
    var fileName by remember { mutableStateOf("Snap2PDF_${System.currentTimeMillis()}") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Save PDF") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                pdfUri?.let { uri ->
                    val savedUri = FileUtils.savePdfToDownloads(context, uri)
                    val document = Document(
                        name = fileName,
                        uri = savedUri.toString(),
                        id = 0,
                        createdAt = System.currentTimeMillis(),
                    )
                    documentViewModel.addDocument(document)
                    onSaveComplete()
                }
            },
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(100.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                Icon(Icons.Default.Check, contentDescription = "Save")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            OutlinedTextField(
                value = fileName,
                onValueChange = { fileName = it },
                label = { Text("Document Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

//            pdfUri?.let {
//                OutlinedButton(onClick = { FileUtils.openPdf(context, it) }) {
//                    Text("Preview PDF")
//                }
//            }

            if (imageUris.isNotEmpty()) {
                Text("Scanned Images:", style = MaterialTheme.typography.titleMedium)
                LazyColumn {
                    items(imageUris) { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
