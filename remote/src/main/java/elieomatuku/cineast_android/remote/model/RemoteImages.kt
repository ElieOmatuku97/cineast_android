package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class RemoteImages(
    @SerializedName("backdrops")
    @Expose
    val backdrops: List<RemoteBackdrop> = listOf(),

    @SerializedName("posters")
    @Expose
    var posters: List<RemotePoster> = listOf(),

    @SerializedName("profiles")
    @Expose
    var peoplePosters: List<RemotePoster> = listOf(),
)

@Keep
data class RemoteBackdrop(
    val aspect_ratio: Number?,
    val file_path: String?,
    val height: Int?,
    val iso_639_1: String?,
    val vote_average: Double?,
    val vote_count: Int?,
    val width: Int?

)

@Keep
data class RemotePoster(
    val aspect_ratio: Number?,
    val file_path: String?,
    val height: Int?,
    val iso_639_1: String?,
    val vote_average: Double?,
    val vote_count: Int?,
    val width: Int?
)
