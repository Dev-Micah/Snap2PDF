package com.micahnyabuto.snap2pdf.features.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
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
    scannerViewModel: ScannerViewModel = koinViewModel(),
    documentViewModel: DocumentViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val documentList by documentViewModel.documents.collectAsState()

    val uiState by documentViewModel.uiState.collectAsState()

    var expandedDocId by remember { mutableStateOf<String?>(null) }



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
                }
            )
        },
    ) { innerPadding ->
        when{
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = "Loading...")
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
                modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(documentList) { doc ->
                    Card(
                        onClick = {
                            val uri = Uri.parse(doc.uri)
                            FileUtils.openPdf(context, uri)

//                           val encodedUri = Uri.encode(doc.uri)
//                            navController.navigate("viewer?uri=$encodedUri")

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
                                            // share logic
                                            val uri = Uri.parse(doc.uri)
                                            // Create intent
                                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                                type = "application/pdf" // since your doc is PDF
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
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "You have no documents")
    }
}


