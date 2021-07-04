package elieomatuku.cineast_android.data.repository.person

import elieomatuku.cineast_android.data.repository.model.ImagesEntity
import elieomatuku.cineast_android.data.repository.model.PeopleEntity
import elieomatuku.cineast_android.data.repository.model.PersonDetailsEntity
import elieomatuku.cineast_android.data.repository.model.PersonMovieCreditsEntity


/**
 * Created by elieomatuku on 2021-07-04
 */

interface PersonRemote {
    suspend fun getPopularPeople(): PeopleEntity

    suspend fun getPeopleMovies(personId: Int): PersonMovieCreditsEntity

    suspend fun getPersonDetails(personId: Int): PersonDetailsEntity

    suspend fun getPeopleImages(personId: Int): ImagesEntity

    suspend fun searchPerson(query: String): PeopleEntity
}