package com.vaibhavranga.shoppingadminapp.presentation.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhavranga.shoppingadminapp.common.ResultState
import com.vaibhavranga.shoppingadminapp.domain.models.CategoryModel
import com.vaibhavranga.shoppingadminapp.domain.models.ProductModel
import com.vaibhavranga.shoppingadminapp.domain.useCase.AddCategoryImageUseCase
import com.vaibhavranga.shoppingadminapp.domain.useCase.AddCategoryUseCase
import com.vaibhavranga.shoppingadminapp.domain.useCase.AddProductImageUseCase
import com.vaibhavranga.shoppingadminapp.domain.useCase.AddProductUseCase
import com.vaibhavranga.shoppingadminapp.domain.useCase.GetAllCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val addProductImageUseCase: AddProductImageUseCase,
    private val addCategoryImageUseCase: AddCategoryImageUseCase
) : ViewModel() {
    private val _addCategory = MutableStateFlow(AddCategoryState())
    val addCategory = _addCategory.asStateFlow()

    private val _addCategoryImage = MutableStateFlow(AddCategoryImageState())
    val addCategoryImage = _addCategoryImage.asStateFlow()

    private val _getAllCategories = MutableStateFlow(GetAllCategoriesState())
    val getAllCategories = _getAllCategories.asStateFlow()

    private val _addProduct = MutableStateFlow(AddProductState())
    val addProduct = _addProduct.asStateFlow()

    private val _addProductImage = MutableStateFlow(AddProductImageState())
    val addProductImage = _addProductImage.asStateFlow()

    fun addCategory(category: CategoryModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addCategoryUseCase.addCategoryUseCase(category = category).collect { response ->
                when (response) {
                    is ResultState.Error -> _addCategory.value = AddCategoryState(isLoading = false, error = response.error)
                    ResultState.Loading -> _addCategory.value = AddCategoryState(isLoading = true)
                    is ResultState.Success -> _addCategory.value = AddCategoryState(isLoading = false, data = response.data)
                }
            }
        }
    }

    fun clearAddCategoryState() {
        _addCategory.value = AddCategoryState()
    }

    fun addCategoryImage(imageUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            addCategoryImageUseCase.addCategoryImageUseCase(imageUri).collect { response ->
                when (response) {
                    is ResultState.Error -> _addCategoryImage.value = AddCategoryImageState(isLoading = false, error = response.error)
                    ResultState.Loading -> _addCategoryImage.value = AddCategoryImageState(isLoading = true)
                    is ResultState.Success -> _addCategoryImage.value = AddCategoryImageState(isLoading = false, data = response.data)
                }
            }
        }
    }

    fun clearAddCategoryImageState() {
        _addCategoryImage.value = AddCategoryImageState()
    }

    fun getAllCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllCategoriesUseCase.getAllCategoriesUseCase().collect { response ->
                when (response) {
                    is ResultState.Error -> _getAllCategories.value = GetAllCategoriesState(isLoading = false, error = response.error)
                    ResultState.Loading -> _getAllCategories.value = GetAllCategoriesState(isLoading = true)
                    is ResultState.Success -> {
                        _getAllCategories.value =
                            GetAllCategoriesState(isLoading = false, data = response.data)
                        Log.d("TAG", "getAllCategories: ${response.data}")
                    }
                }
            }
        }
    }

    fun clearGetAllCategoriesState() {
        _getAllCategories.value = GetAllCategoriesState(isLoading = false, error = null)
    }

    fun addProduct(product: ProductModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addProductUseCase.addProductUseCase(product = product).collect { response ->
                when (response) {
                    is ResultState.Error -> _addProduct.value = AddProductState(isLoading = false, error = response.error)
                    ResultState.Loading -> _addProduct.value = AddProductState(isLoading = true)
                    is ResultState.Success -> _addProduct.value = AddProductState(isLoading = false, data = response.data)
                }
            }
        }
    }

    fun clearAddProductState() {
        _addProduct.value = AddProductState()
    }

    fun addProductImage(imageUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            addProductImageUseCase.addProductImageUseCase(imageUri = imageUri).collect { response ->
                when (response) {
                    is ResultState.Error -> _addProductImage.value = AddProductImageState(isLoading = false, error = response.error)
                    ResultState.Loading -> _addProductImage.value = AddProductImageState(isLoading = true)
                    is ResultState.Success -> _addProductImage.value = AddProductImageState(isLoading = false, data = response.data)
                }
            }
        }
    }

    fun clearAddProductImageState() {
        _addProductImage.value = AddProductImageState()
    }
}

data class AddCategoryState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: String? = null
)

data class GetAllCategoriesState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<CategoryModel>? = null
)

data class AddProductState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: String? = null
)

data class AddProductImageState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: String? = null
)

data class AddCategoryImageState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: String? = null
)