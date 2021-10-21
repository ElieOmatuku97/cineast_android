package elieomatuku.cineast_android.domain.model

import java.io.Serializable

data class Image(
    val aspectRatio: Number?,
    val filePath: String?,
    val height: Int?,
    val iso6391: String?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val width: Int?
) : Serializable
