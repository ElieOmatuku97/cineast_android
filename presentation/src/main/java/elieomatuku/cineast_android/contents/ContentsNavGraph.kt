package elieomatuku.cineast_android.contents

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import elieomatuku.cineast_android.details.movie.MovieScreen
import elieomatuku.cineast_android.details.person.PersonScreen
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person

@Composable
fun ContentsNavGraph(
    modifier: Modifier = Modifier,
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavHostController = rememberNavController(),
    contents: List<Content>,
    hasNetworkConnection: Boolean,
    startDestination: String = ContentsDestinations.CONTENTS_SCREEN,
    onSeeAllClick: (contents: List<Content>) -> Unit
) {
    val contentsScreenActions = remember(navController) {
        ContentsNavigationActions(navController)
    }
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            ContentsDestinations.CONTENTS_SCREEN
        ) {
            ContentsScreen(
                contents = contents,
                onContentClick = {
                    if (it is Movie) {
                        contentsScreenActions.navigateToMovieDetail(it.id)
                    }

                    if (it is Person) {
                        contentsScreenActions.navigateToPersonDetail(it.id)
                    }
                }
            )
        }
        composable(
            ContentsDestinations.MOVIE_SCREEN,
            arguments = listOf(navArgument(ContentsDestinations.MOVIE_SCREEN_ARG) {
                type = NavType.IntType
            })
        ) {
            MovieScreen(
                viewModelFactory = viewModelFactory,
                hasNetworkConnection = hasNetworkConnection,
                goToGallery = { /*goToGallery()*/ },
                goToWebsite = { /*goToWebsite(it)*/ },
                gotoPerson = { /*gotoPerson(it)*/ },
                gotoMovie = { /*gotoMovie(it)*/ },
                showRatingDialog = { /*showRatingDialog(it)*/ },
                showTrailer = { /*showTrailer(it)*/ },
                onSeeAllClick = onSeeAllClick
            )
        }
        composable(ContentsDestinations.PERSON_SCREEN) {
            PersonScreen(
                viewModelFactory = viewModelFactory,
                hasNetworkConnection = hasNetworkConnection,
                goToGallery = { /*navigateToGallery()*/ },
                goToWebsite = {
                    /*goToWebsite(it)*/
                },
                gotoMovie = {
                    /*gotoMovie(it)*/
                },
                onSeeAllClick = onSeeAllClick
            )
        }
    }
}
