package elieomatuku.cineast_android.business.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import elieomatuku.cineast_android.domain.model.Person

class MovieCreditsResponse {
    @SerializedName("crew")
    @Expose
    var crew: List<Person> = listOf()

    @SerializedName("cast")
    @Expose
    var cast: List<Person> = listOf()
}
