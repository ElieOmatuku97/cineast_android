package elieomatuku.cineast_android.data.model

import elieomatuku.cineast_android.domain.model.Image
import elieomatuku.cineast_android.domain.model.Images

data class ImageEntities(
    val backdrops: List<ImageEntity> = listOf(),
    val posters: List<ImageEntity> = listOf(),
    val peoplePosters: List<ImageEntity> = listOf(),
) {
    companion object {
        fun toImages(imageEntities: ImageEntities): Images {
            return Images(
                backdrops = imageEntities.backdrops.map { it.let(ImageEntity::toImage) },
                posters = imageEntities.posters.map { it.let(ImageEntity::toImage) },
                peoplePosters = imageEntities.peoplePosters.map { it.let(ImageEntity::toImage) }
            )
        }

        fun fromImages(images: Images): ImageEntities {
            return ImageEntities(
                backdrops = images.backdrops.map { it.let(ImageEntity::fromImage) },
                posters = images.posters.map { it.let(ImageEntity::fromImage) },
                peoplePosters = images.peoplePosters.map { it.let(ImageEntity::fromImage) }
            )
        }
    }
}

data class ImageEntity(
    val aspectRatio: Number?,
    val filePath: String?,
    val height: Int?,
    val iso6391: String?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val width: Int?
) {
    companion object {
        fun toImage(imageEntity: ImageEntity): Image {
            return Image(
                aspectRatio = imageEntity.aspectRatio,
                filePath = imageEntity.filePath,
                height = imageEntity.height,
                iso6391 = imageEntity.iso6391,
                voteAverage = imageEntity.voteAverage,
                voteCount = imageEntity.voteCount,
                width = imageEntity.width
            )
        }

        fun fromImage(image: Image): ImageEntity {
            return ImageEntity(
                aspectRatio = image.aspectRatio,
                filePath = image.filePath,
                height = image.height,
                iso6391 = image.iso6391,
                voteAverage = image.voteAverage,
                voteCount = image.voteCount,
                width = image.width
            )
        }
    }
}
