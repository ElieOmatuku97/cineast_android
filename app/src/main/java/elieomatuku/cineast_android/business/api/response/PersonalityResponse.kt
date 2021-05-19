package elieomatuku.cineast_android.business.api.response

import androidx.annotation.Keep
import elieomatuku.cineast_android.core.model.Personality

@Keep
class PersonalityResponse {
    var results: List<Personality> = listOf()
}
