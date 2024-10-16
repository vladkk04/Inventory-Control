package com.example.bachelorwork.data.repository

import android.content.Context
import com.example.bachelorwork.domain.repository.BarcodeScannerRepository
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class BarcodeScannerImplRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : BarcodeScannerRepository {

    override suspend fun startScan(): Result<Barcode> {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).build()

        val scanner = GmsBarcodeScanning.getClient(context, options)

        return try {
            val barcode = scanner.startScan().result
            Result.success(barcode)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}