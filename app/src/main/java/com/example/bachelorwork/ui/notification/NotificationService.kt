package com.example.bachelorwork.ui.notification

import com.example.bachelorwork.domain.model.product.Product

interface NotificationService {

    fun showNotification(products: List<Product>)

}