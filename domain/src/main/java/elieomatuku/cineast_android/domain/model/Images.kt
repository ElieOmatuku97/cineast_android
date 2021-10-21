package elieomatuku.cineast_android.domain.model

/**
 * Created by elieomatuku on 2021-08-22
 */

data class Images(
    val backdrops: List<Image> = listOf(),
    val posters: List<Image> = listOf(),
    val peoplePosters: List<Image> = listOf(),
)
