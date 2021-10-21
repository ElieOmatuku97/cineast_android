package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep
import elieomatuku.cineast_android.data.model.MovieCreditsEntity

@Keep
data class RemoteMovieCredits(
    val crew: List<RemotePerson> = listOf(),
    val cast: List<RemotePerson> = listOf()
) {
    companion object {
        fun toMovieCreditsEntity(remoteMovieCredits: RemoteMovieCredits): MovieCreditsEntity {
            return MovieCreditsEntity(
                remoteMovieCredits.crew.map { it.let(RemotePerson::toPersonEntity) },
                remoteMovieCredits.cast.map { it.let(RemotePerson::toPersonEntity) }
            )
        }
    }
}
