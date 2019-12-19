package elieomatuku.cineast_android.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import elieomatuku.cineast_android.database.dao.MovieDao
import elieomatuku.cineast_android.database.dao.MovieTypeDao
import elieomatuku.cineast_android.database.dao.MovieTypeJoinDao
import elieomatuku.cineast_android.database.entity.MovieEntity
import elieomatuku.cineast_android.database.entity.MovieType
import elieomatuku.cineast_android.database.entity.MovieTypeEntity
import elieomatuku.cineast_android.database.entity.MovieTypeJoin
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
class MovieTypeJoinDaoTest  {

    private lateinit var contentDatabase: ContentDatabase
    private lateinit var movieTypeJoinDao: MovieTypeJoinDao
    private lateinit var movieTypeDao: MovieTypeDao
    private lateinit var movieDao: MovieDao

    @Before
    fun initDb()  = runBlocking  {
        val context = ApplicationProvider.getApplicationContext<Context>()
        contentDatabase = Room.inMemoryDatabaseBuilder(
                context, ContentDatabase::class.java).build()

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
    fun testInsertMovieWithType() = runBlocking {
        val movie = Movie(id = 12345, original_title = "Tropic Thunder", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true)
        movieDao.insertMovie(MovieEntity.fromMovie(movie))
        movieTypeJoinDao.insert(MovieTypeJoin(movie.id, MovieType.POPULAR.id))
        Assert.assertEquals(movieTypeJoinDao.getMoviesForType(MovieType.POPULAR.id), listOf(MovieEntity.fromMovie(movie)))

        val movie2 = Movie(id = 13346, original_title = "Borat", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true)
        movieDao.insertMovie(MovieEntity.fromMovie(movie2))
        movieTypeJoinDao.insert(MovieTypeJoin(movie2.id, MovieType.POPULAR.id))

        Assert.assertEquals(movieTypeJoinDao.getMoviesForType(MovieType.POPULAR.id), listOf(MovieEntity.fromMovie(movie), MovieEntity.fromMovie(movie2)))
    }


    @Test
    fun testDeleteMovie() = runBlocking {

        var movies = listOf(
                Movie(id = 11345, original_title = "Hangover", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true),
                Movie(id = 12345, original_title = "Tropic Thunder", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true))

        //insert movie
        movieDao.insertMovies(MovieEntity.fromMovies(movies))
        movieTypeJoinDao.insert(MovieTypeJoin(movies[0].id, MovieType.POPULAR.id))
        movieTypeJoinDao.insert(MovieTypeJoin(movies[1].id, MovieType.POPULAR.id))
        Assert.assertEquals(movieTypeJoinDao.getMoviesForType(MovieType.POPULAR.id), MovieEntity.fromMovies(movies))
        Assert.assertEquals(movieTypeJoinDao.getMovieTypeJoins(),
                listOf(MovieTypeJoin(movieId=11345, movieTypeId="cineast_popular"), MovieTypeJoin(movieId=12345, movieTypeId="cineast_popular")))

        //delete movie
        movieDao.delete(12345)
        Assert.assertEquals(movieTypeJoinDao.getMovieTypeJoins(),
                listOf(MovieTypeJoin(movieId=11345, movieTypeId="cineast_popular")))
        Assert.assertEquals(movieTypeJoinDao.getMoviesForType(MovieType.POPULAR.id), listOf(MovieEntity.fromMovie(movies[0])))


        //delete type
        movieTypeDao.delete(MovieType.POPULAR.id)
        Assert.assertEquals(movieTypeJoinDao.getMovieTypeJoins(), listOf<MovieTypeJoin>())
        Assert.assertEquals(movieDao.getAllMovies(), listOf(MovieEntity.fromMovie(movies[0])))
    }
}