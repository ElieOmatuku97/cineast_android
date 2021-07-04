package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class RemotePersonMovieCredits(
    @SerializedName("cast")
    @Expose
    val cast: List<RemoteKnownFor> = listOf()
)

@Keep
data class RemoteKnownFor(
    val release_date: String?,
    val title: String?,
    val original_title: String?,
    val id: Int?,
    val backdrop_path: String?,
    val poster_path: String?
)
