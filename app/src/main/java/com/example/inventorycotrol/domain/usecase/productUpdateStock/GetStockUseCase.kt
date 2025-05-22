package com.example.inventorycotrol.domain.usecase.productUpdateStock

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.domain.repository.remote.ProductUpdateStockRemoteDataSource
import kotlinx.coroutines.flow.catch

class GetStockUseCase(
    private val remote: ProductUpdateStockRemoteDataSource,
) {
    suspend fun getAllByProductId(productId: String) = remote.getAllByProductId(productId)
        .catch { emit(ApiResponseResult.Failure(errorMessage = it.message.toString(), 404)) }

    suspend fun getAllByOrganisationView() =
        remote.getAllByOrganisationView().catch { emit(ApiResponseResult.Failure(errorMessage = it.message.toString(), 404)) }
}
