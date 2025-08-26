package com.example.cinema

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cinema.data.remote.OnboardingPrefs
import kotlinx.coroutines.launch
import com.example.cinema.ui.components.*
import com.example.cinema.ui.components.NeonColors
import com.example.cinema.ui.components.NeonText
import com.example.cinema.ui.components.NeonIcons

class MainActivity : ComponentActivity() {
    private lateinit var appContainer: AppContainer
    private lateinit var onboardingPrefs: OnboardingPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appContainer = AppContainer(this)
        onboardingPrefs = OnboardingPrefs(this)

        setContent {
            SkillCinemaTheme {
                CinemaApp(
                    appContainer = appContainer,
                    onboardingPrefs = onboardingPrefs
                )
            }
        }
    }
}

@Composable
fun CinemaApp(
    appContainer: AppContainer,
    onboardingPrefs: OnboardingPrefs
) {
    MaterialTheme {
        val isCompleted by onboardingPrefs.isOnboardingCompleted.collectAsState(initial = null)
        val navController = rememberNavController()
        val coroutineScope = rememberCoroutineScope()

        if (isCompleted == null) {
            // Не знаем ещё состояние - показываем экран-заглушку
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (isCompleted == true) {
            androidx.compose.material3.Scaffold(
                bottomBar = {
                    BottomAppBar(
                        modifier = Modifier.height(64.dp),
                        containerColor = NeonColors.Surface
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            IconButton(
                                onClick = { navController.navigate("home") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        NeonIcons.Home, 
                                        contentDescription = "Home",
                                        tint = NeonColors.Primary
                                    )
                                    NeonText(
                                        text = "Главная",
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                            IconButton(
                                onClick = { navController.navigate("search") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        NeonIcons.Search, 
                                        contentDescription = "Search",
                                        tint = NeonColors.Secondary
                                    )
                                    NeonText(
                                        text = "Поиск",
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                            IconButton(
                                onClick = { navController.navigate("profile") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        NeonIcons.Person, 
                                        contentDescription = "Profile",
                                        tint = NeonColors.Tertiary
                                    )
                                    NeonText(
                                        text = "Профиль",
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            ) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(paddingValues)
                ) {
                    composable("home") {
                        val viewModel: HomeViewModel = viewModel(
                            factory = appContainer.provideHomeViewModelFactory()
                        )
                        HomeScreen(
                            viewModel = viewModel,
                            onFilmClick = { filmId ->
                                navController.navigate("film/$filmId")
                            },
                            onSectionClick = { title ->
                                navController.navigate("section/$title")
                            }
                        )
                    }
                    composable("search") {
                        val searchViewModel: SearchViewModel = viewModel(
                            factory = SearchViewModelFactory(api = appContainer.kinopoiskService)
                        )
                        SearchScreen(
                            onFilmClick = { filmId ->
                                navController.navigate("film/$filmId")
                            },
                            viewModel = searchViewModel
                        )
                    }
                    composable("profile") {
                        val profileViewModel: ProfileViewModel = viewModel(
                            factory = ProfileViewModelFactory(
                                filmDao = appContainer.appDatabase.filmDao(),
                                collectionsRepository = appContainer.provideCollectionsRepository(),
                                historyDao = appContainer.appDatabase.historyDao()
                            )
                        )
                        ProfileScreen(
                            viewModel = profileViewModel,
                            onOpenCollections = { navController.navigate("collections") },
                            onOpenHistory = { navController.navigate("history") }
                        )
                    }
                    composable("history") {
                        val vm: HistoryViewModel = viewModel(
                            factory = HistoryViewModelFactory(appContainer.appDatabase.historyDao())
                        )
                        HistoryScreen(viewModel = vm)
                    }
                    composable("collections") {
                        val vm: CollectionsViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                @Suppress("UNCHECKED_CAST")
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return CollectionsViewModel(appContainer.provideCollectionsRepository()) as T
                                }
                            }
                        )
                        CollectionsScreen(
                            viewModel = vm,
                            onCollectionClick = { id -> navController.navigate("collection/$id") },
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                    composable("collection/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: return@composable
                        val vm: CollectionDetailsViewModel = viewModel(
                            factory = CollectionDetailsViewModelFactory(
                                repo = appContainer.provideCollectionsRepository(),
                                collectionId = id
                            )
                        )
                        CollectionDetailsScreen(
                            viewModel = vm,
                            collectionId = id,
                            onFilmClick = { filmId -> navController.navigate("film/$filmId") },
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                    // Placeholder for section "All" screen route
                    composable("section/{title}") { backStackEntry ->
                        val title = backStackEntry.arguments?.getString("title") ?: ""
                        val scope = rememberCoroutineScope()
                        val flow = when (title) {
                            "Популярное" -> pagerForTop(appContainer.kinopoiskService, type = "TOP_POPULAR_ALL", scope)
                            "Топ-250" -> pagerForTop(appContainer.kinopoiskService, type = "TOP_250_MOVIES", scope)
                            "Сериалы" -> pagerForFilters(appContainer.kinopoiskService, scope = scope, type = "TV_SERIES", order = "RATING", ratingFrom = 7)
                            "Боевики США" -> pagerForFilters(appContainer.kinopoiskService, scope = scope, countryId = 1, genreId = 3)
                            else -> pagerForTop(appContainer.kinopoiskService, type = "TOP_POPULAR_ALL", scope)
                        }
                        AllItemsPagingScreen(
                            flow = flow,
                            onFilmClick = { filmId ->
                                navController.navigate("film/$filmId")
                            }
                        )
                    }
                    composable("film/{id}") { backStackEntry ->
                        val filmId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: return@composable
                        val vm: FilmDetailsViewModel = viewModel(
                            factory = FilmDetailsViewModelFactory(
                                api = appContainer.kinopoiskService,
                                collectionsRepository = appContainer.provideCollectionsRepository(),
                                filmId = filmId,
                                historyDao = appContainer.appDatabase.historyDao()
                            )
                        )
                        FilmDetailsScreen(
                            filmId = filmId,
                            viewModel = vm,
                            onBackClick = { navController.popBackStack() },
                            onFilmClick = { filmIdParam -> navController.navigate("film/$filmIdParam") },
                            onPersonClick = { personId -> navController.navigate("person/$personId") },
                            appContainer = appContainer
                        )
                    }
                    composable("person/{id}") { backStackEntry ->
                        val personId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: return@composable
                        val vm: ActorViewModel = viewModel(
                            factory = ActorViewModelFactory(api = appContainer.kinopoiskService, personId = personId)
                        )
                        ActorScreen(viewModel = vm)
                    }
                    composable("gallery/{id}") { backStackEntry ->
                        val filmId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: return@composable
                        val vm: GalleryViewModel = viewModel(
                            factory = GalleryViewModelFactory(api = appContainer.kinopoiskService, filmId = filmId)
                        )
                        GalleryScreen(
                            viewModel = vm,
                            onOpenFull = { startIndex -> navController.navigate("imageViewer/$filmId/$startIndex") }
                        )
                    }
                    composable("imageViewer/{id}/{index}") { backStackEntry ->
                        val filmId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: return@composable
                        val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
                        val vm: GalleryViewModel = viewModel(
                            factory = GalleryViewModelFactory(api = appContainer.kinopoiskService, filmId = filmId)
                        )
                        ImageViewerScreen(
                            viewModel = vm,
                            startIndex = index
                        )
                    }
                    composable("seasons/{id}") { backStackEntry ->
                        val filmId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: return@composable
                        val vm: SeasonsViewModel = viewModel(
                            factory = SeasonsViewModelFactory(api = appContainer.kinopoiskService, filmId = filmId)
                        )
                        SeasonsScreen(viewModel = vm)
                    }
                }
            }
        } else {
            OnboardingScreen(
                onFinish = {
                    coroutineScope.launch {
                        onboardingPrefs.setOnboardingCompleted()
                    }
                }
            )
        }
    }
}




