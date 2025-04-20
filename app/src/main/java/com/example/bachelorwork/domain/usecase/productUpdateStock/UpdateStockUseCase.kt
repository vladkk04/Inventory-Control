package com.example.bachelorwork.domain.usecase.productUpdateStock

import com.example.bachelorwork.domain.model.updateStock.ProductUpdateStockRequest
import com.example.bachelorwork.domain.repository.remote.ProductUpdateStockRemoteDataSource

class UpdateStockUseCase(
    private val remote: ProductUpdateStockRemoteDataSource,
) {
    suspend operator fun invoke(request: ProductUpdateStockRequest) = remote.updateStock(request)
}
