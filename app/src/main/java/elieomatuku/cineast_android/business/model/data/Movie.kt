package elieomatuku.cineast_android.business.model.data

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie (val poster_path: String?, val adult: Boolean, val overview: String,
                  val release_date: String, val genre_ids: List<Int>?, val genres: List<Genre>? ,val id: Int,
                  val original_title: String, val original_language: String?, val title: String?,
                  val backdrop_path: String?, val popularity: Double?, val vote_count: Int?, val video: Boolean?,
                  val vote_average: Float?): Widget() {


    override fun equals(other: Any?): Boolean {
        if (!(other is Movie))
            return false

        val movie: Movie = other

        return movie.id == id
    }

    override fun hashCode(): Int {
        var result = 17

        result = if (id != null) {
            31 * id  + result
        } else {
            31 * 0 + result
        }

        return result
    }

}