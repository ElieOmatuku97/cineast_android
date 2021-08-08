package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class RemoteMovieCredits(
    @SerializedName("crew")
    @Expose
    val crew: List<RemoteCrew> = listOf(),

    @SerializedName("cast")
    @Expose
    val cast: List<RemoteCast> = listOf()
)

@Keep
data class RemoteCrew(
    val id: Int?,
    val name: String?,
    val profile_path: String?
)

@Keep
data class RemoteCast(
    val id: Int?,
    val name: String?,
    val profile_path: String?
)
