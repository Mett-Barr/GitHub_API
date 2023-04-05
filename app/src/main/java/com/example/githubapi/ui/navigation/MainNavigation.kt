package com.example.githubapi.ui.navigation

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.githubapi.MainViewModel
import com.example.githubapi.R
import com.example.githubapi.ui.page.BookmarkPage
import com.example.githubapi.ui.page.RepositoryPage
import com.example.githubapi.ui.page.SearchPage
import com.example.githubapi.ui.page.SettingsPage

const val REPOSITORY = "Repository"

sealed class MainRoute(val route: String) {
    object Search : MainRoute("Search")
    object Settings : MainRoute("Settings")
    object BookMarks : MainRoute("Bookmarks")
    class Repository(val url: String) : MainRoute(REPOSITORY)

//    companion object {
//        val repository = Repository("")
//    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation(
//    viewModel: MainViewModel
) {

//    val navController = rememberNavController()


    // 使用 hiltViewModel() 函數獲取 MainViewModel 的實例
    val viewModel: MainViewModel = hiltViewModel()

    val route by viewModel.route.collectAsState()

    var backRoute by remember {
        mutableStateOf<MainRoute>(MainRoute.Search)
    }

    var url by remember {
        mutableStateOf("")
    }

//    LaunchedEffect(route) {
//
//        val currentRoute = navController.currentBackStackEntry?.id ?: ""
//
//        if (currentRoute == MainRoute.repository.route) {
//            navController.popBackStack()
//        } else if (route is MainRoute.Repository) {
//            val it = route
//            if (it is MainRoute.Repository) {
//                url = it.url
//            }
//            navController.navigate(route.route)
//        } else {
//            // 三個主頁面之間切換，不保留導航歷史
//            navController.navigate(route.route) {
//                // 将启动模式设置为 singleTop，以便在导航到相同目标时重用已经存在的实例
//                launchSingleTop = true
//            }
//        }
//
//        Log.d("!!!", "MainNavigation: ${route.route}")
////        navController.navigate(route.route)
//    }

//    LaunchedEffect(route) {
//        val it = route
//        if (it is MainRoute.Repository) {
//            url = it.url
//            navController.navigate(route.route) {
//                // 将堆栈中的所有页面弹出，直到您到达指定的目标
//                popUpTo(route.route) { inclusive = true }
//            }
//        } else {
//            // 三個主頁面之間切換，不保留導航歷史
//            navController.navigate(route.route) {
//                // 将启动模式设置为 singleTop，以便在导航到相同目标时重用已经存在的实例
//                launchSingleTop = true
//            }
//        }
//        Log.d("!!!", "MainNavigation: ${route.route}")
//    }

    Scaffold(
//        modifier = Modifier.statusBarsPadding(),
        bottomBar = {
//            BottomAppBar {
//                Icon(painter = painterResource(id = R.drawable.search_fill0_wght400_grad0_opsz48), contentDescription = "search page")
//                Icon(painter = painterResource(id = R.drawable.bookmark_black_24dp), contentDescription = "bookmarks")
//                Icon(painter = painterResource(id = R.drawable.settings_black_24dp), contentDescription = "settings", modifier = Modifier.clickable {  })
//            }

            Surface(
                modifier = Modifier
                    .wrapContentSize()
                    .animateContentSize { _, _ -> },
                color = MaterialTheme.colorScheme.background
            ) {
                if (route !is MainRoute.Repository) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                    ) {
                        NavigationBarItem(selected = route == MainRoute.Search,
                            onClick = { viewModel.navigate(MainRoute.Search) },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.search_fill0_wght400_grad0_opsz48),
                                    contentDescription = "search page",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = { Text(text = MainRoute.Search.route) }
                        )
                        NavigationBarItem(selected = route == MainRoute.BookMarks,
                            onClick = { viewModel.navigate(MainRoute.BookMarks) },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.bookmark_black_24dp),
                                    contentDescription = "bookmarks",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = { Text(text = MainRoute.BookMarks.route) }
                        )
                        NavigationBarItem(selected = route == MainRoute.Settings,
                            onClick = { viewModel.navigate(MainRoute.Settings) },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.settings_black_24dp),
                                    contentDescription = "settings",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = { Text(text = MainRoute.Settings.route) }
                        )
                    }

                }
//                AnimatedVisibility(visible = route !is MainRoute.Repository,
//                    enter = slideInVertically { -it },
//                    exit = slideOutVertically { it }
////                        fadeIn() with fadeOut()
//                ) {
//                    NavigationBar(
//                        containerColor = MaterialTheme.colorScheme.background,
//                        modifier = Modifier
//                            .background(MaterialTheme.colorScheme.background)
//                            .fillMaxWidth()
//                    ) {
//                        NavigationBarItem(selected = route == MainRoute.Search,
//                            onClick = { viewModel.navigate(MainRoute.Search) },
//                            icon = {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.search_fill0_wght400_grad0_opsz48),
//                                    contentDescription = "search page",
//                                    modifier = Modifier.size(24.dp)
//                                )
//                            },
//                            label = { Text(text = MainRoute.Search.route) }
//                        )
//                        NavigationBarItem(selected = route == MainRoute.BookMarks,
//                            onClick = { viewModel.navigate(MainRoute.BookMarks) },
//                            icon = {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.bookmark_black_24dp),
//                                    contentDescription = "bookmarks",
//                                    modifier = Modifier.size(24.dp)
//                                )
//                            },
//                            label = { Text(text = MainRoute.BookMarks.route) }
//                        )
//                        NavigationBarItem(selected = route == MainRoute.Settings,
//                            onClick = { viewModel.navigate(MainRoute.Settings) },
//                            icon = {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.settings_black_24dp),
//                                    contentDescription = "settings",
//                                    modifier = Modifier.size(24.dp)
//                                )
//                            },
//                            label = { Text(text = MainRoute.Settings.route) }
//                        )
//                    }
//                }
                
//                AnimatedContent(targetState = route !is MainRoute.Repository,
//                    transitionSpec = {
//                        fadeIn() with fadeOut()
//                    }) {
//                    if (it) {
//                        NavigationBar(
//                            containerColor = MaterialTheme.colorScheme.background,
//                            modifier = Modifier
//                                .background(MaterialTheme.colorScheme.background)
//                                .fillMaxWidth()
//                        ) {
//                            NavigationBarItem(selected = route == MainRoute.Search,
//                                onClick = { viewModel.navigate(MainRoute.Search) },
//                                icon = {
//                                    Icon(
//                                        painter = painterResource(id = R.drawable.search_fill0_wght400_grad0_opsz48),
//                                        contentDescription = "search page",
//                                        modifier = Modifier.size(24.dp)
//                                    )
//                                },
//                                label = { Text(text = MainRoute.Search.route) }
//                            )
//                            NavigationBarItem(selected = route == MainRoute.BookMarks,
//                                onClick = { viewModel.navigate(MainRoute.BookMarks) },
//                                icon = {
//                                    Icon(
//                                        painter = painterResource(id = R.drawable.bookmark_black_24dp),
//                                        contentDescription = "bookmarks",
//                                        modifier = Modifier.size(24.dp)
//                                    )
//                                },
//                                label = { Text(text = MainRoute.BookMarks.route) }
//                            )
//                            NavigationBarItem(selected = route == MainRoute.Settings,
//                                onClick = { viewModel.navigate(MainRoute.Settings) },
//                                icon = {
//                                    Icon(
//                                        painter = painterResource(id = R.drawable.settings_black_24dp),
//                                        contentDescription = "settings",
//                                        modifier = Modifier.size(24.dp)
//                                    )
//                                },
//                                label = { Text(text = MainRoute.Settings.route) }
//                            )
//                        }
//                    } else {
//                        BottomAppBar(
//                            modifier = Modifier
//                                .background(MaterialTheme.colorScheme.background)
//                                .fillMaxWidth()
//                                .wrapContentHeight(),
//                            containerColor = MaterialTheme.colorScheme.background
//                        ) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.cancel_black_24dp),
//                                contentDescription = ""
//                            )
//                        }
//                    }
//                }
            }
        }
    ) { padding ->


        AnimatedContent(targetState = route, modifier = Modifier.padding(padding)) {
            when (it) {
                MainRoute.Search -> {
                    backRoute = it
                    SearchPage()
                }
                MainRoute.BookMarks -> {
                    backRoute = it
                    BookmarkPage()
                }
                MainRoute.Settings -> SettingsPage()
                is MainRoute.Repository -> RepositoryPage(url = it.url) {
                    viewModel.route.value = backRoute
                }

            }
        }

        // Hilt + ViewModel + Navigation + Compose 會有bug
        // 哭啊！
        // https://issuetracker.google.com/u/1/issues/265838050
//        NavHost(
//            navController = navController,
//            startDestination = MainRoute.Search.route,
//            modifier = Modifier.padding(it)
//        ) {
//            composable(MainRoute.Search.route) {
//                SearchPage(viewModel)
//            }
//            composable(MainRoute.Settings.route) {
//
//            }
//            composable(MainRoute.BookMarks.route) {
//                BookmarkPage()
//                Spacer(modifier = Modifier.size(50.dp).background(Color.White))
//            }
//            composable(REPOSITORY) {
//                RepositoryPage(url = url) {
////                    navController.popBackStack()
//                    viewModel.route.value = when (navController.currentBackStackEntry?.id) {
//                        MainRoute.BookMarks.route -> MainRoute.BookMarks
//                        MainRoute.Settings.route -> MainRoute.Settings
//                        else -> MainRoute.Search
//                    }
//                }
//            }
//        }
    }
}