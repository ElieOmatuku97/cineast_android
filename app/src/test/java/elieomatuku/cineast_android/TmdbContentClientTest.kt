package elieomatuku.cineast_android

import android.content.res.Resources
import elieomatuku.cineast_android.business.client.TmdbContentClient
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before

/**
 * Created by elieomatuku on 2019-12-29
 */

class TmdbContentClientTest {

    @MockK
    lateinit var resources: Resources

    @MockK
    lateinit var persistClient: elieomatuku.cineast_android.data.PrefManager

    private lateinit var unauthorizedClient: TmdbContentClient

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { resources.getString(R.string.rest_base_url) } returns "http://api.themoviedb.org/3/"
        unauthorizedClient =
            TmdbContentClient(resources, persistClient, MockUtils.unAuthorizedInterceptor())
    }

    private fun mockedClient(urlEnding: String, filename: String): TmdbContentClient {
        return TmdbContentClient(
            resources,
            persistClient,
            MockUtils.workingInterceptor(urlEnding, filename)
        )
    }
}
