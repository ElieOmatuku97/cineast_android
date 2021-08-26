package elieomatuku.cineast_android.data.model

import elieomatuku.cineast_android.domain.model.MovieFacts

/**
 * Created by elieomatuku on 2021-08-08
 */

data class MovieFactsEntity(
    val budget: Int?,
    val releaseDate: String?,
    val runtime: Int?,
    val revenue: Int?,
    val homepage: String?
) {
    companion object {
        fun toMovieFacts(movieFactsEntity: MovieFactsEntity): MovieFacts {
            return MovieFacts(
                movieFactsEntity.budget,
                movieFactsEntity.releaseDate,
                movieFactsEntity.runtime,
                movieFactsEntity.revenue,
                movieFactsEntity.homepage
            )
        }

        fun fromMovieFacts(movieFacts: MovieFacts): MovieFactsEntity {
            return MovieFactsEntity(
                movieFacts.budget,
                movieFacts.releaseDate,
                movieFacts.runtime,
                movieFacts.revenue,
                movieFacts.homepage
            )
        }
    }
}
