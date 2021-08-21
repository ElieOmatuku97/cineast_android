package elieomatuku.cineast_android.data.model

/**
 * Created by elieomatuku on 2021-08-08
 */

data class MovieCreditsEntity(
    val crew: List<PersonEntity>,
    val cast: List<PersonEntity>
)
