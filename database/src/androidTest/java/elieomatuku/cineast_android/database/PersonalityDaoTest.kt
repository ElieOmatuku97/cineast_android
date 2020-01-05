package elieomatuku.cineast_android.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import elieomatuku.cineast_android.database.dao.PersonalityDao
import elieomatuku.cineast_android.database.entity.PersonalityEntity
import elieomatuku.cineast_android.core.model.Personality
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by elieomatuku on 2019-12-20
 */

@RunWith(AndroidJUnit4::class)
class PersonalityDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var contentDatabase: ContentDatabase
    private lateinit var personalityDao: PersonalityDao

    @Before
    fun initDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        contentDatabase = Room.inMemoryDatabaseBuilder(
                context, ContentDatabase::class.java).build()

        personalityDao = contentDatabase.personalityDao()
    }

    @After
    fun closeDb() {
        contentDatabase.close()
    }

    @Test
    fun insertAndGetPersonalities() {
        val personality = Personality(id = 1234, adult = true, name = "Eddie Murphy", profile_path = "personality_profile_path")

        personalityDao.insertPersonality(PersonalityEntity.fromPersonality(personality))

        personalityDao.getAllPersonalityById(personality.id)
                .test()
                .assertValue { it.id == personality.id && it.name == personality.name }
    }
}