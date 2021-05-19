package elieomatuku.cineast_android.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieSummary(
    val movie: Movie? = null,
    val trailers: List<Trailer>? = listOf(),
    val facts: MovieFacts? = null,
    val genres: List<Genre>? = listOf(),
    val screenName: String? = null,
    val cast: List<Cast>? = listOf(),
    val crew: List<Crew>? = listOf(),
    val similarMovies: List<Movie>? = listOf()
) : Parcelable {

    fun isEmpty(): Boolean {
        val emptySummary = MovieSummary()
        return this == emptySummary
    }
}
