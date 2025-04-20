package com.example.bachelorwork.data.remote.repositories.organisation

import android.content.res.Resources.NotFoundException
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.data.constants.AppConstants
import com.example.bachelorwork.data.remote.dto.ProductDto
import com.example.bachelorwork.data.remote.services.ProductApiService
import com.example.bachelorwork.domain.manager.DataStoreManager
import com.example.bachelorwork.domain.model.product.ProductRequest
import com.example.bachelorwork.domain.repository.remote.ProductRemoteDataSource
import com.example.bachelorwork.ui.utils.extensions.safeApiCallFlow
import com.example.bachelorwork.ui.utils.extensions.safeResponseApiCallFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class ProductRemoteDataImpl(
    private val api: ProductApiService,
    private val dataStoreManager: DataStoreManager
) : ProductRemoteDataSource {

    private suspend fun organisationId(): String =
        dataStoreManager.getPreference(AppConstants.SELECTED_ORGANISATION_ID).firstOrNull()
            ?: throw NotFoundException()

    override suspend fun create(request: ProductRequest): Flow<ApiResponseResult<ProductDto>> =
        safeResponseApiCallFlow { api.create(organisationId(), request) }

    override suspend fun update(
        productId: String,
        request: ProductRequest
    ): Flow<ApiResponseResult<Unit>> =
        safeApiCallFlow { api.update(organisationId(), productId, request) }

    override suspend fun delete(productId: String): Flow<ApiResponseResult<Unit>> =
        safeApiCallFlow { api.delete(organisationId(), productId) }

    override suspend fun get(productId: String): Flow<ApiResponseResult<ProductDto>> =
        safeResponseApiCallFlow { api.get(organisationId(), productId) }

    override suspend fun getAll(): Flow<ApiResponseResult<List<ProductDto>>> =
        safeResponseApiCallFlow { api.getAll(organisationId()) }


}