package elieomatuku.cineast_android.domain.model

import java.io.Serializable

data class MovieSummary(
    val movie: Movie? = null,
    val trailers: List<Trailer>? = listOf(),
    val facts: MovieFacts? = null,
    val genres: List<Genre>? = listOf(),
    val screenName: String? = null,
    val cast: List<Person>? = listOf(),
    val crew: List<Person>? = listOf(),
    val similarMovies: List<Movie>? = listOf()
): Serializable {

    fun isEmpty(): Boolean {
        val emptySummary = MovieSummary()
        return this == emptySummary
    }
}
