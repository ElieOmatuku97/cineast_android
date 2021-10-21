package elieomatuku.cineast_android.data.model

import elieomatuku.cineast_android.domain.model.MovieCredits

/**
 * Created by elieomatuku on 2021-08-08
 */

data class MovieCreditsEntity(
    val crew: List<PersonEntity>,
    val cast: List<PersonEntity>
) {
    companion object {
        fun toMovieCredits(movieCreditsEntity: MovieCreditsEntity): MovieCredits {
            return MovieCredits(
                movieCreditsEntity.crew.map { it.let(PersonEntity::toPerson) },
                movieCreditsEntity.cast.map { it.let(PersonEntity::toPerson) }
            )
        }

        fun fromMovieCredits(movieCredits: MovieCredits): MovieCreditsEntity {
            return MovieCreditsEntity(
                movieCredits.crew.map { it.let(PersonEntity::fromPerson) },
                movieCredits.cast.map { it.let(PersonEntity::fromPerson) }
            )
        }
    }
}
