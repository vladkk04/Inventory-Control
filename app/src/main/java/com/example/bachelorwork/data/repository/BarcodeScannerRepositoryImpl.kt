package com.example.bachelorwork.data.repository

import android.content.Context
import com.example.bachelorwork.domain.repository.BarcodeScannerRepository
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BarcodeScannerRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BarcodeScannerRepository {

    override suspend fun startScan(): Result<Barcode> {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).build()

        val scanner = GmsBarcodeScanning.getClient(context, options)

        return suspendCoroutine { continuation ->
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    continuation.resume(Result.success(barcode))
                }
                .addOnFailureListener {
                    continuation.resume(Result.failure(it))
                }
        }
    }
}