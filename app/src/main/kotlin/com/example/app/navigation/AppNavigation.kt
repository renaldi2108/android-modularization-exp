package com.example.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.core.shared.designsystem.component.AppButton
import com.example.core.shared.designsystem.theme.AppTheme
import com.example.core.utils.navigation.AppDestinations
import com.example.core.utils.navigation.FeatureNavGraph
import com.example.core.utils.navigation.registerAll
import com.example.feature.auth.presentation.navigation.AuthRoutes

@Composable
fun AppNavigation(
    navGraphs: Set<FeatureNavGraph>,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AuthRoutes.Login
    ) {
        registerAll(navGraphs, navController)

        composable(AppDestinations.Dashboard) {
            HomeMenu(
                onProducts = { navController.navigate(AppDestinations.Products) },
                onUsers = { navController.navigate(AppDestinations.Users) },
                onPosts = { navController.navigate(AppDestinations.Posts) },
                onTodos = { navController.navigate(AppDestinations.Todos) },
                onQuotes = { navController.navigate(AppDestinations.Quotes) },
                onCarts = { navController.navigate(AppDestinations.Carts) },
                onRecipes = { navController.navigate(AppDestinations.Recipes) },
                onComments = { navController.navigate(AppDestinations.Comments) },
            )
        }
    }
}

@Composable
private fun HomeMenu(
    onProducts: () -> Unit,
    onUsers: () -> Unit,
    onPosts: () -> Unit,
    onTodos: () -> Unit,
    onQuotes: () -> Unit,
    onCarts: () -> Unit,
    onRecipes: () -> Unit,
    onComments: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(AppTheme.dimens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.spaceSm),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("AndroidBase", style = AppTheme.typography.headlineMedium)
        AppButton(text = "Products", onClick = onProducts)
        AppButton(text = "Users", onClick = onUsers)
        AppButton(text = "Posts", onClick = onPosts)
        AppButton(text = "Todos", onClick = onTodos)
        AppButton(text = "Quotes", onClick = onQuotes)
        AppButton(text = "Carts", onClick = onCarts)
        AppButton(text = "Recipes", onClick = onRecipes)
        AppButton(text = "Comments", onClick = onComments)
    }
}
