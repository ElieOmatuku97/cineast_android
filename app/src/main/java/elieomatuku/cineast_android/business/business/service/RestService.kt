package elieomatuku.cineast_android.business.business.service

import elieomatuku.cineast_android.business.business.client.RestClient
import elieomatuku.cineast_android.business.business.rest.AuthenticationApi
import elieomatuku.cineast_android.business.business.rest.MovieApi
import elieomatuku.cineast_android.business.business.rest.PeopleApi


class RestService(private val restClient: RestClient) {
    val movieApi: MovieApi by lazy {
        restClient.restAdapter.create(MovieApi::class.java)
    }

    val peopleApi: PeopleApi by lazy {
        restClient.restAdapter.create(PeopleApi::class.java)
    }

    val authenticationApi: AuthenticationApi by lazy {
        restClient.restAdapter.create(AuthenticationApi::class.java)
    }
}