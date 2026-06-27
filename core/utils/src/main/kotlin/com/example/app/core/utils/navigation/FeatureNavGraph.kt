package com.example.app.core.utils.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

fun interface FeatureNavGraph {
    fun register(builder: NavGraphBuilder, navController: NavHostController)
}

fun NavGraphBuilder.registerAll(
    graphs: Set<FeatureNavGraph>,
    navController: NavHostController
) {
    graphs.forEach { it.register(this, navController) }
}
