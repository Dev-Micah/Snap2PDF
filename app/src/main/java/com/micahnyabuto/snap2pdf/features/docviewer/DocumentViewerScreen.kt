//package com.micahnyabuto.snap2pdf.features.docviewer
//
//import android.graphics.Bitmap
//import android.net.Uri
//import android.os.ParcelFileDescriptor
//import android.widget.Toast
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import com.micahnyabuto.snap2pdf.utils.FileUtils.copyToCache
//import com.shockwave.pdfium.PdfiumCore
//
//@Composable
//fun PdfViewerScreen(
//    documentUri: Uri,
//    modifier: Modifier = Modifier
//) {
//    val context = LocalContext.current
//    var pageBitmaps by remember { mutableStateOf<List<Bitmap>>(emptyList()) }
//
//    LaunchedEffect(documentUri) {
//        val file = copyToCache(context, documentUri)
//        if (file != null) {
//            val pdfiumCore = PdfiumCore(context)
//            val fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
//            val pdfDocument = pdfiumCore.newDocument(fd)
//            val pageCount = pdfiumCore.getPageCount(pdfDocument)
//
//            val bitmaps = mutableListOf<Bitmap>()
//            for (i in 0 until pageCount) {
//                pdfiumCore.openPage(pdfDocument, i)
//                val width = pdfiumCore.getPageWidthPoint(pdfDocument, i)
//                val height = pdfiumCore.getPageHeightPoint(pdfDocument, i)
//                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//                pdfiumCore.renderPageBitmap(pdfDocument, bitmap, i, 0, 0, width, height)
//                bitmaps.add(bitmap)
//            }
//            pdfiumCore.closeDocument(pdfDocument)
//            pageBitmaps = bitmaps
//        } else {
//            Toast.makeText(context, "Failed to load PDF", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    LazyColumn(modifier = modifier.fillMaxSize()) {
//        items(pageBitmaps.size) { index ->
//            Image(
//                bitmap = pageBitmaps[index].asImageBitmap(),
//                contentDescription = "PDF Page ${index + 1}",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//            )
//        }
//    }
//}
//
