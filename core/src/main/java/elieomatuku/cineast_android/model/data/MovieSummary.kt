package elieomatuku.cineast_android.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieSummary (val movie: Movie? = null,
                         val trailers: List<Trailer>? = null,
                         val facts: MovieFacts? = null,
                         val genres: List<Genre>? = null,
                         val screenName: String? = null,
                         val cast: List<Cast>? = null,
                         val crew: List<Crew>? = null,
                         val similarMovies: List<Movie>? = null) : Parcelable {





    fun isEmpty(): Boolean  {
        val emptySummary = MovieSummary()

        return this == emptySummary
    }


}
