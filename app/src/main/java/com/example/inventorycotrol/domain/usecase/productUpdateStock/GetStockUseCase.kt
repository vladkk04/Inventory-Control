package com.example.inventorycotrol.domain.usecase.productUpdateStock

import com.example.inventorycotrol.domain.repository.remote.ProductUpdateStockRemoteDataSource

class GetStockUseCase(
    private val remote: ProductUpdateStockRemoteDataSource,
) {
    suspend fun getAllByProductId(productId: String) = remote.getAllByProductId(productId)

    suspend fun getAllByOrganisation() = remote.getAllByOrganisation()

    suspend fun getAllByOrganisationView() = remote.getAllByOrganisationView()
}
