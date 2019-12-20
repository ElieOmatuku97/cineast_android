package elieomatuku.cineast_android.business.api.response

import androidx.annotation.Keep
import elieomatuku.cineast_android.model.data.Movie

@Keep
class MovieResponse {
    var results: List<Movie> = listOf()
}