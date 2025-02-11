package com.vaibhavranga.shoppingadminapp.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    object HomeScreen : Routes()

    @Serializable
    object AddCategoryScreen : Routes()

    @Serializable
    object AddProductScreen : Routes()
}