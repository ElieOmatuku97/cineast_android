package elieomatuku.cineast_android.business


import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.client.TmdbContentClient
import elieomatuku.cineast_android.business.api.response.*
import elieomatuku.cineast_android.database.repository.ContentRepository
import elieomatuku.cineast_android.model.DiscoverContent
import elieomatuku.cineast_android.model.data.*
import elieomatuku.cineast_android.utils.RestUtils
import io.reactivex.Flowable
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KSuspendFunction0
import kotlin.reflect.KSuspendFunction1


class ContentManager(private val tmdbContentClient: TmdbContentClient, private val contentRepository: ContentRepository) : CoroutineScope {
    companion object {
        val API_KEY = RestUtils.API_KEY
    }


    val job: Job by lazy { SupervisorJob() }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    /**
     * This is a list of discoverContent (movies & people) as seen on the database. Can not be changed directly
     */

    fun discoverContent(): Flowable<DiscoverContent> {
        return contentRepository.discoverContent()
    }

    fun popularMovies(): Flowable<List<Movie>> {
        return contentRepository.popularMovies
    }

    fun popularPeople(): Flowable<List<Personality>> {
        return contentRepository.popularPeople
    }

    suspend fun fetchDiscoverContent() {
        val popularMovies =  getMovies(tmdbContentClient::getPopularMovies, contentRepository::insertPopularMovie)
        val upcomingMovies = getMovies(tmdbContentClient::getUpcomingMovies, contentRepository::insertUpcomingMovie)
        val nowPlayingMovies =  getMovies(tmdbContentClient::getNowPlayingMovies, contentRepository::insertNowPlayingMovie)
        val topRatedMovies = getMovies(tmdbContentClient::getTopRatedMovies, contentRepository::insertTopRatedMovie)
        val popularPeople = getPopularPeople()

        //calls have been made in parallel and we now wait for both to finish
        popularMovies.await()
        upcomingMovies.await()
        nowPlayingMovies.await()
        topRatedMovies.await()
        popularPeople.await()
    }

    suspend fun getMovies(clientCall: KSuspendFunction0<MovieResponse?>, repositoryInsert: KSuspendFunction1<Movie, Unit>) = async {
        val response = clientCall()
        response?.results?.forEach {
            repositoryInsert(it)
        }
    }

    suspend fun getPopularPeople() = async {
        val response = tmdbContentClient.getPopularPeople()
        response?.results?.forEach {
            contentRepository.insertPersonality(it)
        }
    }

    fun getGenres(asyncResponse: AsyncResponse<GenreResponse>) {
        tmdbContentClient.getGenres(asyncResponse)
    }

    fun getMovieVideos(movie: Movie, asyncResponse: AsyncResponse<TrailerResponse>) {
        tmdbContentClient.getMovieVideos(movie, asyncResponse)
    }

    fun getMovieDetails(movie: Movie, asyncResponse: AsyncResponse<MovieDetails>) {
        tmdbContentClient.getMovieDetails(movie, asyncResponse)
    }

    fun getMovieCredits(movie: Movie, asyncResponse: AsyncResponse<MovieCreditsResponse>) {
        tmdbContentClient.getMovieCredits(movie, asyncResponse)
    }

    fun getSimilarMovie(movie: Movie, asyncResponse: AsyncResponse<MovieResponse>) {
        tmdbContentClient.getSimilarMovie(movie, asyncResponse)
    }

    fun getMovieImages(movieId: Int, asyncResponse: AsyncResponse<ImageResponse>) {
        tmdbContentClient.getMovieImages(movieId, asyncResponse)
    }

    fun getPeopleMovies(person: Person, asyncResponse: AsyncResponse<PeopleCreditsResponse>) {
        tmdbContentClient.getPeopleMovies(person, asyncResponse)
    }

    fun getPeopleDetails(person: Person, asyncResponse: AsyncResponse<PeopleDetails>) {
        tmdbContentClient.getPeopleDetails(person, asyncResponse)
    }

    fun getPeopleImages(personId: Int, asyncResponse: AsyncResponse<ImageResponse>) {
        tmdbContentClient.getPeopleImages(personId, asyncResponse)
    }


    fun getMovie(movieId: Int, asyncResponse: AsyncResponse<Movie>) {
        tmdbContentClient.getMovie(movieId, asyncResponse)
    }

    fun searchMovies(argQuery: String, asyncResponse: AsyncResponse<MovieResponse>) {
        tmdbContentClient.searchMovies(argQuery, asyncResponse)
    }

    fun searchPeople(argQuery: String, asyncResponse: AsyncResponse<PeopleResponse>) {
        tmdbContentClient.searchPeople(argQuery, asyncResponse)
    }
}