package elieomatuku.cineast_android.cache.repository

import elieomatuku.cineast_android.cache.ContentDatabase
import elieomatuku.cineast_android.cache.entity.CacheGenre
import elieomatuku.cineast_android.cache.entity.CacheMovie
import elieomatuku.cineast_android.cache.entity.CacheMovieTypeJoin
import elieomatuku.cineast_android.cache.entity.CachePerson
import elieomatuku.cineast_android.cache.entity.MovieType
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by elieomatuku on 2019-12-07
 */

class ContentRepository(private val contentDatabase: ContentDatabase) {

    /**
     * Get the DiscoverContent.

     * @return a [Flowable] that will emit every time the db has been updated.
     */

    fun discoverContent(): Flowable<DiscoverContent> {
        val moviesFlowable = Flowable.zip(
            popularMovies, nowPlayingMovies, upcomingMovies, topRatedMovies,
            Function4<List<Movie>, List<Movie>, List<Movie>, List<Movie>, DiscoverContent>
            { popularMovies, nowPlayingMovies, upcomingMovies, topRatedMovies ->
                DiscoverContent(
                    popularMovies = popularMovies,
                    upcomingMovies = upcomingMovies,
                    nowPlayingMovies = nowPlayingMovies,
                    topRatedMovies = topRatedMovies
                )
            }
        )

        return Flowable.combineLatest(
            moviesFlowable, personalities,
            BiFunction { discoverContent, personalities ->
                discoverContent.people = personalities
                discoverContent
            }
        )
    }

    val popularMovies: Flowable<List<Movie>>
        get() {
            return contentDatabase.movieTypeJoinDao().getMoviesForType(MovieType.POPULAR.id).map { CacheMovie.toMovies(it) }
        }

    private val upcomingMovies: Flowable<List<Movie>>
        get() {
            return contentDatabase.movieTypeJoinDao().getMoviesForType(MovieType.UPCOMING.id)
                .map { CacheMovie.toMovies(it) }
        }

    private val nowPlayingMovies: Flowable<List<Movie>>
        get() {
            return contentDatabase.movieTypeJoinDao().getMoviesForType(MovieType.NOW_PLAYING.id)
                .map { CacheMovie.toMovies(it) }
        }

    private val topRatedMovies: Flowable<List<Movie>>
        get() {
            return contentDatabase.movieTypeJoinDao().getMoviesForType(MovieType.TOP_RATED.id)
                .map { CacheMovie.toMovies(it) }
        }

    val personalities: Flowable<List<Person>>
        get() {
            return contentDatabase.personalityDao().getPopularPeople()
                .map { CachePerson.toPeople(it) }
        }

    val genres: Maybe<List<Genre>>
        get() {
            return contentDatabase.genreDao().getAllGenres()
                .map { CacheGenre.toGenres(it) }
        }

    /**
     * Below methods insert content to the database
     */

    fun insertPopularMovie(movies: List<Movie>) {
        movies.forEach { movie ->
            insertMovie(movie, MovieType.POPULAR)
        }
    }

    fun insertUpcomingMovie(movies: List<Movie>) {
        movies.forEach { movie ->
            insertMovie(movie, MovieType.UPCOMING)
        }
    }

    fun insertNowPlayingMovie(movies: List<Movie>) {
        movies.forEach { movie ->
            insertMovie(movie, MovieType.NOW_PLAYING)
        }
    }

    fun insertTopRatedMovie(movies: List<Movie>) {
        movies.forEach { movie ->
            insertMovie(movie, MovieType.TOP_RATED)
        }
    }

    fun updatePersonality(person: Person) {
        GlobalScope.launch(Dispatchers.IO) {
            contentDatabase.personalityDao().updatePerson(CachePerson.fromPersonEntity(person))
        }
    }

    fun insertPersonalities(people: List<Person>) {
        people.forEach { personality ->
            insertPersonality(personality)
        }
    }

    fun insertPersonality(person: Person) {
        GlobalScope.launch(Dispatchers.IO) {
            contentDatabase.personalityDao().insertPerson(CachePerson.fromPersonEntity(person))
        }
    }

    fun updateMovie(movie: Movie) {
        GlobalScope.launch(Dispatchers.IO) {
            contentDatabase.movieDao().updateMovie(CacheMovie.fromMovie(movie))
        }
    }

    fun insertMovie(movie: Movie, type: MovieType) {
        contentDatabase.movieDao().insertMovie(CacheMovie.fromMovie(movie))
        contentDatabase.movieTypeJoinDao().insert(CacheMovieTypeJoin(movie.id, type.id))
    }

    fun insertGenres(genres: List<Genre>) {
        contentDatabase.genreDao().insertGenres(CacheGenre.fromGenres(genres))
    }

    /**
     * Below methods delete content in the database on the IO thread
     */
    fun deleteMovie(movie: Movie) {
        GlobalScope.launch(Dispatchers.IO) {
            contentDatabase.movieDao().delete(movie.id)
        }
    }

    fun deleteAllMovies() {
        GlobalScope.launch(Dispatchers.IO) {
            contentDatabase.movieDao().deleteAll()
        }
    }

    fun deletePersonality(person: Person) {
        GlobalScope.launch(Dispatchers.IO) {
            contentDatabase.personalityDao().delete(person.id)
        }
    }

    fun deleteAllPersonalities() {
        GlobalScope.launch(Dispatchers.IO) {
            contentDatabase.personalityDao().deleteAll()
        }
    }
}
