package elieomatuku.cineast_android.business.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import elieomatuku.cineast_android.business.model.data.Movie

class MovieResponse {
    @SerializedName("results")
    @Expose
    var results: List<Movie> = listOf()
}