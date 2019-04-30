package elieomatuku.restapipractice.business.business.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import elieomatuku.restapipractice.business.business.model.data.Trailer


class TrailerResponse {
    @SerializedName("cast_id")
    @Expose
    var id: Int? = null

    @SerializedName("results")
    @Expose
    var results:List<Trailer> = listOf()
}