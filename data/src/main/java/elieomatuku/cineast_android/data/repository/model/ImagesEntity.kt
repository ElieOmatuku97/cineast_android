package elieomatuku.cineast_android.data.repository.model


data class ImagesEntity(
    val backdrops: List<ImageEntity> = listOf(),
    var posters: List<ImageEntity> = listOf(),
    var peoplePosters: List<ImageEntity> = listOf(),
)


data class ImageEntity(
    val aspectRatio: Number?,
    val filePath: String?,
    val height: Int?,
    val iso_639_1: String?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val width: Int?
)
