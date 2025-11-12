package com.micahnyabuto.snap2pdf.features.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.micahnyabuto.snap2pdf.core.navigation.Destinations
import com.micahnyabuto.snap2pdf.features.scanner.ScannerViewModel
import com.micahnyabuto.snap2pdf.utils.FileUtils
import com.micahnyabuto.snap2pdf.utils.Greeting
import org.koin.androidx.compose.koinViewModel
import androidx.core.net.toUri
import com.micahnyabuto.snap2pdf.R
import com.micahnyabuto.snap2pdf.features.settings.AboutDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    activity: Activity,
    scannerViewModel: ScannerViewModel = koinViewModel(),
    documentViewModel: DocumentViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val documentList by documentViewModel.documents.collectAsState()

    val uiState by documentViewModel.uiState.collectAsState()

    var expandedDocId by remember { mutableStateOf<String?>(null) }


    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        HelpAlertDialog (onDismiss = { showDialog = false })
    }

    LaunchedEffect(Unit) {
        documentViewModel.loadDocuments()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.applogo),
                        contentDescription = "Snap2PDF Logo",
                        modifier= Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(6.dp))

                    )
                },
                title = {
                    Column {
                        Text(text = "Snap2PDF", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Images to PDF Converter",
                            style = MaterialTheme.typography.bodySmall)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showDialog = true
                    }) {
                        Icon(Icons.Default.Help, contentDescription = "Help", modifier = Modifier.size(25.dp))
                    }
                    IconButton(onClick = { navController.navigate(Destinations.Search.route) }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", modifier = Modifier.size(25.dp))
                    }


                },
                windowInsets = WindowInsets.statusBars
            )
        },
    ) { innerPadding ->
        when{
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
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
        Column(modifier = Modifier.padding(innerPadding).fillMaxWidth()) {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(documentList) { doc ->
                    Card(
                        onClick = {
                            val uri = doc.uri.toUri()
                            FileUtils.openPdf(context, uri)
                        },
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
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

//                            Image(
//                                painter = painterResource(id = R.drawable.),
//                                contentDescription = "Snap2PDF Logo",
//                                modifier = Modifier
//                                    .size(48.dp)
//                                    .clip(RoundedCornerShape(8.dp))
//                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
//                            )


                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = doc.name,
                                    style = MaterialTheme.typography.titleSmall,
                                )
                                Text(
                                    text = FileUtils.formatTimestamp(doc.createdAt),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                            Box {
                                IconButton(onClick = { expandedDocId =doc.uri}) { // use doc.id or uri
                                    Icon(Icons.Default.MoreVert, contentDescription = "More options")
                                }

                                DropdownMenu(
                                    expanded = expandedDocId == doc.uri,
                                    onDismissRequest = { expandedDocId = null }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Share") },
                                        onClick = {
                                            expandedDocId = null
                                            val uri = Uri.parse(doc.uri)
                                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                                type = "application/pdf"
                                                putExtra(Intent.EXTRA_STREAM, uri)
                                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            }
                                            context.startActivity(Intent.createChooser(shareIntent, "Share PDF"))
                                        },
                                        leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Delete") },
                                        onClick = {
                                            expandedDocId = null
                                            documentViewModel.deleteDocument(doc)
                                        },
                                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null) }
                                    )
                                }
                            }
                        }
                    }
                    HorizontalDivider()
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
        modifier = Modifier.fillMaxSize()
            .padding(all = 50.dp),
        contentAlignment = Alignment.Center
    ){
        Text(text = "You have no documents tap on the Camera button to scan")
    }
}

@Composable
fun HelpAlertDialog(
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.applogo),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(32.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "How to scan a document",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        },
        text = {
            Text(
                text = "To scan a document just tap on the camera button and follow the few simple steps. ",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text("Close")
            }
        }
    )

}


