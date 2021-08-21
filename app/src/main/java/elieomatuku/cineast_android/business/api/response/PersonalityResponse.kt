package elieomatuku.cineast_android.business.api.response

import androidx.annotation.Keep
import elieomatuku.cineast_android.domain.model.Person

@Keep
class PersonalityResponse {
    var results: List<Person> = listOf()
}
