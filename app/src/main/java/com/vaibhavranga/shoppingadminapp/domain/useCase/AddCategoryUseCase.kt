package com.vaibhavranga.shoppingadminapp.domain.useCase

import com.vaibhavranga.shoppingadminapp.domain.models.CategoryModel
import com.vaibhavranga.shoppingadminapp.domain.repository.Repository
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun addCategoryUseCase(category: CategoryModel) =
        repository.addCategory(category = category)
}