package elieomatuku.cineast_android.cache

import elieomatuku.cineast_android.cache.dao.GenreDao
import elieomatuku.cineast_android.cache.dao.MovieDao
import elieomatuku.cineast_android.cache.dao.MovieTypeJoinDao
import elieomatuku.cineast_android.cache.entity.CacheGenre
import elieomatuku.cineast_android.cache.entity.CacheMovie
import elieomatuku.cineast_android.cache.entity.CacheMovieTypeJoin
import elieomatuku.cineast_android.data.PrefManager
import elieomatuku.cineast_android.data.model.GenreEntity
import elieomatuku.cineast_android.data.model.MovieEntity
import elieomatuku.cineast_android.data.model.MovieType
import elieomatuku.cineast_android.data.repository.movie.MovieCache
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-09-01
 */

class MovieCacheImpl @Inject constructor (
    private val movieDao: MovieDao,
    private val joinDao: MovieTypeJoinDao,
    private val genreDao: GenreDao,
    private val prefManager: PrefManager
) :
    MovieCache {

    private val timeStamp: Long
        get() {
            return prefManager.get(ContentExpiryUtils.TIMESTAMP, null)?.toLong() ?: 0
        }

    override fun getPopularMovies(): List<MovieEntity> {
        return CacheMovie.toMovies(joinDao.getMoviesForType(MovieType.POPULAR.id))
    }

    override fun getUpcomingMovies(): List<MovieEntity> {
        return CacheMovie.toMovies(joinDao.getMoviesForType(MovieType.UPCOMING.id))
    }

    override fun getNowPlayingMovies(): List<MovieEntity> {
        return CacheMovie.toMovies(joinDao.getMoviesForType(MovieType.NOW_PLAYING.id))
    }

    override fun getTopRatedMovies(): List<MovieEntity> {
        return CacheMovie.toMovies(joinDao.getMoviesForType(MovieType.TOP_RATED.id))
    }

    override fun getGenres(): List<GenreEntity> {
        return CacheGenre.toGenres(genreDao.getAllGenres())
    }

    override fun deleteAllMovies() {
        movieDao.deleteAll()
    }

    override fun deleteMovie(movie: MovieEntity) {
        movieDao.delete(movie.id)
    }

    override fun insertGenres(genres: List<GenreEntity>) {
        genreDao.insertGenres(CacheGenre.fromGenres(genres))
    }

    override fun insertTopRatedMovie(movies: List<MovieEntity>) {
        movies.forEach { movie ->
            insertMovie(movie, MovieType.TOP_RATED)
        }
    }

    override fun insertNowPlayingMovie(movies: List<MovieEntity>) {
        movies.forEach { movie ->
            insertMovie(movie, MovieType.NOW_PLAYING)
        }
    }

    override fun insertUpcomingMovie(movies: List<MovieEntity>) {
        movies.forEach { movie ->
            insertMovie(movie, MovieType.UPCOMING)
        }
    }

    override fun insertPopularMovie(movies: List<MovieEntity>) {
        movies.forEach { movie ->
            insertMovie(movie, MovieType.POPULAR)
        }
    }

    override fun insertMovie(movie: MovieEntity, type: MovieType) {
        movieDao.insertMovie(CacheMovie.fromMovie(movie))
        joinDao.insert(CacheMovieTypeJoin(movie.id, type.id))
    }

    override fun updateMovie(movie: MovieEntity) {
        movieDao.updateMovie(CacheMovie.fromMovie(movie))
    }

    override fun isCached(): Boolean {
        return !(getPopularMovies().isNullOrEmpty() || getNowPlayingMovies().isNullOrEmpty() || getUpcomingMovies().isNullOrEmpty() || getTopRatedMovies().isNullOrEmpty())
    }

    override fun isExpired(): Boolean {
        return !ContentExpiryUtils.isUpToDate(timeStamp)
    }
}
