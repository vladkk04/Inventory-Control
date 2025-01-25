package com.example.bachelorwork.ui.fragments.orders.create.manage.discount

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.domain.model.ValidatorInputFieldFactory
import com.example.bachelorwork.domain.usecase.validators.ValidatorDecimalFormat
import com.example.bachelorwork.domain.usecase.validators.ValidatorNotEmpty
import com.example.bachelorwork.domain.usecase.validators.ValidatorPercentage
import com.example.bachelorwork.ui.model.order.create.DiscountType
import com.example.bachelorwork.ui.model.order.create.manage.discount.OrderManageDiscountUiState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderManageDiscountViewModal @Inject constructor(
    private val navigator: Navigator,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val orderManageDiscountRouteArgs =
        Destination.from<Destination.OrderManageDiscount>(savedStateHandle)

    private val _uiState = MutableStateFlow(OrderManageDiscountUiState())
    val uiState = _uiState.asStateFlow()

    var currentDiscount: String = orderManageDiscountRouteArgs.discount.toString()

    init {
        _uiState.update {
            it.copy(
                discount = orderManageDiscountRouteArgs.discount,
                discountType = orderManageDiscountRouteArgs.discountType
            )
        }
    }

    fun saveDiscountAndNavigateUp() = viewModelScope.launch {
        if (isValidate()) return@launch

        navigator.navigateUp(mapOf("discount" to currentDiscount.toDouble()))
    }

    fun updateDiscount(discount: String) {
        currentDiscount = discount
        _uiState.update {
            it.copy(discountError = null)
        }
    }

    private fun isValidate(): Boolean {

        val validators =
            if (_uiState.value.discountType == DiscountType.PERCENTAGE) setOf(
                ValidatorNotEmpty,
                ValidatorDecimalFormat,
                ValidatorPercentage
            ) else setOf(ValidatorNotEmpty, ValidatorDecimalFormat)

        val factoryValidators = ValidatorInputFieldFactory(
            inputs = arrayOf(currentDiscount),
            validators = validators
        )

        _uiState.update {
            it.copy(
                discountError = factoryValidators.errorMessages[currentDiscount]
            )
        }

        return factoryValidators.hasError
    }

}