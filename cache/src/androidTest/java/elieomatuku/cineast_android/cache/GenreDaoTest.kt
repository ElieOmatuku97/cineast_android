package elieomatuku.cineast_android.cache

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import elieomatuku.cineast_android.cache.dao.GenreDao
import elieomatuku.cineast_android.cache.entity.CacheGenre
import elieomatuku.cineast_android.domain.model.Genre
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by elieomatuku on 2019-12-29
 */

@RunWith(AndroidJUnit4::class)
class GenreDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var contentDatabase: ContentDatabase
    private lateinit var genreDao: GenreDao

    @Before
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        contentDatabase = Room.inMemoryDatabaseBuilder(
            context, ContentDatabase::class.java
        ).build()

        genreDao = contentDatabase.genreDao()
    }

    @After
    fun closeDb() {
        contentDatabase.close()
    }

    @Test
    fun testInsertAndGetGenres() {
        val genres = listOf(
            Genre(28, "Action"),
            Genre(12, "Adventure"),
            Genre(16, "Animation"),
            Genre(35, "Comedy"),
            Genre(80, "Crime")
        )

        genreDao.insertGenres(CacheGenre.fromGenres(genres))

        genreDao.getAllGenres()
            .test()
            .assertValue { it == CacheGenre.fromGenres(genres) }
    }

    @Test
    fun testEmptyRows() {
        genreDao.getAllGenres()
            .test()
            .assertValue { it == listOf<CacheGenre>() }
    }
}
