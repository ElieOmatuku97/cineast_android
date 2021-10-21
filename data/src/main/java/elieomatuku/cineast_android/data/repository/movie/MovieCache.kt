package elieomatuku.cineast_android.data.repository.movie

import elieomatuku.cineast_android.data.model.GenreEntity
import elieomatuku.cineast_android.data.model.MovieEntity
import elieomatuku.cineast_android.data.model.MovieType

/**
 * Created by elieomatuku on 2021-08-21
 */

interface MovieCache {

    fun getPopularMovies(): List<MovieEntity>

    fun getUpcomingMovies(): List<MovieEntity>

    fun getNowPlayingMovies(): List<MovieEntity>

    fun getTopRatedMovies(): List<MovieEntity>

    fun getGenres(): List<GenreEntity>

    fun deleteAllMovies()

    fun deleteMovie(movie: MovieEntity)

    fun insertGenres(genres: List<GenreEntity>)

    fun insertTopRatedMovie(movies: List<MovieEntity>)

    fun insertNowPlayingMovie(movies: List<MovieEntity>)

    fun insertUpcomingMovie(movies: List<MovieEntity>)

    fun insertPopularMovie(movies: List<MovieEntity>)

    fun insertMovie(movie: MovieEntity, type: MovieType)

    fun updateMovie(movie: MovieEntity)

    fun isCached(): Boolean

    fun isExpired(): Boolean
}
