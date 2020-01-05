package elieomatuku.cineast_android.business


import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.client.TmdbContentClient
import elieomatuku.cineast_android.business.api.response.*
import elieomatuku.cineast_android.database.repository.ContentRepository
import elieomatuku.cineast_android.core.DiscoverContent
import elieomatuku.cineast_android.core.model.*
import elieomatuku.cineast_android.core.ValueStore
import elieomatuku.cineast_android.database.entity.MovieType
import io.flatcircle.coroutinehelper.onFail
import io.flatcircle.coroutinehelper.onSuccess
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

    private val persistClient: elieomatuku.cineast_android.core.ValueStore by App.kodein.instance()
    private val timeStamp: Long
        get() {
            return persistClient.get(elieomatuku.cineast_android.core.DiscoverContent.TIMESTAMP, null)?.toLong() ?: 0
        }

    /**
     * This is a list of discoverContent (movies & people) as seen on the database. Can not be changed directly
     */

    fun discoverContent(): Flowable<elieomatuku.cineast_android.core.DiscoverContent> {
        return contentRepository.discoverContent()
    }

    fun popularMovies(): Flowable<List<Movie>> {
        return contentRepository.popularMovies
    }

    fun personalities(): Flowable<List<Personality>> {
        return contentRepository.personalities
    }

    fun genres(): Maybe<List<Genre>> {
        return contentRepository.genres
    }

    fun fetchDiscoverContent() {
       val disposable =  discoverContent()
                .observeOn(Schedulers.io())
                .subscribe({
                    Timber.i("has missing content: ${it.isEmpty()} and is up to date: ${isContentUpToDate()}")
                    if (it.isEmpty()) {
                        Timber.i("Empty Content fetch from Client")
                        downloadContent()
                        downloadGenres()
                    } else if (!isContentUpToDate()) {
                        Timber.i("Needs to be refreshed, fetch from Client: ${!isContentUpToDate()}")
                        updateContent(it)
                    }
                }, {
                    Timber.e("getAllMovies: $it")
                })
    }

    private fun isContentUpToDate(): Boolean {
        return elieomatuku.cineast_android.core.DiscoverContent.isUpToDate(timeStamp)
    }

    fun downloadContent() {
        Timber.i("downloadContent called")
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

    private fun updateContent(oldDiscoverContent: elieomatuku.cineast_android.core.DiscoverContent = elieomatuku.cineast_android.core.DiscoverContent.emptyDiscoverContent()) {
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
        val response = tmdbContentClient.getPersonalities()
        response?.results?.let {
            contentRepository.insertPersonalities(it)
        }
    }

    suspend fun updatePersonalities(oldPersonalities: List<Personality>) = async {
        val response = tmdbContentClient.getPersonalities()
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

    fun downloadGenres(){
        launch {
            tmdbContentClient.getGenres() onSuccess {
                Timber.i("genres from client: ${it.genres}")
                contentRepository.insertGenres(it.genres)
            } onFail {
                Timber.d("failed to fetch genres from client: ${it}")
            }
        }
    }

    suspend fun getMovieVideos(movie: Movie): TrailerResponse? {
        return tmdbContentClient.getMovieVideos(movie).getOrNull()
    }

    suspend fun getMovieDetails(movie: Movie): MovieFacts? {
        return  tmdbContentClient.getMovieFacts(movie).getOrNull()
    }

    suspend fun getMovieCredits(movie: Movie ): MovieCreditsResponse? {
        return tmdbContentClient.getMovieCredits(movie).getOrNull()
    }

    suspend fun getSimilarMovie(movie: Movie): MovieResponse? {
        return tmdbContentClient.getSimilarMovie(movie).getOrNull()
    }

    fun getMovieImages(movieId: Int, asyncResponse: AsyncResponse<ImageResponse>) {
        tmdbContentClient.getMovieImages(movieId, asyncResponse)
    }

    fun getPeopleMovies(person: Person, asyncResponse: AsyncResponse<PeopleCreditsResponse>) {
        tmdbContentClient.getPeopleMovies(person, asyncResponse)
    }

    fun getPeopleDetails(person: Person, asyncResponse: AsyncResponse<PersonalityDetails>) {
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

    fun searchPeople(argQuery: String, asyncResponse: AsyncResponse<PersonalityResponse>) {
        tmdbContentClient.searchPeople(argQuery, asyncResponse)
    }


    private fun setContentInsertionTimeStamp() {
        persistClient.set(elieomatuku.cineast_android.core.DiscoverContent.TIMESTAMP, System.currentTimeMillis().toString())
    }
}