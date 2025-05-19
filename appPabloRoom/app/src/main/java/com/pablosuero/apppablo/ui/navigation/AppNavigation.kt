package com.pablosuero.apppablo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.pablosuero.apppablo.App
import com.pablosuero.apppablo.data.db.MealDao
import com.pablosuero.apppablo.data.db.MealDatabase
import com.pablosuero.apppablo.ui.screen.ListScreen.ListScreen
import com.pablosuero.apppablo.ui.screen.ListScreen.ListViewModel
import com.pablosuero.apppablo.ui.screen.ListScreen.ListViewModel.Factory
import com.pablosuero.apppablo.ui.screen.DetailScreen.DetailScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current
    // Obtener MealDao desde la clase Application
    val mealDao: MealDao = (context.applicationContext as App).db.mealDao()
    // Crear el ViewModel con la Factory que inyecta MealDao
    val listViewModel: ListViewModel = viewModel(factory = Factory(mealDao))

    NavHost(navController = navController, startDestination = List) {
        composable<List> {
            ListScreen(
                listViewModel = listViewModel,
                navigateToDetail = { mealId ->
                    navController.navigate(Detail(mealId))
                }
            )
        }

        composable<Detail> { backStackEntry ->
            val detail = backStackEntry.toRoute<Detail>()
            val mealId = detail.id
            DetailScreen(
                mealId = mealId,
                onBack = { navController.popBackStack() },
                listViewModel = listViewModel
            )
        }
    }
}
