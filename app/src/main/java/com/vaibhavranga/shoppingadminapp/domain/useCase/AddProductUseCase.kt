package com.vaibhavranga.shoppingadminapp.domain.useCase

import com.vaibhavranga.shoppingadminapp.domain.models.ProductModel
import com.vaibhavranga.shoppingadminapp.domain.repository.Repository
import javax.inject.Inject

class AddProductUseCase @Inject constructor(private val repository: Repository) {
    suspend fun addProductUseCase(product: ProductModel) = repository.addProduct(product = product)
}