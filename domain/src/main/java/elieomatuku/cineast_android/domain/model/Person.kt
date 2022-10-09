package elieomatuku.cineast_android.domain.model

data class Person(
    val profilePath: String?,
    override val id: Int,
    override val name: String?
): Content {
    override val imagePath: String?
        get() = profilePath

    override val title: String?
        get() = name

    override val subTitle: String?
        get() = null
}
