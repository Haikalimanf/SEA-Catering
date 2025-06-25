package com.example.seacatering.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.seacatering.model.DataAnalyticsResult
import com.example.seacatering.utils.FormatRupiah.formatRupiah
import com.google.firebase.Timestamp
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

object PdfReportGenerator {
    fun generateSubscriptionReport(
        context: Context,
        startDate: Timestamp?,
        endDate: Timestamp?,
        data: DataAnalyticsResult?
    ): File? {
        val docsFolder = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString())
        if (!docsFolder.exists()) docsFolder.mkdirs()

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val paintTitle = Paint().apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textSize = 20f
            color = Color.BLACK
        }

        val paintBody = Paint().apply {
            textSize = 16f
            color = Color.DKGRAY
        }

        canvas.drawText("Laporan Statistik Subscription", 40f, 60f, paintTitle)

        val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        val startDateText = startDate?.toDate()?.let { dateFormatter.format(it) } ?: "-"
        val endDateText = endDate?.toDate()?.let { dateFormatter.format(it) } ?: "-"

        var y = 100f
        val spacing = 30f
        canvas.drawText("Periode: $startDateText - $endDateText", 40f, y, paintBody)

        y += spacing
        canvas.drawText("Langganan Baru: ${data?.newSubscriptions}", 40f, y, paintBody)

        y += spacing
        canvas.drawText("Pendapatan (MRR): ${formatRupiah(data?.recurringRevenue)}", 40f, y, paintBody)

        y += spacing
        canvas.drawText("Reaktivasi: ${data?.reactivations}", 40f, y, paintBody)

        y += spacing
        canvas.drawText("Langganan Aktif: ${data?.subscriptionActive}", 40f, y, paintBody)

        pdfDocument.finishPage(page)

        val fileName = "Laporan_Subscription_${startDateText}_sampai_${endDateText}.pdf"
        val file = File(docsFolder, fileName)

        return try {
            pdfDocument.writeTo(FileOutputStream(file))
            pdfDocument.close()
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun openPdf(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Tidak ada aplikasi pembaca PDF", Toast.LENGTH_LONG).show()
        }
    }
}
