package elieomatuku.cineast_android.business.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import elieomatuku.cineast_android.model.data.Genre


class GenreResponse {
    @SerializedName("genres")
    @Expose
    var genres: List<Genre> = listOf()
}