package br.edu.kb.expenseTracker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.edu.kb.expenseTracker.feature.add_expense.AddExpenseScreen
import br.edu.kb.expenseTracker.feature.home.HomeScreen
import br.edu.kb.expenseTracker.feature.stats.StatsScreen
import br.edu.kb.expenseTracker.ui.theme.Zinc
import br.edu.kb.expenseTracker.viewmodel.ExpenseViewModel

object Routes {
    const val HOME = "/home"
    const val ADD_EXPENSE = "/add"
    const val STATS = "/stats"
}

@Composable
fun NavHostScreen(viewModel: ExpenseViewModel) {
    val navController = rememberNavController()
    var bottomBarVisibility by remember {
        mutableStateOf(true)
    }

    Scaffold(bottomBar = {
        AnimatedVisibility(visible = bottomBarVisibility) {
            NavigationBottomBar(
                navController = navController,
                items = listOf(
                    NavItem(route = Routes.HOME, icon = R.drawable.ic_home),
                    NavItem(route = Routes.STATS, icon = R.drawable.ic_stats)
                )
            )
        }
    }) {
        NavHost(
            navController = navController,
            startDestination = "/home",
            modifier = Modifier.padding(it)
        ) {
            composable(Routes.HOME) {
                bottomBarVisibility = true
                HomeScreen(navController, viewModel)
            }

            composable(Routes.ADD_EXPENSE) {
                bottomBarVisibility = false
                AddExpenseScreen(navController, viewModel)
            }

            composable(Routes.STATS) {
                bottomBarVisibility = true
                StatsScreen(navController, viewModel)
            }
        }
    }
}

data class NavItem(
    val route: String,
    val icon: Int
)

@Composable
fun NavigationBottomBar(navController: NavController, items: List<NavItem>) {
    // Bottom Navigation Bar
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    BottomAppBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(painter = painterResource(id = item.icon), contentDescription = null)
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = Zinc,
                    selectedIconColor = Zinc,
                    unselectedTextColor = Color.Gray,
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}