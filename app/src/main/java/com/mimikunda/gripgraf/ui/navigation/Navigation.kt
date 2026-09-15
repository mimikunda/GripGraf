package com.mimikunda.gripgraf.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mimikunda.gripgraf.ui.screens.GraphScreen
import com.mimikunda.gripgraf.ui.screens.HomeScreen
import com.mimikunda.gripgraf.ui.screens.TableScreen
import kotlinx.serialization.Serializable

@Serializable object HomeRoute
@Serializable object TableRoute
@Serializable data class GraphRoute(val graphId: Int)

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val routes = listOf(
        Triple(TableRoute, Icons.AutoMirrored.Filled.List, "Table"),
        Triple(HomeRoute, Icons.Default.Home, "Home"),
        Triple(GraphRoute(0), Icons.Default.AutoGraph, "Graph")
    )

    fun getRouteIndex(destination: androidx.navigation.NavDestination?): Int {
        return when {
            destination?.hasRoute<TableRoute>() == true -> 0
            destination?.hasRoute<HomeRoute>() == true -> 1
            destination?.hasRoute<GraphRoute>() == true -> 2
            else -> 1
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .padding(horizontal = 26.dp, vertical = 12.dp)
                    .clip(CircleShape),
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.9f)
            ) {
                routes.forEach { (route, icon, label) ->
                    val isSelected = currentDestination?.hasRoute(route::class) == true
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (!isSelected) {
                                navController.navigate(route) {
                                    popUpTo(HomeRoute) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            enterTransition = {
                val initialIndex = getRouteIndex(initialState.destination)
                val targetIndex = getRouteIndex(targetState.destination)
                slideIntoContainer(
                    if (targetIndex > initialIndex) AnimatedContentTransitionScope.SlideDirection.Start
                    else AnimatedContentTransitionScope.SlideDirection.End
                )
            },
            exitTransition = {
                val initialIndex = getRouteIndex(initialState.destination)
                val targetIndex = getRouteIndex(targetState.destination)
                slideOutOfContainer(
                    if (targetIndex > initialIndex) AnimatedContentTransitionScope.SlideDirection.Start
                    else AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            composable<HomeRoute> {
                HomeScreen(
                    onNavigateToTable = { navController.navigate(TableRoute) },
                    onNavigateToGraph = { graphId -> navController.navigate(GraphRoute(graphId = graphId)) }
                )
            }

            composable<TableRoute> {
                TableScreen()
            }

            composable<GraphRoute> { backStackEntry ->
                val args: GraphRoute = backStackEntry.toRoute()
                GraphScreen(
                    graphId = args.graphId,
                )
            }
        }
    }
}