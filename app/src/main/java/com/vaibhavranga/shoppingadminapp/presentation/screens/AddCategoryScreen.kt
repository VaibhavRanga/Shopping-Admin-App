package com.vaibhavranga.shoppingadminapp.presentation.screens

import android.Manifest
import android.net.Uri
import android.os.Build
import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import com.vaibhavranga.shoppingadminapp.domain.models.CategoryModel
import com.vaibhavranga.shoppingadminapp.presentation.common.ImageAccessPermissionRequiredDialog
import com.vaibhavranga.shoppingadminapp.presentation.common.showToast
import com.vaibhavranga.shoppingadminapp.presentation.viewModel.MyViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddCategoryScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel()
) {
    val addCategory by viewModel.addCategory.collectAsStateWithLifecycle()
    val addCategoryImage by viewModel.addCategoryImage.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val categoryName = remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageUrl by remember { mutableStateOf("") }
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

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = categoryName.value,
                onValueChange = { categoryName.value = it },
                label = {
                    Text(text = "Category Name")
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
                                viewModel.addCategoryImage(
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
                    val category = CategoryModel(
                        categoryName = categoryName.value,
                        categoryImageUrl = imageUrl
                    )
                    viewModel.addCategory(category = category)
                }
            ) {
                Text(text = "Add Category")
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
            addCategoryImage.isLoading -> CircularProgressIndicator()
            addCategoryImage.error != null -> {
                showToast(
                    context = context,
                    message = addCategoryImage.error.toString()
                )
                viewModel.clearAddCategoryImageState()
            }

            addCategoryImage.data != null -> {
                imageUrl = addCategoryImage.data.toString()
                showToast(
                    context = context,
                    message = "Image uploaded successfully"
                )
                viewModel.clearAddCategoryImageState()
            }
        }
        when {
            addCategory.isLoading -> CircularProgressIndicator()
            addCategory.error != null -> {
                showToast(context = context, message = addCategory.error.toString())
                viewModel.clearAddCategoryState()
            }

            addCategory.data != null -> {
                showToast(context = context, message = addCategory.data.toString())
                viewModel.clearAddCategoryState()
            }
        }
    }
}