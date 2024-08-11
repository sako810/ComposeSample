package net.hiraok.compose_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import net.hiraok.compose_sample.ui.theme.ComposeSampleTheme

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val navController = rememberNavController()
            val selected by remember { mutableStateOf(false) }
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            ComposeSampleTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(modifier = Modifier.fillMaxWidth(), title = {
                            Text(text = "Compose Sample")
                        })
                    },
                    bottomBar = {
                        NavigationBar(modifier = Modifier.fillMaxWidth()) {
                            NavigationBarItem(
                                selected = currentDestination?.hierarchy?.any { it.route == "find" } == true,
                                onClick = { navController.navigate("find") },
                                icon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) })
                            NavigationBarItem(
                                selected = currentDestination?.hierarchy?.any { it.route == "draw" } == true,
                                onClick = { navController.navigate("draw") },
                                icon = { Icon(imageVector = Icons.Default.Create, contentDescription = null) })
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "find",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                    }
                }
            }
        }
    }
}