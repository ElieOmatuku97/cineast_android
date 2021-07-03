package elieomatuku.cineast_android.business.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import elieomatuku.cineast_android.domain.model.Cast
import elieomatuku.cineast_android.domain.model.Crew

class MovieCreditsResponse {
    @SerializedName("crew")
    @Expose
    var crew: List<Crew> = listOf()

    @SerializedName("cast")
    @Expose
    var cast: List<Cast> = listOf()
}
