package com.micahnyabuto.snap2pdf.features.home

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.mlkit.vision.documentscanner.*
import com.micahnyabuto.snap2pdf.core.data.local.Document
import com.micahnyabuto.snap2pdf.core.navigation.Destinations
import com.micahnyabuto.snap2pdf.features.scanner.ScannerViewModel
import com.micahnyabuto.snap2pdf.utils.FileUtils
import com.micahnyabuto.snap2pdf.utils.Greeting
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    activity: Activity,
    scannerLauncher: ActivityResultLauncher<IntentSenderRequest>,
    scannerViewModel: ScannerViewModel = koinViewModel(),
    documentViewModel: DocumentViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val documentList by documentViewModel.documents.collectAsState()

    val uiState by documentViewModel.uiState.collectAsState()

    var expandedDocId by remember { mutableStateOf<String?>(null) }


    //scannerLauncher
    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val scanResult = GmsDocumentScanningResult.fromActivityResultIntent(result.data)
            val imageUris = scanResult?.pages?.mapNotNull { it.imageUri } ?: emptyList()
            val pdfUri = scanResult?.pdf?.uri

            scannerViewModel.setScanResults(pdfUri, imageUris)
            navController.navigate(Destinations.Save.route)
        }
    }

    LaunchedEffect(Unit) {
        documentViewModel.loadDocuments()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Greeting() },
                actions = {
                    IconButton(onClick = { navController.navigate(Destinations.Search.route) }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", modifier = Modifier.size(28.dp))
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(35.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val options = GmsDocumentScannerOptions.Builder()
                        .setGalleryImportAllowed(true)
                        .setPageLimit(5)
                        .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_JPEG, GmsDocumentScannerOptions.RESULT_FORMAT_PDF)
                        .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
                        .build()

                    val scanner = GmsDocumentScanning.getClient(options)
                    scanner.getStartScanIntent(activity)
                        .addOnSuccessListener { intentSender ->
                            val request = IntentSenderRequest.Builder(intentSender).build()
                            scannerLauncher.launch(request)
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Scanner failed to launch", Toast.LENGTH_SHORT).show()
                        }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.padding(10.dp)
            ) {
                Icon(Icons.Default.DocumentScanner, contentDescription = "Snap")
            }
        }
    ) { innerPadding ->
        when{
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }
            documentList.isEmpty() -> {
                EmptyScreen()
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = "Error: ${uiState.error}")
                }
            }else -> {
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(documentList) { doc ->
                    Card(
                        onClick = {
                            val uri = Uri.parse(doc.uri)
                            FileUtils.openPdf(context, uri)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            AsyncImage(
                                model = doc.uri,
                                contentDescription = "Document thumbnail",
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.LightGray)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = doc.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = FileUtils.formatTimestamp(doc.createdAt),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }


                            }
                        }
                        }
                    }
                }
            }
        }


    }
}

@Composable
fun EmptyScreen(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "You have no documents")
    }
}

@Composable
fun DocumentRow(
    doc: Document,
    isExpanded: Boolean,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(doc.name)

        Box {
            IconButton(onClick = onExpand) {
                Icon(Icons.Default.MoreVert, contentDescription = "Share and delete")
            }

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = onDismiss
            ) {
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        onDismiss()
                        onDelete()
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Share") },
                    onClick = {
                        onDismiss()
                        onShare()
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                )
            }
        }
    }
}

