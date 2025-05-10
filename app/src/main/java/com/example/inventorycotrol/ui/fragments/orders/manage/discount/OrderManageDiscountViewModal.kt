package com.example.inventorycotrol.ui.fragments.orders.manage.discount

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.domain.model.validator.InputValidator
import com.example.inventorycotrol.ui.model.order.DiscountType
import com.example.inventorycotrol.ui.model.order.discount.OrderManageDiscountUiState
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderManageDiscountViewModal @Inject constructor(
    private val navigator: AppNavigator,
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
        if (!isValidateInput()) return@launch

        navigator.navigateUp(mapOf("discount" to currentDiscount.toDouble()))
    }

    fun updateDiscount(discount: String) {
        currentDiscount = discount
        _uiState.update {
            it.copy(discountError = null)
        }
    }

    private fun isValidateInput(): Boolean {

        val inputValidatorPercentage = InputValidator
            .create()
            .withNotEmpty()
            .withPercentage()

        val inputValidatorCustom = InputValidator
            .create()
            .withNotEmpty()
            .withNotGreaterThenValue(
                orderManageDiscountRouteArgs.subtotal,
                "The value cannot be greater then subtotal"
            )

        return when (_uiState.value.discountType) {
            DiscountType.PERCENTAGE -> {
                _uiState.update {
                    it.copy(
                        discountError = inputValidatorPercentage.build()
                            .invoke(currentDiscount).errorMessage
                    )
                }
                !inputValidatorPercentage.build().invoke(currentDiscount).hasError
            }

            DiscountType.FIXED -> {
                _uiState.update {
                    it.copy(
                        discountError = inputValidatorCustom.build()
                            .invoke(currentDiscount).errorMessage
                    )
                }
                !inputValidatorCustom.build().invoke(currentDiscount).hasError
            }
        }
    }

}