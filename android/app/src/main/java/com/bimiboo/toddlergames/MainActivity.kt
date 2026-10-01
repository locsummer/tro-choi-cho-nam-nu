package com.bimiboo.toddlergames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bimiboo.toddlergames.core.model.GameCategory
import com.bimiboo.toddlergames.features.games.*
import com.bimiboo.toddlergames.features.home.HomeScreen
import com.bimiboo.toddlergames.ui.theme.ToddlerGamesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToddlerGamesTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ToddlerGamesNavigation()
                }
            }
        }
    }
}

@Composable
fun ToddlerGamesNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToGame = { category ->
                    navController.navigate(category.route)
                }
            )
        }

        composable(GameCategory.SHAPES.route) {
            ShapeSortingScreen(onBack = { navController.popBackStack() })
        }

        composable(GameCategory.COLORS.route) {
            ColorMatchingScreen(onBack = { navController.popBackStack() })
        }

        composable(GameCategory.SIZES.route) {
            SizeOrderingScreen(onBack = { navController.popBackStack() })
        }

        composable(GameCategory.PUZZLE.route) {
            AnimalPuzzleScreen(onBack = { navController.popBackStack() })
        }

        composable(GameCategory.COUNTING.route) {
            CountingScreen(onBack = { navController.popBackStack() })
        }

        composable(GameCategory.FEED_ANIMAL.route) {
            FeedAnimalScreen(onBack = { navController.popBackStack() })
        }

        composable(GameCategory.VEHICLES.route) {
            VehicleSortingScreen(onBack = { navController.popBackStack() })
        }

        composable(GameCategory.ODD_ONE_OUT.route) {
            OddOneOutScreen(onBack = { navController.popBackStack() })
        }
    }
}
