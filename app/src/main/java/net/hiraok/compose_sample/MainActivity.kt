package net.hiraok.compose_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import net.hiraok.compose_sample.ui.theme.ComposeSampleTheme
import net.hiraok.feature.top.TopScreen

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
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "top",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("top") {
                            TopScreen()
                        }
                    }
                }
            }
        }
    }
}