package net.hiraok.composesample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import net.hiraok.composesample.ui.theme.ComposeSampleTheme
import net.hiraok.movie.MovieScreen
import net.hiraok.museum.ModelScreen

enum class Screens(val title: String) {
    A("HOME"),
    B("NEXT")
}

@UnstableApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSampleTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigation {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            Screens.values().forEach { screen ->
                                BottomNavigationItem(
                                    icon = {
                                        Icon(
                                            Icons.Filled.Favorite,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text(screen.title) },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.title } == true,
                                    onClick = {
                                        navController.navigate(screen.title) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "HOME",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("HOME") {
                            MovieScreen()
                        }
                        composable("NEXT") {
                            Next()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Next() {
    ModelScreen()
}