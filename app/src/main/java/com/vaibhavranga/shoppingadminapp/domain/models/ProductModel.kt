package com.vaibhavranga.shoppingadminapp.domain.models

data class ProductModel(
    val name: String = "",
    val price: String = "",
    val finalPrice: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val category: String = "",
    val date: Long = System.currentTimeMillis(),
    val availableUnits: Int = 0
)
