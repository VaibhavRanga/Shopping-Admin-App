package com.vaibhavranga.shoppingadminapp.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vaibhavranga.shoppingadminapp.presentation.screens.AddCategoryScreen
import com.vaibhavranga.shoppingadminapp.presentation.screens.AddProductScreen
import com.vaibhavranga.shoppingadminapp.presentation.screens.HomeScreen

@Composable
fun App() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavigationItems.forEachIndexed { index, bottomNavigationItem ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == bottomNavigationItem.route::class.qualifiedName } == true
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(bottomNavigationItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = bottomNavigationItem.icon,
                                contentDescription = bottomNavigationItem.title
                            )
                        },
                        label = {
                            Text(text = bottomNavigationItem.title)
                        }
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HomeScreen,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable<Routes.HomeScreen> {
                HomeScreen()
            }
            composable<Routes.AddCategoryScreen> {
                AddCategoryScreen()
            }
            composable<Routes.AddProductScreen> {
                AddProductScreen()
            }
        }
    }
}

val bottomNavigationItems = listOf(
    BottomNavigationItem(
        title = "Home",
        icon = Icons.Default.Home,
        route = Routes.HomeScreen
    ),
    BottomNavigationItem(
        title = "Add category",
        icon = Icons.Default.Add,
        route = Routes.AddCategoryScreen
    ),
    BottomNavigationItem(
        title = "Add product",
        icon = Icons.Default.AddCircle,
        route = Routes.AddProductScreen
    )
)

data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: Routes
)