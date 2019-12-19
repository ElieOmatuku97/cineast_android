package elieomatuku.cineast_android.database


import elieomatuku.cineast_android.database.entity.MovieEntity
import elieomatuku.cineast_android.database.entity.MovieType
import elieomatuku.cineast_android.database.entity.MovieTypeJoin
import elieomatuku.cineast_android.model.DiscoverContent
import elieomatuku.cineast_android.model.data.Movie
import io.reactivex.Flowable
import io.reactivex.functions.Function4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * Created by elieomatuku on 2019-12-07
 */


class ContentRepository(private val contentDatabase: ContentDatabase) {


    /**
     * Get the DiscoverContent.

     * @return a [Flowable] that will emit every time the db has been updated.
     */


    fun discoverContent(): Flowable<DiscoverContent> {
        return Flowable.zip(popularMovies(), nowPlayingMovies, upcomingMovies, topRatedMovies,
                Function4<List<Movie>, List<Movie>, List<Movie>, List<Movie>, DiscoverContent> { popularMovies, nowPlayingMovies, upcomingMovies, topRatedMovies ->
                    DiscoverContent(
                            popularMovies = popularMovies,
                            popularPeople = listOf(),
                            upcomingMovies = upcomingMovies,
                            nowPlayingMovies = nowPlayingMovies,
                            topRatedMovies = topRatedMovies)
                })
    }

    fun popularMovies(): Flowable<List<Movie>> {
        return contentDatabase.movieTypeJoinDao().getMoviesForType(MovieType.POPULAR.id)
                .map {
                    MovieEntity.toMovies(it)
                }
    }


    val upcomingMovies: Flowable<List<Movie>>
        get() {
            return contentDatabase.movieTypeJoinDao().getMoviesForType(MovieType.UPCOMING.id)
                    .map {
                        MovieEntity.toMovies(it)
                    }
        }

    val nowPlayingMovies: Flowable<List<Movie>>
        get() {
            return contentDatabase.movieTypeJoinDao().getMoviesForType(MovieType.NOW_PLAYING.id)
                    .map {
                        MovieEntity.toMovies(it)
                    }
        }

    val topRatedMovies: Flowable<List<Movie>>
        get() {
            return contentDatabase.movieTypeJoinDao().getMoviesForType(MovieType.TOP_RATED.id)
                    .map {
                        MovieEntity.toMovies(it)
                    }
        }

    fun getAllMovies(): Flowable<List<Movie>> {
        return contentDatabase.movieDao().getAllMovies()
                .map {
                    MovieEntity.toMovies(it)
                }
    }

    suspend fun insertPopularMovie(movie: Movie) {
        Timber.d("movie from insertPopularMove: $movie")

        GlobalScope.launch(Dispatchers.IO) {
            insertMovie(movie, MovieType.POPULAR)
        }
    }

    suspend fun insertUpcomingMovie(movie: Movie) {
        GlobalScope.launch(Dispatchers.IO) {
            insertMovie(movie, MovieType.UPCOMING)
        }
    }

    suspend fun insertNowPlayingMovie(movie: Movie) {
        GlobalScope.launch(Dispatchers.IO) {
            insertMovie(movie, MovieType.NOW_PLAYING)
        }
    }

    suspend fun insertTopRatedMovie(movie: Movie) {
        GlobalScope.launch(Dispatchers.IO) {
            insertMovie(movie, MovieType.TOP_RATED)
        }
    }

    suspend fun insertMovie(movie: Movie, type: MovieType) {
        GlobalScope.launch(Dispatchers.IO) {
            contentDatabase.movieDao().insertMovie(MovieEntity.fromMovie(movie))
            contentDatabase.movieTypeJoinDao().insert(MovieTypeJoin(movie.id, type.id))
        }
    }
}