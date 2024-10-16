package com.example.bachelorwork.domain.repository

import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.flow.Flow

interface BarcodeScannerRepository {
    suspend fun startScan(): Result<Barcode>
}