package com.vaibhavranga.shoppingadminapp.presentation.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vaibhavranga.shoppingadminapp.presentation.navigation.App
import com.vaibhavranga.shoppingadminapp.ui.theme.ShoppingAdminAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingAdminAppTheme {
                App()
            }
        }
    }
}
