package elieomatuku.cineast_android.business.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import elieomatuku.cineast_android.domain.model.Image


class ImageResponse {
    @SerializedName("backdrops")
    @Expose
    var backdrops: List<Image> = listOf()

    @SerializedName("posters")
    @Expose
    var posters: List<Image> = listOf()

    @SerializedName("profiles")
    @Expose
    var peoplePosters: List<Image> = listOf()
}
