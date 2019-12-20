package elieomatuku.cineast_android

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import elieomatuku.cineast_android.database.ContentDatabase
import elieomatuku.cineast_android.database.repository.ContentRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import elieomatuku.cineast_android.database.entity.MovieTypeEntity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by elieomatuku on 2019-12-18
 */

@RunWith(AndroidJUnit4::class)
class ContentRepositoryTest  {


    @get:Rule var instantTaskExecutorRule = InstantTaskExecutorRule()




    private lateinit var contentDatabase: ContentDatabase
    private lateinit var contentRepository: ContentRepository


    @Before
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        contentDatabase = Room.inMemoryDatabaseBuilder(
                context, ContentDatabase::class.java)
                .allowMainThreadQueries()
                .build()

        contentDatabase.movieTypeDao().insert(MovieTypeEntity.getPredefinedTypes())


        contentRepository = ContentRepository(contentDatabase)

    }


    @After
    fun closeDb() {
        contentDatabase.close()
    }


    @Test
    fun testInsertContentAndDiscoverContainer(){


    }



}