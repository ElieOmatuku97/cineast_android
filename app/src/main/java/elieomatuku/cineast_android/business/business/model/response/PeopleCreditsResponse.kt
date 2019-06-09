package elieomatuku.restapipractice.business.business.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import elieomatuku.restapipractice.business.business.model.data.PeopleCast


class PeopleCreditsResponse {
    @SerializedName("cast")
    @Expose
    var cast: List<PeopleCast> = listOf()
}