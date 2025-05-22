package com.example.inventorycotrol.ui.utils

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

class PdfReportGenerator {
    companion object {
        private const val PDF_PAGE_WIDTH = 595
        private const val PDF_PAGE_HEIGHT = 842
    }

    fun generateStockReport(
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
        var page = doc.startPage(pageInfo)
        var canvas = page.canvas

        var currentY = 50f

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
            paint.textSize = 14f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            canvas.drawText(
                "No stock data available for the selected period",
                50f, currentY, paint
            )
            currentY += 30f
        } else {
            drawTableHeader(canvas, currentY)
            currentY += 30f


            products.forEach { product ->
                drawProductRow(canvas, product, currentY)
                currentY += 30f

                if (currentY > PDF_PAGE_HEIGHT - 100) {
                    paint.textSize = 10f
                    canvas.drawText(
                        "Page ${doc.pages.size + 1} of ${doc.pages.size + 1}",
                        PDF_PAGE_WIDTH - 100f,
                        PDF_PAGE_HEIGHT - 30f,
                        paint
                    )
                    doc.finishPage(page)
                    page = doc.startPage(PdfDocument.PageInfo.Builder(
                        PDF_PAGE_WIDTH,
                        PDF_PAGE_HEIGHT,
                        doc.pages.size + 1
                    ).create())

                    canvas = page.canvas
                    currentY = 50f

                    drawTableHeader(canvas, currentY)
                    currentY += 30f
                }
            }
        }

        paint.textSize = 10f
        canvas.drawText(
            "Page ${doc.pages.size + 1}",
            PDF_PAGE_WIDTH - 60f,
            PDF_PAGE_HEIGHT - 30f,
            paint
        )

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

        val colWidth = (PDF_PAGE_WIDTH - 100) / 4f
        val xPositions = listOf(
            50f,
            50f + colWidth,
            50f + 2 * colWidth,
            50f + 3 * colWidth
        )

        canvas.drawText("Product", xPositions[0], yPos, paint)
        canvas.drawText("Stock In", xPositions[1], yPos, paint)
        canvas.drawText("Stock Out", xPositions[2], yPos, paint)
        canvas.drawText("Stock on Hand", xPositions[3], yPos, paint)

        paint.strokeWidth = 1f
        canvas.drawLine(50f, yPos + 10, PDF_PAGE_WIDTH - 50f, yPos + 10, paint)
    }

    private fun drawProductRow(canvas: Canvas, product: ProductPdf, yPos: Float) {
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 10f
            isAntiAlias = true
        }

        val colWidth = (PDF_PAGE_WIDTH - 100) / 4f
        val xPositions = listOf(
            50f,
            50f + colWidth,
            50f + 2 * colWidth,
            50f + 3 * colWidth
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