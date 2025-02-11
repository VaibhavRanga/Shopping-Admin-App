package com.vaibhavranga.shoppingadminapp.domain.useCase

import android.net.Uri
import com.vaibhavranga.shoppingadminapp.domain.repository.Repository
import javax.inject.Inject

class AddProductImageUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun addProductImageUseCase(
        imageUri: Uri
    ) =
        repository.addProductImage(
            imageUri = imageUri
        )
}