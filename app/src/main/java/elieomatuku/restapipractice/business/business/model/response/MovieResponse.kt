package elieomatuku.restapipractice.business.business.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import elieomatuku.restapipractice.business.business.model.data.Movie

class MovieResponse {
    @SerializedName("results")
    @Expose
    var results: List<Movie> = listOf()
}