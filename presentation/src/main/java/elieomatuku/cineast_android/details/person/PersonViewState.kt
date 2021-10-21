package elieomatuku.cineast_android.details.person

import elieomatuku.cineast_android.domain.model.Image
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.PersonDetails
import elieomatuku.cineast_android.utils.SingleEvent
import elieomatuku.cineast_android.utils.ViewError


/**
 * Created by elieomatuku on 2021-10-02
 */

data class PersonViewState(
    val isLoading: Boolean = false,
    val person: PersonDetails? = null,
    val knownFor: List<Movie> = listOf(),
    val posters: List<Image> = listOf(),
    val screenName: String? = null,
    val viewError: SingleEvent<ViewError>? = null
)
