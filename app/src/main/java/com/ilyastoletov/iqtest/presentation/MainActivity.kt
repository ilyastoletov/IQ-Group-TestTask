package com.ilyastoletov.iqtest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ilyastoletov.iqtest.presentation.favourites.screen.FavouritesScreen
import com.ilyastoletov.iqtest.presentation.theme.IQGroupTestTheme
import com.ilyastoletov.iqtest.presentation.vacancies.screen.VacanciesScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IQGroupTestTheme {

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "vacancies"
                ) {

                    composable("vacancies") {
                        VacanciesScreen(
                            viewModel = hiltViewModel(),
                            openFavouritesScreen = { navController.navigate("favourites") }
                        )
                    }

                    composable("favourites") {
                        FavouritesScreen(
                            viewModel = hiltViewModel(),
                            onBack = { navController.popBackStack() }
                        )
                    }

                }

            }
        }
    }

}