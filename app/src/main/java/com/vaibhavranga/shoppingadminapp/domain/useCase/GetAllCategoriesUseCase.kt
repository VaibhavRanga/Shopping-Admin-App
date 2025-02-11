package com.vaibhavranga.shoppingadminapp.domain.useCase

import com.vaibhavranga.shoppingadminapp.domain.repository.Repository
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(private val repository: Repository) {
    suspend fun getAllCategoriesUseCase() = repository.getAllCategories()
}