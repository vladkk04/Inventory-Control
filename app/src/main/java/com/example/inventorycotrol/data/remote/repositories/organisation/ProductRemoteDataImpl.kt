package com.example.inventorycotrol.data.remote.repositories.organisation

import android.content.res.Resources.NotFoundException
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.data.remote.dto.ProductDto
import com.example.inventorycotrol.data.remote.services.ProductApiService
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.model.product.ProductRequest
import com.example.inventorycotrol.domain.repository.remote.ProductRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.safeApiCallFlow
import com.example.inventorycotrol.ui.utils.extensions.safeSuspendResponseApiCallFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class ProductRemoteDataImpl(
    private val api: ProductApiService,
    private val dataStoreManager: DataStoreManager
) : ProductRemoteDataSource {

    private suspend fun organisationId(): String =
        dataStoreManager.getPreference(AppConstants.SELECTED_ORGANISATION_ID).firstOrNull()
            ?: throw NotFoundException("No organisation id found")

    override suspend fun create(request: ProductRequest): Flow<ApiResponseResult<ProductDto>> =
        safeSuspendResponseApiCallFlow { api.create(organisationId(), request) }

    override suspend fun update(
        productId: String,
        request: ProductRequest
    ): Flow<ApiResponseResult<ProductDto>> =
        safeSuspendResponseApiCallFlow { api.update(organisationId(), productId, request) }

    override suspend fun delete(productId: String): Flow<ApiResponseResult<Unit>> =
        safeApiCallFlow { api.delete(organisationId(), productId) }

    override suspend fun get(productId: String): Flow<ApiResponseResult<ProductDto>> =
        safeSuspendResponseApiCallFlow { api.get(organisationId(), productId) }

    override fun getAll(): Flow<ApiResponseResult<List<ProductDto>>> = safeSuspendResponseApiCallFlow { api.getAll(organisationId()) }

}