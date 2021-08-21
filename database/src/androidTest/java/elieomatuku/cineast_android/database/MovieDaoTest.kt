package elieomatuku.cineast_android.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import elieomatuku.cineast_android.database.dao.MovieDao
import elieomatuku.cineast_android.database.entity.MovieEntity
import elieomatuku.cineast_android.domain.model.Movie
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by elieomatuku on 2019-12-08
 */

@RunWith(AndroidJUnit4::class)
class MovieDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var contentDatabase: ContentDatabase
    private lateinit var movieDao: MovieDao

    @Before
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        contentDatabase = Room.inMemoryDatabaseBuilder(
            context, ContentDatabase::class.java
        ).build()

        movieDao = contentDatabase.movieDao()
    }

    @After
    fun closeDb() {
        contentDatabase.close()
    }

    @Test
    fun testInsertMovie() {
        var movie = Movie(id = 12345, originalTitle = "Tropic Thunder", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true)

        movieDao.insertMovie(MovieEntity.fromMovie(movie))
        movieDao.getAllMovies()
            .test()
            .assertValue { it == listOf(MovieEntity.fromMovie(movie)) }
    }

    @Test
    fun testInsertMovies() {
        var movies = listOf(
            Movie(id = 11345, originalTitle = "Hangover", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true),
            Movie(id = 12345, originalTitle = "Tropic Thunder", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true)
        )

        movieDao.insertMovies(MovieEntity.fromMovies(movies))
        movieDao.getAllMovies()
            .test()
            .assertValue { it == MovieEntity.fromMovies(movies) }

        // duplicates
        val duplicates = listOf(
            Movie(id = 11345, originalTitle = "Hangover", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true),
            Movie(id = 13345, originalTitle = "Hangover 2", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true),
            Movie(id = 12345, originalTitle = "Tropic Thunder", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true)
        )

        movies = listOf(
            Movie(id = 11345, originalTitle = "Hangover", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true),
            Movie(id = 12345, originalTitle = "Tropic Thunder", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true),
            Movie(id = 13345, originalTitle = "Hangover 2", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true)
        )

        movieDao.insertMovies(MovieEntity.fromMovies(duplicates))

        movieDao.getAllMovies()
            .test()
            .assertValue { it == MovieEntity.fromMovies(movies) }

        // empty list
        movieDao.insertMovies(listOf())
        movieDao.getAllMovies()
            .test()
            .assertValue { it == MovieEntity.fromMovies(movies) }
    }

    @Test
    fun testDeleteMovies() {
        var movies = listOf(
            Movie(id = 11345, originalTitle = "Hangover", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true),
            Movie(id = 12345, originalTitle = "Tropic Thunder", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true)
        )

        movieDao.insertMovies(MovieEntity.fromMovies(movies))
        movieDao.getAllMovies()
            .test()
            .assertValue { it == MovieEntity.fromMovies(movies) }

        movieDao.delete(11345)
        movieDao.getAllMovies()
            .test()
            .assertValue { it == listOf(MovieEntity.fromMovie(Movie(id = 12345, originalTitle = "Tropic Thunder", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true))) }

        movieDao.deleteAll()

        movieDao.getAllMovies()
            .test()
            .assertValue { it == listOf<MovieEntity>() }
    }
}
