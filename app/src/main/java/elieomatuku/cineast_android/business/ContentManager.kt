package elieomatuku.cineast_android.business


import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.client.TmdbContentClient
import elieomatuku.cineast_android.business.api.response.*
import elieomatuku.cineast_android.database.repository.ContentRepository
import elieomatuku.cineast_android.DiscoverContent
import elieomatuku.cineast_android.model.data.*
import elieomatuku.cineast_android.ValueStore
import elieomatuku.cineast_android.database.entity.MovieType
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KSuspendFunction0
import kotlin.reflect.KFunction1
import org.kodein.di.generic.instance


class ContentManager(private val tmdbContentClient: TmdbContentClient, private val contentRepository: ContentRepository) : CoroutineScope {
    val job: Job by lazy { SupervisorJob() }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    private val persistClient: ValueStore by App.kodein.instance()
    private val timeStamp: Long
        get() {
            return persistClient.get(DiscoverContent.TIMESTAMP, null)?.toLong() ?: 0
        }

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
        return contentRepository.personalities
    }

    fun fetchDiscoverContent() {
        discoverContent()
                .observeOn(Schedulers.io())
                .subscribe({

                    Timber.i("has missing content: ${it.isEmpty()} and is up to date: ${it.isUpToDate(timeStamp)}")
                    if (it.isEmpty()) {
                        Timber.i("Empty Content fetch from Client")
                        fetchContent()
                    } else if (!it.isUpToDate(timeStamp)) {
                        Timber.i("Needs to be refreshed, fetch from Client: ${!it.isUpToDate(timeStamp)}")
                        updateContent(it)
                    }
                }, {
                    Timber.e("getAllMovies: $it")
                })
    }

    private fun fetchContent() {
        Timber.i("fetchContent called")
        setContentInsertionTimeStamp()
        launch(Dispatchers.IO) {

            val popularMovies = getMovies(tmdbContentClient::getPopularMovies, contentRepository::insertPopularMovie)
            val upcomingMovies = getMovies(tmdbContentClient::getUpcomingMovies, contentRepository::insertUpcomingMovie)
            val nowPlayingMovies = getMovies(tmdbContentClient::getNowPlayingMovies, contentRepository::insertNowPlayingMovie)
            val topRatedMovies = getMovies(tmdbContentClient::getTopRatedMovies, contentRepository::insertTopRatedMovie)
            val popularPeople = getPopularPeople()

            //calls have been made in parallel and we now wait for all to finish
            popularMovies.await()
            upcomingMovies.await()
            nowPlayingMovies.await()
            topRatedMovies.await()
            popularPeople.await()
        }
    }

    private fun updateContent(oldDiscoverContent: DiscoverContent = DiscoverContent.emptyDiscoverContent()) {
        Timber.i("update Content Called.")
        setContentInsertionTimeStamp()
        launch(Dispatchers.IO) {

            val popularMovies = updateMovies(tmdbContentClient::getPopularMovies, oldDiscoverContent.popularMovies, MovieType.POPULAR)
            val upcomingMovies = updateMovies(tmdbContentClient::getUpcomingMovies, oldDiscoverContent.upcomingMovies, MovieType.UPCOMING)
            val nowPlayingMovies = updateMovies(tmdbContentClient::getNowPlayingMovies, oldDiscoverContent.nowPlayingMovies, MovieType.NOW_PLAYING)
            val topRatedMovies = updateMovies(tmdbContentClient::getTopRatedMovies, oldDiscoverContent.topRatedMovies, MovieType.TOP_RATED)
            val personalities = updatePersonalities(oldDiscoverContent.personalities)

            //calls have been made in parallel and we now wait for all to finish
            popularMovies.await()
            upcomingMovies.await()
            nowPlayingMovies.await()
            topRatedMovies.await()
            personalities.await()
        }
    }


    suspend fun getMovies(clientCall: KSuspendFunction0<MovieResponse?>, repositoryInsert: KFunction1<List<Movie>, Unit>) = async {
        val response = clientCall()
        response?.results?.let {
            repositoryInsert(it)
        }

    }

    suspend fun updateMovies(clientCall: KSuspendFunction0<MovieResponse?>, oldMovies: List<Movie>, type: MovieType) = async {
        val response = clientCall()
        response?.results?.let { nuMovies ->
            updateMoviesDatabase(nuMovies, oldMovies, type)
        }
    }

    private fun updateMoviesDatabase(nuMovies: List<Movie>, oldMovies: List<Movie>, type: MovieType) {
        if (nuMovies.isEmpty()) {
            contentRepository.deleteAllMovies()
        } else {
            oldMovies.forEach { oldMovie ->
                if (nuMovies.firstOrNull { it.id == oldMovie.id } == null) {
                    contentRepository.deleteMovie(oldMovie)
                }
            }

            nuMovies.forEach { nuMovie ->
                if (oldMovies.firstOrNull { it.id == nuMovie.id } != null) {
                    //update
                    contentRepository.updateMovie(nuMovie)

                } else {
                    //insert
                    contentRepository.insertMovie(nuMovie, type)
                }
            }
        }
    }

    suspend fun getPopularPeople() = async {
        val response = tmdbContentClient.getPopularPeople()
        response?.results?.let {
            contentRepository.insertPersonalities(it)
        }
    }

    suspend fun updatePersonalities(oldPersonalities: List<Personality>) = async {
        val response = tmdbContentClient.getPopularPeople()
        response?.results?.let { nuPersonalities ->
            updatePersonalitiesDatabase(nuPersonalities, oldPersonalities)
        }
    }

    private fun updatePersonalitiesDatabase(nuPersonalities: List<Personality>, oldPersonalities: List<Personality>) {
        if (nuPersonalities.isEmpty()) {
            contentRepository.deleteAllPersonalitites()
        } else {
            oldPersonalities.forEach { oldPersonalitiy ->
                if (nuPersonalities.firstOrNull { it.id == oldPersonalitiy.id } == null) {
                    contentRepository.deletePersonality(oldPersonalitiy)
                }
            }

            nuPersonalities.forEach { nuPersonality ->
                if (oldPersonalities.firstOrNull { it.id == nuPersonality.id } != null) {
                    //update
                    contentRepository.updatePersonality(nuPersonality)
                } else {
                    //insert
                    contentRepository.insertPersonality(nuPersonality)
                }
            }
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


    private fun setContentInsertionTimeStamp() {
        persistClient.set(DiscoverContent.TIMESTAMP, System.currentTimeMillis().toString())
    }
}