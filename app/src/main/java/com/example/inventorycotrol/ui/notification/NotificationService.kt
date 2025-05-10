package com.example.inventorycotrol.ui.notification

import com.example.inventorycotrol.domain.model.product.Product

interface NotificationService {

    fun showNotification(products: List<Product>)

}