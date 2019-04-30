package elieomatuku.restapipractice.business.business.service

import elieomatuku.restapipractice.business.business.client.RestClient
import elieomatuku.restapipractice.business.business.rest.AuthenticationApi
import elieomatuku.restapipractice.business.business.rest.MovieApi
import elieomatuku.restapipractice.business.business.rest.PeopleApi


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