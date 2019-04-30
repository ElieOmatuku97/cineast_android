package elieomatuku.restapipractice.business.business.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import elieomatuku.restapipractice.business.business.model.data.Cast
import elieomatuku.restapipractice.business.business.model.data.Crew


class MovieCreditsResponse {
    @SerializedName("crew")
    @Expose
    var crew: List<Crew> = listOf()

    @SerializedName("cast")
    @Expose
    var cast: List<Cast> = listOf()
}