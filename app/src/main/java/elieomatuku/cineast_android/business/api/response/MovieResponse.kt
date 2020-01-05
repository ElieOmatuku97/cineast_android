package elieomatuku.cineast_android.business.api.response

import androidx.annotation.Keep
import elieomatuku.cineast_android.core.model.Movie

@Keep
class MovieResponse {
    var results: List<Movie> = listOf()
}