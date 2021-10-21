package elieomatuku.cineast_android.domain.model

data class Person(
    val profilePath: String?,
    override val id: Int,
    override val name: String?
): Content
