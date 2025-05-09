package com.example.canvas.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navigation(modifier: Modifier = Modifier){
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        NavHost(
            navController,
            startDestination = NavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavItem.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(
                route = "art?artId={artId}",
                arguments = listOf(
                    navArgument("artId") {
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val artId = backStackEntry.arguments?.getString("artId")?.toIntOrNull()
                Log.d("Navigation", "artId: $artId")
                DetailScreen(
                    navController = navController,
                    artId = artId
                )
            }
        }
    }
}
sealed class NavItem(val route: String) {
    object Home : NavItem("home")
    object Art : NavItem("art") {
        fun withNoteId(artId: Int?) : String {
            Log.d("NavItem", "withNoteId called with artId: $artId")
            if (artId == null) return route
            Log.d("NavItem", "Returning route with artId: $artId")
            return "art?artId=$artId"
        }
    }
}