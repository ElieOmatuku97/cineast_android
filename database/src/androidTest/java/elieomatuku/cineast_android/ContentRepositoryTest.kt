package elieomatuku.cineast_android

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Personality
import elieomatuku.cineast_android.database.ContentDatabase
import elieomatuku.cineast_android.database.entity.MovieTypeEntity
import elieomatuku.cineast_android.database.repository.ContentRepository
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by elieomatuku on 2019-12-18
 */

@RunWith(AndroidJUnit4::class)
class ContentRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var contentDatabase: ContentDatabase
    private lateinit var contentRepository: ContentRepository

    @Before
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        contentDatabase = Room.inMemoryDatabaseBuilder(
            context, ContentDatabase::class.java
        )
            .build()

        contentDatabase.movieTypeDao().insert(MovieTypeEntity.getPredefinedTypes())

        contentRepository = ContentRepository(contentDatabase)
    }

    @After
    fun closeDb() {
        contentDatabase.close()
    }

    @Test
    fun testInsertContentAndDiscoverContainer() {
        val personalities = listOf(
            Personality(id = 1234, adult = true, name = "Eddie Murphy", profile_path = "personality_profile_path"),
            Personality(id = 2234, adult = true, name = "Chris Rock", profile_path = "personality_profile_path"),
            Personality(id = 3234, adult = true, name = "Louis Ck", profile_path = "personality_profile_path"),
            Personality(id = 4234, adult = true, name = "Ben Stiller", profile_path = "personality_profile_path")
        )

        val movies = listOf(
            Movie(id = 12345, original_title = "Tropic Thunder", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true),
            Movie(id = 13346, original_title = "Borat", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true),
            Movie(id = 11345, original_title = "Hangover", genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true)
        )

        contentRepository.insertNowPlayingMovie(movies)
        contentRepository.insertPopularMovie(movies)
        contentRepository.insertTopRatedMovie(movies)
        contentRepository.insertUpcomingMovie(movies)
        contentRepository.insertPersonalities(personalities)

        contentRepository.discoverContent()
            .test()
            .assertValue { discoverContent ->
                discoverContent.topRatedMovies == movies && discoverContent.upcomingMovies == movies &&
                    discoverContent.nowPlayingMovies == movies &&
                    discoverContent.popularMovies == movies && discoverContent.personalities == personalities
            }
    }
}
