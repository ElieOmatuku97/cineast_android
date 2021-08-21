package elieomatuku.cineast_android.domain.model

abstract class Person : Content() {
    abstract val id: Int?
    abstract val name: String?
    abstract val profilePath: String?
}
