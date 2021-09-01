package elieomatuku.cineast_android.cache

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import elieomatuku.cineast_android.cache.dao.MovieTypeDao
import elieomatuku.cineast_android.cache.entity.MovieTypeEntity
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
class MovieTypeDaoTest {

    private lateinit var contentDatabase: ContentDatabase
    private lateinit var movieTypeDao: MovieTypeDao

    @Before
    fun initDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        contentDatabase = Room.inMemoryDatabaseBuilder(
            context, ContentDatabase::class.java
        ).build()

        movieTypeDao = contentDatabase.movieTypeDao()
        movieTypeDao.insert(MovieTypeEntity.getPredefinedTypes())
    }

    @After
    fun closeDb() {
        contentDatabase.close()
    }

    @Test
    fun testInsertMovieTypes() = runBlocking {
        var types = listOf(
            MovieTypeEntity("cineast_popular", "popular"), MovieTypeEntity("cineast_nowPlaying", "nowPlaying"),
            MovieTypeEntity("cineast_upcoming", "upcoming"),
            MovieTypeEntity("cineast_topRated", "topRated")
        )

        Assert.assertEquals(movieTypeDao.getAllTypes(), types)
    }
}
