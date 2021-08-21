package elieomatuku.cineast_android

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import elieomatuku.cineast_android.database.ContentDatabase
import elieomatuku.cineast_android.database.entity.MovieTypeEntity
import elieomatuku.cineast_android.database.repository.ContentRepository
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person
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
            Person(id = 1234, adult = true, name = "Eddie Murphy", profilePath = "personality_profile_path"),
            Person(id = 2234, adult = true, name = "Chris Rock", profilePath = "personality_profile_path"),
            Person(id = 3234, adult = true, name = "Louis Ck", profilePath = "personality_profile_path"),
            Person(id = 4234, adult = true, name = "Ben Stiller", profilePath = "personality_profile_path")
        )

        val movies = listOf(
            Movie(id = 12345, originalTitle = "Tropic Thunder", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true),
            Movie(id = 13346, originalTitle = "Borat", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true),
            Movie(id = 11345, originalTitle = "Hangover", genreIds = listOf(1, 2, 3, 4, 5), originalLanguage = "English", adult = true)
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
                    discoverContent.popularMovies == movies && discoverContent.people == personalities
            }
    }
}
