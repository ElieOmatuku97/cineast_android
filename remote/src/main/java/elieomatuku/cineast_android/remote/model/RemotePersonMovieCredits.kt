package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep
import elieomatuku.cineast_android.data.model.MovieEntity

@Keep
data class RemotePersonMovieCredits(
    val cast: List<RemoteKnownFor> = listOf()
)

@Keep
data class RemoteKnownFor(
    val release_date: String?,
    val title: String?,
    val original_title: String?,
    val id: Int?,
    val backdrop_path: String?,
    val poster_path: String?
) {
    companion object {
        fun toMovieEntity(remoteKnownFor: RemoteKnownFor): MovieEntity {
            return MovieEntity(
                releaseDate = remoteKnownFor.release_date,
                title = remoteKnownFor.title,
                originalTitle = remoteKnownFor.original_title,
                id = remoteKnownFor.id ?: 0,
                backdropPath = remoteKnownFor.backdrop_path,
                posterPath = remoteKnownFor.poster_path
            )
        }
    }
}
