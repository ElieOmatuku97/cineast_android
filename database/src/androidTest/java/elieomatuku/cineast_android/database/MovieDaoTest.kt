package elieomatuku.cineast_android.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import elieomatuku.cineast_android.database.dao.MovieDao
import elieomatuku.cineast_android.database.entity.MovieEntity
import elieomatuku.cineast_android.model.data.Movie
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by elieomatuku on 2019-12-08
 */

@RunWith(AndroidJUnit4::class)
class MovieDaoTest {
    private lateinit var contentDatabase: ContentDatabase
    private lateinit var movieDao: MovieDao

    @Before
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        contentDatabase = Room.inMemoryDatabaseBuilder(
                context, ContentDatabase::class.java).build()

        movieDao = contentDatabase.movieDao()
    }

    @After
    fun closeDb() {
        contentDatabase.close()
    }

    @Test
    fun testInsertMovie() = runBlocking {
        var movie = Movie(id = 12345, original_title = "Tropic Thunder", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true)

        movieDao.insertMovie(MovieEntity.fromMovie(movie))

        Assert.assertEquals(movieDao.getAllMovies(), listOf(MovieEntity.fromMovie(movie)))
    }

    @Test
    fun testInsertMovies() = runBlocking {
        var movies = listOf(
                Movie(id = 11345, original_title = "Hangover", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true),
                Movie(id = 12345, original_title = "Tropic Thunder", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true))


        movieDao.insertMovies(MovieEntity.fromMovies(movies))
        Assert.assertEquals(movieDao.getAllMovies(), MovieEntity.fromMovies(movies))


        //duplicates
        val duplicates = listOf(
                Movie(id = 11345, original_title = "Hangover", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true),
                Movie(id = 13345, original_title = "Hangover 2", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true),
                Movie(id = 12345, original_title = "Tropic Thunder", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true))

        movies = listOf(
                Movie(id = 11345, original_title = "Hangover", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true),
                Movie(id = 12345, original_title = "Tropic Thunder", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true),
                Movie(id = 13345, original_title = "Hangover 2", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true))

        movieDao.insertMovies(MovieEntity.fromMovies(duplicates))
        Assert.assertEquals(movieDao.getAllMovies(), MovieEntity.fromMovies(movies))

        // empty list
        movieDao.insertMovies(listOf())
        Assert.assertEquals(movieDao.getAllMovies(), MovieEntity.fromMovies(movies))
    }


    @Test
    fun testDeleteMovies() = runBlocking {
        var movies = listOf(
                Movie(id = 11345, original_title = "Hangover", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true),
                Movie(id = 12345, original_title = "Tropic Thunder", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true))


        movieDao.insertMovies(MovieEntity.fromMovies(movies))
        Assert.assertEquals(movieDao.getAllMovies(), MovieEntity.fromMovies(movies))

        movieDao.delete(11345)
        Assert.assertEquals(movieDao.getAllMovies(), listOf(MovieEntity.fromMovie( Movie(id = 12345, original_title = "Tropic Thunder", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true))))

        movieDao.deleteAll()
        Assert.assertEquals(movieDao.getAllMovies(), listOf<MovieEntity>())
    }

}