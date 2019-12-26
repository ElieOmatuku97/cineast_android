package elieomatuku.cineast_android.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import elieomatuku.cineast_android.database.dao.MovieDao
import elieomatuku.cineast_android.database.entity.MovieEntity
import elieomatuku.cineast_android.model.data.Movie
import kotlinx.coroutines.runBlocking
import org.junit.*
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
                context, ContentDatabase::class.java).build()

        movieDao = contentDatabase.movieDao()
    }

    @After
    fun closeDb() {
        contentDatabase.close()
    }

    @Test
    fun testInsertMovie()  {
        var movie = Movie(id = 12345, original_title = "Tropic Thunder", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true)

        movieDao.insertMovie(MovieEntity.fromMovie(movie))
        movieDao.getAllMovies()
                .test()
                .assertValue { it ==  listOf(MovieEntity.fromMovie(movie)) }
    }

    @Test
    fun testInsertMovies()  {
        var movies = listOf(
                Movie(id = 11345, original_title = "Hangover", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true),
                Movie(id = 12345, original_title = "Tropic Thunder", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true))


        movieDao.insertMovies(MovieEntity.fromMovies(movies))
        movieDao.getAllMovies()
                .test()
                .assertValue { it ==   MovieEntity.fromMovies(movies) }


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

        movieDao.getAllMovies()
                .test()
                .assertValue { it ==   MovieEntity.fromMovies(movies) }



        // empty list
        movieDao.insertMovies(listOf())
        movieDao.getAllMovies()
                .test()
                .assertValue { it ==   MovieEntity.fromMovies(movies) }
    }


    @Test
    fun testDeleteMovies()  {
        var movies = listOf(
                Movie(id = 11345, original_title = "Hangover", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true),
                Movie(id = 12345, original_title = "Tropic Thunder", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true))


        movieDao.insertMovies(MovieEntity.fromMovies(movies))
        movieDao.getAllMovies()
                .test()
                .assertValue { it ==   MovieEntity.fromMovies(movies) }


        movieDao.delete(11345)
        movieDao.getAllMovies()
                .test()
                .assertValue { it ==   listOf(MovieEntity.fromMovie( Movie(id = 12345, original_title = "Tropic Thunder", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true))) }


        movieDao.deleteAll()

        movieDao.getAllMovies()
                .test()
                .assertValue { it ==  listOf<MovieEntity>() }
    }
}