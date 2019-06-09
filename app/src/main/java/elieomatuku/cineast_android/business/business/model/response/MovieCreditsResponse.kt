package elieomatuku.cineast_android.business.business.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import elieomatuku.cineast_android.business.business.model.data.Cast
import elieomatuku.cineast_android.business.business.model.data.Crew


class MovieCreditsResponse {
    @SerializedName("crew")
    @Expose
    var crew: List<Crew> = listOf()

    @SerializedName("cast")
    @Expose
    var cast: List<Cast> = listOf()
}