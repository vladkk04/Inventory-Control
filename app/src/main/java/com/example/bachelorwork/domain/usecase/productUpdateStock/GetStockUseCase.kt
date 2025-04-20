package com.example.bachelorwork.domain.usecase.productUpdateStock

import com.example.bachelorwork.domain.repository.remote.ProductUpdateStockRemoteDataSource

class GetStockUseCase(
    private val remote: ProductUpdateStockRemoteDataSource,
) {
    suspend fun getAllByProductId(productId: String) = remote.getAllByProductId(productId)

    suspend fun getAllByOrganisation() = remote.getAllByOrganisation()

    suspend fun getAllByOrganisationView() = remote.getAllByOrganisationView()
}
