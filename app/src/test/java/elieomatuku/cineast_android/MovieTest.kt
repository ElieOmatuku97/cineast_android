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
            "poster_path", adult = false, overview = "overview", release_date = "release_date",
            genre_ids = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1997, original_title = "original_title", original_language = null,
            title = null, backdrop_path = null, popularity = null, vote_count = null, video = true, vote_average = null
        )

        val movieB: Movie = Movie(
            "poster_path", adult = false, overview = "overview", release_date = "release_date",
            genre_ids = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1998, original_title = "original_title", original_language = null,
            title = null, backdrop_path = null, popularity = null, vote_count = null, video = true, vote_average = null
        )

        assertEquals(movieA, movieA)
        assertEquals(movieA.equals(movieA), true)
        assertNotEquals(movieA, movieB)
    }

    @Test
    fun symmetryTest() {
        val movieA = Movie(
            "poster_path", adult = false, overview = "overview", release_date = "release_date",
            genre_ids = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1997, original_title = "original_title", original_language = null,
            title = null, backdrop_path = null, popularity = null, vote_count = null, video = true, vote_average = null
        )

        val movieB = Movie(
            "poster_path", adult = false, overview = "overview", release_date = "release_date",
            genre_ids = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1997, original_title = "original_title", original_language = null,
            title = null, backdrop_path = null, popularity = null, vote_count = null, video = true, vote_average = null
        )

        assertEquals(movieA.equals(movieA), true)
        assertEquals(movieB.equals(movieB), true)
    }

    @Test
    fun transivityTest() {
        val movieA = Movie(
            "poster_path", adult = false, overview = "overview", release_date = "release_date",
            genre_ids = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1997, original_title = "original_title", original_language = null,
            title = null, backdrop_path = null, popularity = null, vote_count = null, video = true, vote_average = null
        )

        val movieB = Movie(
            "poster_path", adult = false, overview = "overview", release_date = "release_date",
            genre_ids = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1997, original_title = "original_title", original_language = null,
            title = null, backdrop_path = null, popularity = null, vote_count = null, video = true, vote_average = null
        )

        val movieC = Movie(
            "poster_path", adult = false, overview = "overview", release_date = "release_date",
            genre_ids = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1997, original_title = "original_title", original_language = null,
            title = null, backdrop_path = null, popularity = null, vote_count = null, video = true, vote_average = null
        )

        assertEquals(movieA.equals(movieB), true)
        assertEquals(movieB.equals(movieC), true)
        assertEquals(movieA.equals(movieC), true)
    }

    @Test
    fun nonNullabilityTest() {
        val movieA = Movie(
            "poster_path", adult = false, overview = "overview", release_date = "release_date",
            genre_ids = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1997, original_title = "original_title", original_language = null,
            title = null, backdrop_path = null, popularity = null, vote_count = null, video = true, vote_average = null
        )

        assertEquals(movieA.equals(null), false)
    }

    @Test
    fun hashCodeTest() {

        val movieA = Movie(
            "poster_path", adult = false, overview = "overview", release_date = "release_date",
            genre_ids = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1997, original_title = "original_title", original_language = null,
            title = null, backdrop_path = null, popularity = null, vote_count = null, video = true, vote_average = null
        )

        val movieB = Movie(
            "poster_path", adult = false, overview = "overview", release_date = "release_date",
            genre_ids = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1997, original_title = "original_title", original_language = null,
            title = null, backdrop_path = null, popularity = null, vote_count = null, video = true, vote_average = null
        )

        val movieC = Movie(
            "poster_path", adult = false, overview = "overview", release_date = "release_date",
            genre_ids = listOf<Int>(), genres = listOf<elieomatuku.cineast_android.domain.model.Genre>(), id = 1998, original_title = "original_title", original_language = null,
            title = null, backdrop_path = null, popularity = null, vote_count = null, video = true, vote_average = null
        )

        assertEquals(movieA.hashCode(), movieB.hashCode())
        assertNotEquals(movieA.hashCode(), movieC.hashCode())
    }
}
