package elieomatuku.restapipractice.business.business.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import elieomatuku.restapipractice.business.business.model.data.Genre


class GenreResponse {
    @SerializedName("genres")
    @Expose
    var genres: List<Genre> = listOf()
}