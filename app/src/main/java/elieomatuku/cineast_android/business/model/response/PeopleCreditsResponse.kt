package elieomatuku.cineast_android.business.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import elieomatuku.cineast_android.business.model.data.KnownFor


class PeopleCreditsResponse {
    @SerializedName("cast")
    @Expose
    var cast: List<KnownFor> = listOf()
}