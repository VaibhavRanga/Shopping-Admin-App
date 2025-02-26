package com.vaibhavranga.shoppingadminapp.presentation.screens

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.vaibhavranga.shoppingadminapp.domain.models.ProductModel
import com.vaibhavranga.shoppingadminapp.presentation.common.ImageAccessPermissionRequiredDialog
import com.vaibhavranga.shoppingadminapp.presentation.common.showToast
import com.vaibhavranga.shoppingadminapp.presentation.viewModel.MyViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddProductScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel()
) {
    val getAllCategories = viewModel.getAllCategories.collectAsStateWithLifecycle()
    val addProduct = viewModel.addProduct.collectAsStateWithLifecycle()
    val addProductImage = viewModel.addProductImage.collectAsStateWithLifecycle()
    var selectedCategory by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productFinalPrice by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var availableUnits by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageUrl by remember { mutableStateOf("") }
    var isDropdownMenuOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var showPermissionDialog by remember { mutableStateOf(false) }
    val permissionState = rememberPermissionState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE
    )

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        it?.let { uri ->
            imageUri = uri
        }
    }

    LaunchedEffect(key1 = Unit) {
        permissionState.launchPermissionRequest()
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllCategories()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(text = "Add a Product")
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {
                    selectedCategory = it
                },
                label = {
                    Text(text = "Select Category")
                },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            isDropdownMenuOpen = !isDropdownMenuOpen
                        }
                    ) {
                        if (isDropdownMenuOpen) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = "Close menu"
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Open menu"
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = isDropdownMenuOpen,
                        onDismissRequest = { isDropdownMenuOpen = false },
                        scrollState = rememberScrollState(),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                    ) {
                        when {
                            getAllCategories.value.data != null -> {
                                getAllCategories.value.data!!.forEach { category ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = category.categoryName)
                                        },
                                        onClick = {
                                            selectedCategory = category.categoryName
                                            isDropdownMenuOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            )
            OutlinedTextField(
                value = productName,
                onValueChange = {
                    productName = it
                },
                label = {
                    Text(text = "Product Name")
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = productPrice,
                onValueChange = {
                    val temp = if (it.isEmpty()) {
                        it.trim()
                    } else {
                        when (it.toDoubleOrNull()) {
                            null -> productPrice
                            else -> it.trim()
                        }
                    }
                    productPrice = ("%.2f").format(temp.toDoubleOrNull() ?: 0.0)
                },
                label = {
                    Text(text = "Product Price")
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = productFinalPrice,
                onValueChange = {
                    val temp = if (it.isEmpty()) {
                        it.trim()
                    } else {
                        when (it.toDoubleOrNull()) {
                            null -> productFinalPrice
                            else -> it.trim()
                        }
                    }
                    productFinalPrice = ("%.2f").format(temp.toDoubleOrNull() ?: 0.0)
                },
                label = {
                    Text(text = "Product Final Price")
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = productDescription,
                onValueChange = {
                    productDescription = it
                },
                label = {
                    Text(text = "Product Description")
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = availableUnits,
                onValueChange = {
                    val temp = if (it.isEmpty()) {
                        it.trim()
                    } else {
                        when (it.toIntOrNull()) {
                            null -> availableUnits
                            else -> it.trim()
                        }
                    }
                    availableUnits = (temp.toIntOrNull() ?: 0).toString()
                },
                label = {
                    Text(text = "Product Available Units")
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Card(
                modifier = Modifier
                    .height(150.dp)
                    .width(150.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null
                    )
                    IconButton(
                        onClick = {
                            if (permissionState.status.isGranted) {
                                imagePickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            } else if (permissionState.status.shouldShowRationale) {
                                permissionState.launchPermissionRequest()
                            } else {
                                showPermissionDialog = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add an image"
                        )
                    }
                    TextButton(
                        onClick = {
                            imageUri?.let {
                                viewModel.addProductImage(
                                    imageUri = it
                                )
                            }
                        },
                        modifier = Modifier
                            .align(alignment = Alignment.BottomCenter)
                    ) {
                        Text(text = "Upload image")
                    }
                }
            }
            Button(
                onClick = {
                    if (productName.isNotBlank()
                        && productPrice.isNotBlank()
                        && productFinalPrice.isNotBlank()
                        && productDescription.isNotBlank()
                        && selectedCategory.isNotBlank()
                        && availableUnits.isNotBlank()
                        && imageUrl.isNotBlank()
                    ) {
                        val product = ProductModel(
                            name = productName,
                            price = productPrice,
                            finalPrice = productFinalPrice,
                            description = productDescription,
                            imageUrl = imageUrl,
                            category = selectedCategory,
                            availableUnits = availableUnits.toIntOrNull() ?: 0
                        )
                        viewModel.addProduct(product)
                    } else {
                        showToast(context = context, message = "Please enter all the details")
                    }
                }
            ) {
                Text(text = "Save Product")
            }
        }
        if (showPermissionDialog) {
            ImageAccessPermissionRequiredDialog(
                permissionState = permissionState,
                onDismissRequest = {
                    showPermissionDialog = false
                }
            )
        }
        when {
            addProductImage.value.isLoading -> CircularProgressIndicator()
            addProductImage.value.error != null -> {
                showToast(context = context, message = addProductImage.value.error!!)
                viewModel.clearAddProductImageState()
            }
            addProductImage.value.data != null -> {
                imageUrl = addProductImage.value.data!!
                showToast(
                    context = context,
                    message = "Image uploaded successfully"
                )
                viewModel.clearAddProductImageState()
            }
        }
        when {
            getAllCategories.value.isLoading -> CircularProgressIndicator()
            getAllCategories.value.error != null -> {
                showToast(
                    context = context,
                    message = getAllCategories.value.error.toString()
                )
                viewModel.clearGetAllCategoriesState()
            }
        }
        when {
            addProduct.value.isLoading -> CircularProgressIndicator()
            addProduct.value.error != null -> {
                showToast(context = context, message = addProduct.value.error.toString())
                viewModel.clearAddProductState()
            }

            addProduct.value.data != null -> {
                showToast(context = context, message = addProduct.value.data.toString())
                viewModel.clearAddProductState()
            }
        }
    }
}
