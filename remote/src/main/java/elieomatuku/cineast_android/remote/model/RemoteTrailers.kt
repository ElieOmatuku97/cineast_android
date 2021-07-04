package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class RemoteTrailers(
    @SerializedName("cast_id")
    @Expose
    val id: Int? = null,

    @SerializedName("results")
    @Expose
    val results: List<RemoteTrailer> = listOf()
)

@Keep
data class RemoteTrailer(
    val id: String?,
    val iso_639_1: String?,
    val iso_3166_1: String?,
    val key: String?,
    val name: String?,
    val site: String?,
    val size: Int?,
    val type: String?
)
