package com.vaibhavranga.shoppingadminapp.data.repositoryImpl

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vaibhavranga.shoppingadminapp.common.CATEGORY_PATH
import com.vaibhavranga.shoppingadminapp.common.PRODUCT_PATH
import com.vaibhavranga.shoppingadminapp.common.ResultState
import com.vaibhavranga.shoppingadminapp.domain.models.CategoryModel
import com.vaibhavranga.shoppingadminapp.domain.models.ProductModel
import com.vaibhavranga.shoppingadminapp.domain.repository.Repository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : Repository {
    override suspend fun addCategory(category: CategoryModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            try {
                firestore.collection(CATEGORY_PATH).add(category)
                    .addOnSuccessListener {
                        trySend(ResultState.Success(data = "Category added successfully"))
                    }
                    .addOnFailureListener {
                        trySend(ResultState.Error(error = it.message.toString()))
                    }

            } catch (e: Exception) {
                trySend(ResultState.Error(error = e.message.toString()))
            }
            awaitClose {
                close()
            }
        }

    override suspend fun addCategoryImage(imageUri: Uri): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        try {
            storage.reference.child("$CATEGORY_PATH/${System.currentTimeMillis()}").putFile(imageUri ?: Uri.EMPTY)
                .addOnSuccessListener {
                    it.storage.downloadUrl
                        .addOnSuccessListener { downloadUrl ->
                            trySend(ResultState.Success(data = downloadUrl.toString()))
                        }
                        .addOnFailureListener { exception ->
                            trySend(ResultState.Error(error = exception.message.toString()))
                        }
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(error = it.message.toString()))
                }
        } catch (e: Exception) {
            trySend(ResultState.Error(error = e.message.toString()))
        }

        awaitClose {
            close()
        }
    }

    override suspend fun getAllCategories(): Flow<ResultState<List<CategoryModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        try {
            firestore.collection(CATEGORY_PATH).get()
                .addOnSuccessListener {
                    val categories = it.documents.mapNotNull { document ->
                        document.toObject(CategoryModel::class.java)
                    }
                    trySend(ResultState.Success(data = categories))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(error = it.message.toString()))
                }
        } catch (e: Exception) {
            trySend(ResultState.Error(error = e.message.toString()))
        }
        awaitClose {
            close()
        }
    }

    override suspend fun addProduct(product: ProductModel): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        try {
            firestore.collection(PRODUCT_PATH).add(product)
                .addOnSuccessListener {
                    trySend(ResultState.Success(data = "Product added successfully"))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(error = it.message.toString()))
                }
        } catch (e: Exception) {
            trySend(ResultState.Error(error = e.message.toString()))
        }

        awaitClose {
            close()
        }
    }

    override suspend fun addProductImage(imageUri: Uri): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            storage.reference.child("$PRODUCT_PATH/${System.currentTimeMillis()}").putFile(imageUri ?: Uri.EMPTY)
                .addOnSuccessListener {
                    it.storage.downloadUrl
                        .addOnSuccessListener { downloadUrl ->
                            trySend(ResultState.Success(data = downloadUrl.toString()))
                        }
                        .addOnFailureListener { exception ->
                            trySend(ResultState.Error(error = exception.message.toString()))
                        }
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(error = it.message.toString()))
                }
        } catch (e: Exception) {
            trySend(ResultState.Error(error = e.message.toString()))
        }

        awaitClose {
            close()
        }
    }
}