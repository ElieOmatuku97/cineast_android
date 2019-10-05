package elieomatuku.cineast_android.business.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import elieomatuku.cineast_android.business.model.data.Personality

class PeopleResponse {
    @SerializedName("results")
    @Expose
    var results: List<Personality> = listOf()
}