package elieomatuku.cineast_android.business.api.response

import androidx.annotation.Keep
import elieomatuku.cineast_android.core.model.Movie

@Keep
data class MovieResponse(val results: List<Movie> = listOf())
