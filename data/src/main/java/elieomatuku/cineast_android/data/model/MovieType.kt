package elieomatuku.cineast_android.data.model


/**
 * Created by elieomatuku on 2021-08-21
 */

enum class MovieType (val id: String, val type: String) {
    POPULAR("cineast_popular", "popular"),
    NOW_PLAYING("cineast_nowPlaying", "nowPlaying"),
    UPCOMING("cineast_upcoming", "upcoming"),
    TOP_RATED("cineast_topRated", "topRated");
}