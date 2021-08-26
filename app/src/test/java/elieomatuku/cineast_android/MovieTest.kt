package elieomatuku.cineast_android

import elieomatuku.cineast_android.domain.model.Movie
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class MovieTest {

    @Test
    fun reflexivityTest() {
        val movieA = Movie(
            "poster_path", adult = false, overview = "overview", releaseDate = "release_date",
            genreIds = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1997, originalTitle = "original_title", originalLanguage = null,
            title = null, backdropPath = null, popularity = null, voteCount = null, video = true, voteAverage = null
        )

        val movieB: Movie = Movie(
            "poster_path", adult = false, overview = "overview", releaseDate = "release_date",
            genreIds = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1998, originalTitle = "original_title", originalLanguage = null,
            title = null, backdropPath = null, popularity = null, voteCount = null, video = true, voteAverage = null
        )

        assertEquals(movieA, movieA)
        assertEquals(movieA.equals(movieA), true)
        assertNotEquals(movieA, movieB)
    }

    @Test
    fun symmetryTest() {
        val movieA = Movie(
            "poster_path", adult = false, overview = "overview", releaseDate = "release_date",
            genreIds = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1997, originalTitle = "original_title", originalLanguage = null,
            title = null, backdropPath = null, popularity = null, voteCount = null, video = true, voteAverage = null
        )

        val movieB = Movie(
            "poster_path", adult = false, overview = "overview", releaseDate = "release_date",
            genreIds = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1997, originalTitle = "original_title", originalLanguage = null,
            title = null, backdropPath = null, popularity = null, voteCount = null, video = true, voteAverage = null
        )

        assertEquals(movieA.equals(movieA), true)
        assertEquals(movieB.equals(movieB), true)
    }

    @Test
    fun transivityTest() {
        val movieA = Movie(
            "poster_path", adult = false, overview = "overview", releaseDate = "release_date",
            genreIds = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1997, originalTitle = "original_title", originalLanguage = null,
            title = null, backdropPath = null, popularity = null, voteCount = null, video = true, voteAverage = null
        )

        val movieB = Movie(
            "poster_path", adult = false, overview = "overview", releaseDate = "release_date",
            genreIds = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1997, originalTitle = "original_title", originalLanguage = null,
            title = null, backdropPath = null, popularity = null, voteCount = null, video = true, voteAverage = null
        )

        val movieC = Movie(
            "poster_path", adult = false, overview = "overview", releaseDate = "release_date",
            genreIds = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1997, originalTitle = "original_title", originalLanguage = null,
            title = null, backdropPath = null, popularity = null, voteCount = null, video = true, voteAverage = null
        )

        assertEquals(movieA.equals(movieB), true)
        assertEquals(movieB.equals(movieC), true)
        assertEquals(movieA.equals(movieC), true)
    }

    @Test
    fun nonNullabilityTest() {
        val movieA = Movie(
            "poster_path", adult = false, overview = "overview", releaseDate = "release_date",
            genreIds = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1997, originalTitle = "original_title", originalLanguage = null,
            title = null, backdropPath = null, popularity = null, voteCount = null, video = true, voteAverage = null
        )

        assertEquals(movieA.equals(null), false)
    }

    @Test
    fun hashCodeTest() {

        val movieA = Movie(
            "poster_path", adult = false, overview = "overview", releaseDate = "release_date",
            genreIds = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1997, originalTitle = "original_title", originalLanguage = null,
            title = null, backdropPath = null, popularity = null, voteCount = null, video = true, voteAverage = null
        )

        val movieB = Movie(
            "poster_path", adult = false, overview = "overview", releaseDate = "release_date",
            genreIds = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1997, originalTitle = "original_title", originalLanguage = null,
            title = null, backdropPath = null, popularity = null, voteCount = null, video = true, voteAverage = null
        )

        val movieC = Movie(
            "poster_path", adult = false, overview = "overview", releaseDate = "release_date",
            genreIds = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1998, originalTitle = "original_title", originalLanguage = null,
            title = null, backdropPath = null, popularity = null, voteCount = null, video = true, voteAverage = null
        )

        assertEquals(movieA.hashCode(), movieB.hashCode())
        assertNotEquals(movieA.hashCode(), movieC.hashCode())
    }
}
