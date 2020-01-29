package elieomatuku.cineast_android

import android.content.res.Resources
import elieomatuku.cineast_android.business.client.TmdbContentClient
import elieomatuku.cineast_android.core.model.Movie
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test


/**
 * Created by elieomatuku on 2019-12-29
 */

class TmdbContentClientTest {

    @MockK
    lateinit var resources: Resources

    @MockK
    lateinit var persistClient: elieomatuku.cineast_android.core.ValueStore

    private lateinit var unauthorizedClient: TmdbContentClient



    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { resources.getString(R.string.rest_base_url) } returns "http://api.themoviedb.org/3/"
        unauthorizedClient = TmdbContentClient(resources, persistClient, MockUtils.unAuthorizedInterceptor())
    }


    private fun mockedClient(urlEnding: String, filename: String): TmdbContentClient {
        return TmdbContentClient(resources,
                persistClient,
                MockUtils.workingInterceptor(urlEnding, filename))
    }


    @Test
    fun testGetGenres() = runBlocking {
        Assert.assertEquals(true, unauthorizedClient.getGenres().isFailure)
        Assert.assertEquals(true, mockedClient("genre/movie/list?api_key=490e29e92ea126a6878a02b2779beb24", "getGenres.json").getGenres().isSuccess)
    }

    @Test
    fun testGetMovieFacts() = runBlocking {
        var movie = Movie(id = 181812, original_title = "Star Wars: The Rise of Skywalker",genre_ids = listOf(1, 2, 3, 4, 5), original_language = "English", adult = true)
        Assert.assertEquals(true, unauthorizedClient.getMovieFacts(movie).isFailure)
        Assert.assertEquals(true, mockedClient("movie/181812?api_key=490e29e92ea126a6878a02b2779beb24", "getMovieDetails.json").getMovieFacts(movie).isSuccess)
    }


}