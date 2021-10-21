package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep
import elieomatuku.cineast_android.data.model.ImageEntities
import elieomatuku.cineast_android.data.model.ImageEntity

@Keep
data class RemoteImages(
    val backdrops: List<RemoteImage> = listOf(),
    val posters: List<RemoteImage> = listOf(),
    val profiles: List<RemoteImage> = listOf(),
) {
    companion object {
        fun toImageEntities(remoteImages: RemoteImages): ImageEntities {
            return ImageEntities(
                remoteImages.backdrops.map { it.let(RemoteImage::toImageEntity) },
                remoteImages.posters.map { it.let(RemoteImage::toImageEntity) },
                remoteImages.profiles.map { it.let(RemoteImage::toImageEntity) }
            )
        }
    }
}

@Keep
data class RemoteImage(
    val aspect_ratio: Number?,
    val file_path: String?,
    val height: Int?,
    val iso_639_1: String?,
    val vote_average: Double?,
    val vote_count: Int?,
    val width: Int?
) {
    companion object {
        fun toImageEntity(remoteImage: RemoteImage): ImageEntity {
            return ImageEntity(
                remoteImage.aspect_ratio,
                remoteImage.file_path,
                remoteImage.height,
                remoteImage.iso_639_1,
                remoteImage.vote_average,
                remoteImage.vote_count,
                remoteImage.width
            )
        }
    }
}
