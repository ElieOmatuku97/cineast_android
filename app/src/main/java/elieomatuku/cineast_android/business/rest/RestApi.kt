package elieomatuku.cineast_android.business.rest

import elieomatuku.cineast_android.business.client.RestClient


class RestApi(private val restClient: RestClient) {
    val movie: MovieApi by lazy {
        restClient.restAdapter.create(MovieApi::class.java)
    }

    val people: PeopleApi by lazy {
        restClient.restAdapter.create(PeopleApi::class.java)
    }

    val authentication: AuthenticationApi by lazy {
        restClient.restAdapter.create(AuthenticationApi::class.java)
    }
}