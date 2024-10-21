package com.example.bachelorwork.domain.usecase.barcodeScanner

import com.example.bachelorwork.common.Resources
import com.example.bachelorwork.domain.repository.BarcodeScannerRepository
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BarcodeScannerUseCase @Inject constructor(
    private val repository: BarcodeScannerRepository
) {
    suspend operator fun invoke(): Flow<Resources<Barcode>> = flow {
        repository.startScan().mapCatching { result ->
            emit(Resources.Success(result))
        }.onFailure { error ->
            emit(Resources.Error(error.message.toString()))
        }
    }
}