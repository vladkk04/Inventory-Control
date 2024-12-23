package com.example.bachelorwork.domain.usecase.barcodeScanner

import com.example.bachelorwork.domain.repository.BarcodeScannerRepository
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BarcodeScannerUseCase @Inject constructor(
    private val repository: BarcodeScannerRepository
) {
    operator fun invoke(): Flow<Result<Barcode>> = flow {
        repository.startScan().mapCatching { result ->
            emit(Result.success(result))
        }.onFailure { error ->
            emit(Result.failure(error))
        }
    }
}