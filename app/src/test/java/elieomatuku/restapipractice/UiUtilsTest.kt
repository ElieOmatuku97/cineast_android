package elieomatuku.restapipractice

import elieomatuku.restapipractice.business.business.model.data.Genre
import elieomatuku.restapipractice.utils.UiUtils
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@RunWith(RobolectricTestRunner::class)
@Config(manifest= Config.NONE)
class UiUtilsTest {
    @Test
    fun testGetImageUrl () {
        assertNotEquals(UiUtils.getImageUrl( "/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg",null, "elie"), "/blablab/labl")
        assertNotEquals (UiUtils.getImageUrl("right_path", null, "http://image.tmdb.org/t/p/w1280//"), "http://image.tmdb.org/t/p/w1280//wrong_path")

        assertEquals(UiUtils.getImageUrl("poster_path", "http://image.tmdb.org/t/p/w342//"), "http://image.tmdb.org/t/p/w342//poster_path")
        assertEquals(UiUtils.getImageUrl("right_path", null, "http://image.tmdb.org/t/p/w1280//" ), "http://image.tmdb.org/t/p/w1280//right_path")
    }

    @Test
    fun testGetYoutubeThumbnailPath() {
        assertEquals(UiUtils.getYoutubeThumbnailPath("1111111", "3.jpg"), "https://img.youtube.com/vi/1111111/3.jpg")
        assertEquals(UiUtils.getYoutubeThumbnailPath("122222", "3.jpg"), "https://img.youtube.com/vi/122222/3.jpg")

        assertNotEquals(UiUtils.getYoutubeThumbnailPath("1997", "3.jpg"), "https://img.youtube.com/vi/122222/3.jpg")
        assertNotEquals(UiUtils.getYoutubeThumbnailPath("8888", "3.jpg"), "https://img.youtube.com/vi/122222/3.jpg")
    }

    @Test
    fun testMapMovieGenreIdsWithGenreNames() {
        val movieGenreIds = mutableListOf<Int>(1, 2, 3, 4)
        val genres = mutableListOf<Genre>(Genre(1 as Integer, "action"), Genre(2 as Integer, "comedy"), Genre(3 as Integer, "drama"))

        assertEquals(UiUtils.mapMovieGenreIdsWithGenreNames(movieGenreIds = movieGenreIds as List<Integer>, genres = genres), "action, comedy, drama")
        assertNotEquals(UiUtils.mapMovieGenreIdsWithGenreNames(movieGenreIds as List<Integer>, genres), "comedy, action, drama")

        genres.add(3, Genre(4 as Integer, "sci-fi"))
        assertEquals(UiUtils.mapMovieGenreIdsWithGenreNames(movieGenreIds, genres), "action, comedy, drama, sci-fi")
        assertNotEquals(UiUtils.mapMovieGenreIdsWithGenreNames(movieGenreIds, genres), "sci-fi, drama, comedy, action")
    }

//    @Test
//    fun testFilterWidgets(){
//
//    }



}