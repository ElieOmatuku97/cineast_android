package elieomatuku.cineast_android.contents

import androidx.navigation.NavHostController

class ContentsNavigationActions(navController: NavHostController) {
    val navigateToMovieDetail: (movieId: Int) -> Unit = {
        navController.navigate("${ContentsDestinations.MOVIE_SCREEN_LABEL}/${it}")
    }

    val navigateToPersonDetail: (personId: Int) -> Unit = {
        navController.navigate("${ContentsDestinations.PERSON_SCREEN_LABEL}/${it}")
    }
}