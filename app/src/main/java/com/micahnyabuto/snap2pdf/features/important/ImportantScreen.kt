//package com.micahnyabuto.snap2pdf.features.important
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.micahnyabuto.snap2pdf.core.data.local.Document
//import com.micahnyabuto.snap2pdf.features.home.DocumentViewModel
//import org.koin.androidx.compose.koinViewModel
//import java.text.DateFormat
//import java.util.Date
//
//
//@Composable
//fun ImportantScreen(
//    documentViewModel: DocumentViewModel = koinViewModel(),
//    onDocumentClick: (Document) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val importantDocs by documentViewModel.importantDocuments.collectAsStateWithLifecycle(emptyList())
//
//
//
//
//    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
//        Text(
//            text = "Must Read",
//            style = MaterialTheme.typography.headlineMedium,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        if (importantDocs.isEmpty()) {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text("No important documents yet.")
//            }
//        } else {
//            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//                items(importantDocs) { doc ->
//                    ImportantDocumentCard(
//                        document = doc,
//                        onClick = { onDocumentClick(doc) },
//                        onUnmark = { documentViewModel.unmarkAsImportant(doc) }
//                    )
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun ImportantDocumentCard(
//    document: Document,
//    onClick: () -> Unit,
//    onUnmark: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onClick() },
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column {
//                Text(text = document.name, style = MaterialTheme.typography.titleMedium)
//                Text(
//                    text = "Saved: ${DateFormat.getDateTimeInstance().format(Date(document.createdAt))}",
//                    style = MaterialTheme.typography.bodySmall
//                )
//            }
//
//            IconButton(onClick = onUnmark) {
//                androidx.compose.material3.Icon(
//                    imageVector = Icons.Default.Star,
//                    contentDescription = "Mark as important",
//                    tint = Color.Yellow
//                )
//
//            }
//        }
//    }
//}
