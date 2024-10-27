package com.example.bachelorwork.domain.repository

import com.google.mlkit.vision.barcode.common.Barcode

interface BarcodeScannerRepository {
    suspend fun startScan(): Result<Barcode>
}