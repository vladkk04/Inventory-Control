package com.example.inventorycotrol.ui.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.example.inventorycotrol.domain.model.TimePeriodReports
import java.io.File
import java.io.FileOutputStream


data class ProductPdf(
    val name: String,
    val qtyIn: Double,
    val qtyOut: Double,
    val stockOnHand: Double
)

class PdfReportGenerator(private val context: Context) {

    companion object {
        private const val PDF_PAGE_WIDTH = 595
        private const val PDF_PAGE_HEIGHT = 842
    }

    fun generateStockReport(
        organizationName: String,
        creationDate: String,
        periodReports: TimePeriodReports,
        dataRange: String,
        products: List<ProductPdf>,
        callback: (File?) -> Unit
    ) {
        val doc = PdfDocument()
        val paint = Paint().apply {
            color = Color.BLACK
            isAntiAlias = true
        }

        val pageInfo = PdfDocument.PageInfo.Builder(PDF_PAGE_WIDTH, PDF_PAGE_HEIGHT, 1).create()
        val page = doc.startPage(pageInfo)
        val canvas = page.canvas

        var currentY = 50f

        // Organization name
        paint.textSize = 16f
        canvas.drawText(organizationName, 50f, currentY, paint)
        currentY += 30f

        // Date and Period
        paint.textSize = 12f
        canvas.drawText("Date of creation: $creationDate", 50f, currentY, paint)
        currentY += 20f

        canvas.drawText("Data Range: $dataRange", 50f, currentY, paint)
        currentY += 20f

        canvas.drawText(
            "Period: ${periodReports.name.replace("_", " ")}",
            50f, currentY, paint
        )



        currentY += 40f

        if (products.isEmpty()) {
            // Show "No data available" message
            paint.textSize = 14f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            canvas.drawText(
                "No stock data available for the selected period",
                50f, currentY, paint
            )
            currentY += 30f
        } else {
            // Draw table header (now with 4 columns)
            drawTableHeader(canvas, currentY)
            currentY += 30f

            // Draw product rows
            products.forEach { product ->
                drawProductRow(canvas, product, currentY)
                currentY += 30f

                if (currentY > PDF_PAGE_HEIGHT - 50) {
                    doc.finishPage(page)
                    val newPageInfo = PdfDocument.PageInfo.Builder(
                        PDF_PAGE_WIDTH,
                        PDF_PAGE_HEIGHT,
                        doc.pages.size + 1
                    ).create()
                    val newPage = doc.startPage(newPageInfo)
                    currentY = 50f
                    drawTableHeader(newPage.canvas, currentY)
                    currentY += 30f
                }
            }
        }

        // Page number (only if we have data)
        if (products.isNotEmpty()) {
            paint.textSize = 10f
            canvas.drawText(
                "Page 1 of 1",
                PDF_PAGE_WIDTH - 100f,
                PDF_PAGE_HEIGHT - 30f,
                paint
            )
        }

        doc.finishPage(page)
        savePdfDocument(doc, "stock_report_${System.currentTimeMillis()}.pdf", callback)
    }

    private fun drawTableHeader(canvas: Canvas, yPos: Float) {
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 12f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        // Now 4 columns instead of 5
        val colWidth = (PDF_PAGE_WIDTH - 100) / 4f
        val xPositions = listOf(
            50f,              // Product
            50f + colWidth,   // Stock In
            50f + 2 * colWidth, // Stock Out
            50f + 3 * colWidth  // Stock on Hand
        )

        // Headers (without Ordered column)
        canvas.drawText("Product", xPositions[0], yPos, paint)
        canvas.drawText("Stock In", xPositions[1], yPos, paint)
        canvas.drawText("Stock Out", xPositions[2], yPos, paint)
        canvas.drawText("Stock on Hand", xPositions[3], yPos, paint)

        // Line under headers
        paint.strokeWidth = 1f
        canvas.drawLine(50f, yPos + 10, PDF_PAGE_WIDTH - 50f, yPos + 10, paint)
    }

    private fun drawProductRow(canvas: Canvas, product: ProductPdf, yPos: Float) {
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 10f
            isAntiAlias = true
        }

        // Now 4 columns instead of 5
        val colWidth = (PDF_PAGE_WIDTH - 100) / 4f
        val xPositions = listOf(
            50f,              // Product
            50f + colWidth,   // Stock In
            50f + 2 * colWidth, // Stock Out
            50f + 3 * colWidth  // Stock on Hand
        )

        val productName = if (product.name.length > 20) {
            product.name.substring(0, 17) + "..."
        } else {
            product.name
        }

        canvas.drawText(productName, xPositions[0], yPos, paint)
        canvas.drawText(product.qtyIn.toString(), xPositions[1], yPos, paint)
        canvas.drawText(product.qtyOut.toString(), xPositions[2], yPos, paint)
        canvas.drawText(product.stockOnHand.toString(), xPositions[3], yPos, paint)

        // Line between rows
        paint.strokeWidth = 0.5f
        canvas.drawLine(50f, yPos + 15, PDF_PAGE_WIDTH - 50f, yPos + 15, paint)
    }

    private fun savePdfDocument(doc: PdfDocument, fileName: String, callback: (File?) -> Unit) {
        try {
            val downloadsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)

            FileOutputStream(file).use { outputStream ->
                doc.writeTo(outputStream)
                callback(file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callback(null)
        } finally {
            doc.close()
        }
    }

}