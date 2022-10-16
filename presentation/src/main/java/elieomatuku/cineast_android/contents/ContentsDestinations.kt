package elieomatuku.cineast_android.contents

object ContentsDestinations {
    const val CONTENTS_SCREEN = "contentScreen"

    const val MOVIE_SCREEN_ARG = "movieId"
    const val MOVIE_SCREEN_LABEL = "movie"
    const val MOVIE_SCREEN = "$MOVIE_SCREEN_LABEL/{$MOVIE_SCREEN_ARG}"

    private const val PERSON_SCREEN_ARG = "personId"
    const val PERSON_SCREEN_LABEL = "person"
    const val PERSON_SCREEN = "$PERSON_SCREEN_LABEL/{$PERSON_SCREEN_ARG}"
}