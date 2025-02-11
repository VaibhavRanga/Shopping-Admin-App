package com.vaibhavranga.shoppingadminapp.domain.useCase

import android.net.Uri
import com.vaibhavranga.shoppingadminapp.domain.repository.Repository
import javax.inject.Inject

class AddCategoryImageUseCase @Inject constructor(private val repository: Repository) {
    suspend fun addCategoryImageUseCase(imageUri: Uri) =
        repository.addCategoryImage(imageUri = imageUri)
}