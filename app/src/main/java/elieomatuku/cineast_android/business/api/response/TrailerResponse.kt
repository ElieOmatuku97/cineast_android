package elieomatuku.cineast_android.business.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import elieomatuku.cineast_android.core.model.Trailer

class TrailerResponse {
    @SerializedName("cast_id")
    @Expose
    var id: Int? = null

    @SerializedName("results")
    @Expose
    var results: List<Trailer> = listOf()
}
