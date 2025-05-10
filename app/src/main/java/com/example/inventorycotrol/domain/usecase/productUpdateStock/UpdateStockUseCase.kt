package com.example.inventorycotrol.domain.usecase.productUpdateStock

import com.example.inventorycotrol.domain.model.updateStock.ProductUpdateStockRequest
import com.example.inventorycotrol.domain.repository.remote.ProductUpdateStockRemoteDataSource

class UpdateStockUseCase(
    private val remote: ProductUpdateStockRemoteDataSource,
) {
    suspend operator fun invoke(request: ProductUpdateStockRequest) = remote.updateStock(request)
}
