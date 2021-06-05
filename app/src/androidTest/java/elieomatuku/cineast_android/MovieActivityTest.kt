package elieomatuku.cineast_android

import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import elieomatuku.cineast_android.core.model.Genre
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.ui.details.movie.MovieActivity
import elieomatuku.cineast_android.ui.discover.DiscoverPresenter
import elieomatuku.cineast_android.utils.Constants
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.collections.ArrayList

/**
 * Created by elieomatuku on 2020-01-11
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class MovieActivityTest {

    private var idlingResource: IdlingResource? = null

    val movie = Movie(
        poster_path = "/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg", adult = false,
        overview = "The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown.",
        release_date = "2019-09-17",
        genre_ids = listOf(12, 18, 9648, 878, 53),
        genres = listOf(),
        id = 419704,
        original_title = "Ad Astra", original_language = "en", title = "Ad Astra", backdrop_path = "/5BwqwxMEjeFtdknRV792Svo0K1v.jpg",
        popularity = 478.246,
        vote_count = 1895,
        video = false,
        vote_average = 6.0f,
        rating = null
    )

    @Before
    fun registerIdlingResource() {

        val params = Bundle()
        params.putString(Constants.SCREEN_NAME_KEY, DiscoverPresenter.SCREEN_NAME)
        params.putParcelable(DiscoverPresenter.MOVIE_KEY, movie)
        params.putParcelableArrayList(DiscoverPresenter.MOVIE_GENRES_KEY, ArrayList(listOf<Genre>(Genre(12, "adventure"))))

        val intent = Intent(ApplicationProvider.getApplicationContext(), MovieActivity::class.java)
        intent.putExtras(params)

        val activityScenario = ActivityScenario.launch<MovieActivity>(intent)

        activityScenario.onActivity(
            ActivityScenario.ActivityAction<MovieActivity> { activity ->
                val vu = (activity as MovieActivity).mvpDispatcher.vu
                idlingResource = vu?.countingIdlingResource
                // To prove that the test fails, omit this call:
                IdlingRegistry.getInstance().register(idlingResource)
            }
        )
    }

    @Test
    fun onScreenLoaded() {

        // test movie profile display
        onView(withId(R.id.profile_image)).check(matches(isDisplayed()))
        onView(withId(R.id.item_title_view)).check(matches(isDisplayed()))
        onView(withId(R.id.item_release_view)).check(matches(isDisplayed()))
        onView(withId(R.id.item_link_view)).check(matches(isDisplayed()))
        onView(withId(R.id.item_genre_view)).check(matches(isDisplayed()))
        onView(withId(R.id.movie_rating_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.rate_btn)).check(matches(isDisplayed()))

        // test tab display
        onView(withId(R.id.overview)).check(matches(isDisplayed()))
        onView(withId(R.id.people)).check(matches(isDisplayed()))
        onView(withId(R.id.similar)).check(matches(isDisplayed()))
    }

    @Test
    fun clickSimilarTab() {
        onView(withId(R.id.similar)).check(matches(isDisplayed()))
        onView(withId(R.id.similar)).perform(click())
    }

    @After
    fun unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource)
        }
    }

    // TODO: add full test cases.
}
