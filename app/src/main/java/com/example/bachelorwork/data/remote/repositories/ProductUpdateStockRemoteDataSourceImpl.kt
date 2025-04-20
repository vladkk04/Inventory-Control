package com.example.bachelorwork.data.remote.repositories

import android.content.res.Resources.NotFoundException
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.data.constants.AppConstants
import com.example.bachelorwork.data.remote.dto.ChangeStockProductDto
import com.example.bachelorwork.data.remote.dto.ProductUpdateStockDto
import com.example.bachelorwork.data.remote.dto.ProductUpdateStockViewDto
import com.example.bachelorwork.data.remote.services.ProductUpdateStockApiService
import com.example.bachelorwork.domain.manager.DataStoreManager
import com.example.bachelorwork.domain.model.updateStock.ProductUpdateStockRequest
import com.example.bachelorwork.domain.repository.remote.ProductUpdateStockRemoteDataSource
import com.example.bachelorwork.ui.utils.extensions.safeResponseApiCallFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class ProductUpdateStockRemoteDataSourceImpl(
    private val api: ProductUpdateStockApiService,
    private val dataStoreManager: DataStoreManager
) : ProductUpdateStockRemoteDataSource {

    private suspend fun organisationId(): String =
        dataStoreManager.getPreference(AppConstants.SELECTED_ORGANISATION_ID).firstOrNull()
            ?: throw NotFoundException("Organisation ID not found")

    override suspend fun updateStock(
        request: ProductUpdateStockRequest
    ): Flow<ApiResponseResult<ProductUpdateStockDto>> =
        safeResponseApiCallFlow { api.create(organisationId(), request) }

    override suspend fun getAllByProductId(productId: String): Flow<ApiResponseResult<List<ChangeStockProductDto>>> =
        safeResponseApiCallFlow { api.getAllByProductId(organisationId(), productId) }

    override suspend fun getAllByOrganisation(): Flow<ApiResponseResult<List<ProductUpdateStockDto>>> =
        safeResponseApiCallFlow { api.getAllByOrganisation(organisationId()) }

    override suspend fun getAllByOrganisationView(): Flow<ApiResponseResult<List<ProductUpdateStockViewDto>>> =
        safeResponseApiCallFlow { api.getAllByOrganisationView(organisationId()) }


}