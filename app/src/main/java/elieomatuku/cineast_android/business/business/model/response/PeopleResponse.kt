package elieomatuku.cineast_android.business.business.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import elieomatuku.cineast_android.business.business.model.data.People

class PeopleResponse {
    @SerializedName("results")
    @Expose
    var results: List<People> = listOf()
}