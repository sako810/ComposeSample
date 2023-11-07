package net.hiraok.composesample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import net.hiraok.composesample.ui.theme.ComposeSampleTheme
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
            val navController = rememberNavController()
            val dummyAppState = rememberDummyAppState(navController)
            ComposeSampleTheme {
                Scaffold(
                    topBar = {
                        if (dummyAppState.shouldShowAppBar) {
                            TopAppBar(title = { Text(dummyAppState.title) })
                        }
                    },
                    bottomBar = {
                        BottomNavigation {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            Screens.entries.forEach { screen ->
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
                            Home()
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
fun Home() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("Home"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("init diff diff3")
    }
}

@Composable
fun Next() {
    ModelScreen()
}

data class DummyAppState(
    val title: String,
    val subTitle: String,
    val scope: CoroutineScope,
    val currentDestination: NavDestination? = null,
    val shouldShowAppBar: Boolean = false,
    // メニューアイコン、FABとかも追加する
) {
    private val _event = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event

    fun changeTitle(value: String) {
        scope.launch {
            _event.emit(Event.ChangeTitle(value))
        }
    }

    fun hideTopBar() {
        scope.launch {
            _event.emit(Event.HideTopBar)
        }
    }
}

sealed class Event {
    data class ChangeTitle(val value: String) : Event()
    data object HideTopBar : Event()
}

@Composable
fun rememberDummyAppState(navController: NavHostController?): DummyAppState {
    val scope = rememberCoroutineScope()
    var dummyAppState by remember { mutableStateOf(DummyAppState("", "", scope)) }
    val currentDestination = navController
        ?.currentBackStackEntryAsState()?.value?.destination
    // 基本はMomijiDestinationに指定した文字列をタイトルとする
    val defaultTitle =
        currentDestination?.route?.getDestination()?.titleTextId?.let { stringResource(id = it) }
            ?: ""

    LaunchedEffect(navController?.currentDestination) {
        dummyAppState = dummyAppState.copy(
            title = defaultTitle,
            currentDestination = currentDestination,
            shouldShowAppBar = true
        )
    }

    LaunchedEffect(Unit) {
        dummyAppState.event.collectLatest {
            dummyAppState = when (it) {
                is Event.ChangeTitle -> {
                    dummyAppState.copy(title = it.value)
                }

                Event.HideTopBar -> {
                    dummyAppState.copy(shouldShowAppBar = false)
                }
            }
        }
    }

    return dummyAppState
}

val LocalAppStateProvider: ProvidableCompositionLocal<DummyAppState> =
    compositionLocalOf { DummyAppState("", "", GlobalScope) }

@Composable
fun SampleScreen() {
    // デフォルトの表示とかえるときだけeventをemitする関数を呼び出す
    val appState = LocalAppStateProvider.current
    LaunchedEffect(Unit) {
        appState.changeTitle("change title")
    }

    Text("sample")
}