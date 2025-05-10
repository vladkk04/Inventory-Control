package com.example.inventorycotrol.domain.repository.remote

import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.remote.dto.ChangeStockProductDto
import com.example.inventorycotrol.data.remote.dto.ProductUpdateStockDto
import com.example.inventorycotrol.data.remote.dto.ProductUpdateStockViewDto
import com.example.inventorycotrol.domain.model.updateStock.ProductUpdateStockRequest
import kotlinx.coroutines.flow.Flow

interface ProductUpdateStockRemoteDataSource {

    suspend fun updateStock(request: ProductUpdateStockRequest): Flow<ApiResponseResult<ProductUpdateStockDto>>

    suspend fun getAllByProductId(productId: String): Flow<ApiResponseResult<List<ChangeStockProductDto>>>

    suspend fun getAllByOrganisation(): Flow<ApiResponseResult<List<ProductUpdateStockDto>>>

    suspend fun getAllByOrganisationView(): Flow<ApiResponseResult<List<ProductUpdateStockViewDto>>>

}