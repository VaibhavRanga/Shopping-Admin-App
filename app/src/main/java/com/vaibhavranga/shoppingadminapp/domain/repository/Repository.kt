package com.vaibhavranga.shoppingadminapp.domain.repository

import android.net.Uri
import com.vaibhavranga.shoppingadminapp.common.ResultState
import com.vaibhavranga.shoppingadminapp.domain.models.CategoryModel
import com.vaibhavranga.shoppingadminapp.domain.models.ProductModel
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun addCategory(category: CategoryModel): Flow<ResultState<String>>

    suspend fun getAllCategories(): Flow<ResultState<List<CategoryModel>>>

    suspend fun addProduct(product: ProductModel): Flow<ResultState<String>>

    suspend fun addProductImage(imageUri: Uri): Flow<ResultState<String>>

    suspend fun addCategoryImage(imageUri: Uri): Flow<ResultState<String>>
}