package com.example.inventorycotrol.data.remote.repositories.organisation

import android.content.res.Resources.NotFoundException
import com.example.inventorycotrol.common.ApiResponseResult
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.data.remote.dto.OrderDto
import com.example.inventorycotrol.data.remote.services.OrderApiService
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.model.order.OrderRequest
import com.example.inventorycotrol.domain.repository.remote.OrderRemoteDataSource
import com.example.inventorycotrol.ui.utils.extensions.safeApiCallFlow
import com.example.inventorycotrol.ui.utils.extensions.safeResponseApiCallFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class OrderRemoteDataSourceImpl(
    private val api: OrderApiService,
    private val dataStoreManager: DataStoreManager
): OrderRemoteDataSource {

    private suspend fun organisationId(): String =
        dataStoreManager.getPreference(AppConstants.SELECTED_ORGANISATION_ID).firstOrNull()
            ?: throw NotFoundException("Organisation ID not found")

    override suspend fun create(request: OrderRequest): Flow<ApiResponseResult<OrderDto>> =
        safeResponseApiCallFlow { api.create(organisationId(), request) }

    override suspend fun update(
        orderId: String,
        request: OrderRequest
    ): Flow<ApiResponseResult<Unit>> =
        safeApiCallFlow { api.update(organisationId(), orderId, request) }

    override suspend fun delete(orderId: String): Flow<ApiResponseResult<Unit>> =
        safeApiCallFlow { api.delete(organisationId(), orderId) }

    override suspend fun get(orderId: String): Flow<ApiResponseResult<OrderDto>> =
        safeResponseApiCallFlow { api.get(organisationId(), orderId) }

    override suspend fun getAll(): Flow<ApiResponseResult<List<OrderDto>>> =
        safeResponseApiCallFlow { api.getAll(organisationId()) }
}