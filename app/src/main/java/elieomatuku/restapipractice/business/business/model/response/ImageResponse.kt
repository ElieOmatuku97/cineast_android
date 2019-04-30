package elieomatuku.restapipractice.business.business.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import elieomatuku.restapipractice.business.business.model.data.Backdrop
import elieomatuku.restapipractice.business.business.model.data.Poster

class ImageResponse {
    @SerializedName("backdrops")
    @Expose
    var backdrops: List<Backdrop> = listOf()

    @SerializedName("posters")
    @Expose
    var posters: List<Poster> = listOf()

    @SerializedName("profiles")
    @Expose
    var peoplePosters: List<Poster> = listOf()

}