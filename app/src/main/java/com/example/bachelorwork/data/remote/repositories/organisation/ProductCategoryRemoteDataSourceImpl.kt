package com.example.bachelorwork.data.remote.repositories.organisation

import android.content.res.Resources.NotFoundException
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.data.constants.AppConstants
import com.example.bachelorwork.data.remote.dto.ProductCategoryDto
import com.example.bachelorwork.data.remote.services.ProductCategoryApiService
import com.example.bachelorwork.domain.manager.DataStoreManager
import com.example.bachelorwork.domain.model.category.CreateProductCategoryResponse
import com.example.bachelorwork.domain.model.category.ProductCategoryRequest
import com.example.bachelorwork.domain.repository.remote.ProductCategoryRemoteDataSource
import com.example.bachelorwork.ui.utils.extensions.safeApiCallFlow
import com.example.bachelorwork.ui.utils.extensions.safeResponseApiCallFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class ProductCategoryRemoteDataSourceImpl(
    private val api: ProductCategoryApiService,
    private val dataStoreManager: DataStoreManager
) : ProductCategoryRemoteDataSource {

    private suspend fun organisationId(): String =
        dataStoreManager.getPreference(AppConstants.SELECTED_ORGANISATION_ID).firstOrNull()
            ?: throw NotFoundException("Organisation ID not found")

    override suspend fun create(request: ProductCategoryRequest): Flow<ApiResponseResult<CreateProductCategoryResponse>> =
        safeResponseApiCallFlow { api.create(organisationId(), request) }

    override suspend fun update(
        categoryId: String,
        request: ProductCategoryRequest
    ): Flow<ApiResponseResult<Unit>> =
        safeApiCallFlow { api.update(organisationId(), categoryId, request) }

    override suspend fun delete(categoryId: String): Flow<ApiResponseResult<Unit>> =
        safeApiCallFlow { api.delete(organisationId(), categoryId) }

    override suspend fun get(categoryId: String): Flow<ApiResponseResult<ProductCategoryDto>> =
        safeResponseApiCallFlow { api.get(organisationId(), categoryId) }

    override fun getAll(): Flow<ApiResponseResult<List<ProductCategoryDto>>> =
        safeResponseApiCallFlow { api.getAll(organisationId()) }

    /* override suspend fun create(
         request: ProductCategoryRequest
     ): ApiResponseResult<Unit> {
         return sa {
             id()?.let {  }
         }
     }

     override suspend fun update(
         categoryName: String,
         request: ProductCategoryRequest
     ): ApiResponseResult<Unit> {
         return safeApiCall {
             id()?.let { api.update(it, categoryName, request) }
         }
     }

     override suspend fun delete(
         categoryName: String
     ): ApiResponseResult<Unit> {
         return safeApiCall {
             id()?.let { api.delete(it, categoryName) }
         }
     }

     override suspend fun get(
         categoryName: String
     ): Response<ProductCategoryDto?> {
         return api.get(id() ?: "", categoryName)
     }

     override suspend fun getAll(): Response<List<ProductCategoryDto>> {
         return api.getAll(id() ?: "")
     }*/


}