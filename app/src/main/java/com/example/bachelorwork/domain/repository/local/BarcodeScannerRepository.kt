package com.example.bachelorwork.domain.repository.local

import com.google.mlkit.vision.barcode.common.Barcode

interface BarcodeScannerRepository {
    suspend fun startScan(): Result<Barcode>
}