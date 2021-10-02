package elieomatuku.cineast_android.business.service

import elieomatuku.cineast_android.business.api.response.MovieResponse
import elieomatuku.cineast_android.business.api.response.PersonalityResponse
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.client.TmdbContentClient
import elieomatuku.cineast_android.domain.model.Genre
import io.reactivex.Maybe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext


class ContentService(
    private val tmdbContentClient: TmdbContentClient
) : CoroutineScope {
    private val job: Job by lazy { SupervisorJob() }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job


    fun genres(): Maybe<List<Genre>> {
        return Maybe.empty()
    }

    fun searchMovies(argQuery: String, asyncResponse: AsyncResponse<MovieResponse>) {
        tmdbContentClient.searchMovies(argQuery, asyncResponse)
    }

    fun searchPeople(argQuery: String, asyncResponse: AsyncResponse<PersonalityResponse>) {
        tmdbContentClient.searchPeople(argQuery, asyncResponse)
    }
}
