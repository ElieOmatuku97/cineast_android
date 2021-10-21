package elieomatuku.cineast_android.cache

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import elieomatuku.cineast_android.cache.dao.MovieDao
import elieomatuku.cineast_android.cache.dao.MovieTypeDao
import elieomatuku.cineast_android.cache.dao.MovieTypeJoinDao
import elieomatuku.cineast_android.cache.entity.CacheMovie
import elieomatuku.cineast_android.cache.entity.CacheMovieTypeJoin
import elieomatuku.cineast_android.cache.entity.MovieType
import elieomatuku.cineast_android.cache.entity.MovieTypeEntity
import elieomatuku.cineast_android.domain.model.Movie
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by elieomatuku on 2019-12-08
 */

@RunWith(AndroidJUnit4::class)
class CacheMovieTypeJoinDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var contentDatabase: ContentDatabase
    private lateinit var movieTypeJoinDao: MovieTypeJoinDao
    private lateinit var movieTypeDao: MovieTypeDao
    private lateinit var movieDao: MovieDao

    @Before
    fun initDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        contentDatabase = Room.inMemoryDatabaseBuilder(
            context, ContentDatabase::class.java
        ).build()

        movieTypeJoinDao = contentDatabase.movieTypeJoinDao()
        movieDao = contentDatabase.movieDao()
        movieTypeDao = contentDatabase.movieTypeDao()
        movieTypeDao.insert(MovieTypeEntity.getPredefinedTypes())
    }

    @After
    fun closeDb() {
        contentDatabase.close()
    }

    @Test
    fun testInsertMovieWithType() {
        val movie = Movie(id = 12345, originalTitle = "Tropic Thunder", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true)
        movieDao.insertMovie(CacheMovie.fromMovie(movie))
        movieTypeJoinDao.insert(CacheMovieTypeJoin(movie.id, MovieType.POPULAR.id))

        movieTypeJoinDao.getMoviesForType(MovieType.POPULAR.id)
            .test()
            .assertValue { it == listOf(CacheMovie.fromMovie(movie)) }

        val movie2 = Movie(id = 13346, originalTitle = "Borat", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true)
        movieDao.insertMovie(CacheMovie.fromMovie(movie2))
        movieTypeJoinDao.insert(CacheMovieTypeJoin(movie2.id, MovieType.POPULAR.id))

        movieTypeJoinDao.getMoviesForType(MovieType.POPULAR.id)
            .test()
            .assertValue { it == listOf(CacheMovie.fromMovie(movie), CacheMovie.fromMovie(movie2)) }
    }

    @Test
    fun testMovieTypeDuplication() {

        val movie = Movie(id = 12345, originalTitle = "Tropic Thunder", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true)

        movieDao.insertMovie(CacheMovie.fromMovie(movie))
        movieTypeJoinDao.insert(CacheMovieTypeJoin(movie.id, MovieType.POPULAR.id))

        movieDao.insertMovie(CacheMovie.fromMovie(movie))
        movieTypeJoinDao.insert(CacheMovieTypeJoin(movie.id, MovieType.NOW_PLAYING.id))

        movieTypeJoinDao.getMoviesForType(MovieType.POPULAR.id)
            .test()
            .assertValue { it == listOf(CacheMovie.fromMovie(movie)) }

        movieTypeJoinDao.getMoviesForType(MovieType.NOW_PLAYING.id)
            .test()
            .assertValue { it == listOf(CacheMovie.fromMovie(movie)) }
    }

    @Test
    fun testDeleteMovie() {

        var movies = listOf(
            Movie(id = 11345, originalTitle = "Hangover", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true),
            Movie(id = 12345, originalTitle = "Tropic Thunder", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true)
        )

        // insert movie
        movieDao.insertMovies(CacheMovie.fromMovies(movies))
        movieTypeJoinDao.insert(CacheMovieTypeJoin(movies[0].id, MovieType.POPULAR.id))
        movieTypeJoinDao.insert(CacheMovieTypeJoin(movies[1].id, MovieType.POPULAR.id))
        movieTypeJoinDao.getMoviesForType(MovieType.POPULAR.id)
            .test()
            .assertValue { it == CacheMovie.fromMovies(movies) }

        Assert.assertEquals(
            movieTypeJoinDao.getMovieTypeJoins(),
            listOf(CacheMovieTypeJoin(movieId = 11345, movieTypeId = "cineast_popular"), CacheMovieTypeJoin(movieId = 12345, movieTypeId = "cineast_popular"))
        )

        // delete movie
        movieDao.delete(12345)
        Assert.assertEquals(
            movieTypeJoinDao.getMovieTypeJoins(),
            listOf(CacheMovieTypeJoin(movieId = 11345, movieTypeId = "cineast_popular"))
        )

        movieTypeJoinDao.getMoviesForType(MovieType.POPULAR.id)
            .test()
            .assertValue { it == listOf(CacheMovie.fromMovie(movies[0])) }

        // delete type
        movieTypeDao.delete(MovieType.POPULAR.id)
        Assert.assertEquals(movieTypeJoinDao.getMovieTypeJoins(), listOf<CacheMovieTypeJoin>())
        movieDao.getAllMovies()
            .test()
            .assertValue { it == listOf(CacheMovie.fromMovie(movies[0])) }
    }
}
