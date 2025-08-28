//package com.micahnyabuto.snap2pdf.features.docviewer
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.os.ParcelFileDescriptor
//import com.shockwave.pdfium.PdfiumCore
//import java.io.File
//
//fun renderPdfPage(context: Context, file: File, pageIndex: Int): Bitmap? {
//    return try {
//        val pdfiumCore = PdfiumCore(context)
//        val fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
//        val pdfDocument = pdfiumCore.newDocument(fd)
//        pdfiumCore.openPage(pdfDocument, pageIndex)
//
//        val width = pdfiumCore.getPageWidthPoint(pdfDocument, pageIndex)
//        val height = pdfiumCore.getPageHeightPoint(pdfDocument, pageIndex)
//        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//
//        pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageIndex, 0, 0, width, height)
//        pdfiumCore.closeDocument(pdfDocument)
//        bitmap
//    } catch (e: Exception) {
//        e.printStackTrace()
//        null
//    }
//}
