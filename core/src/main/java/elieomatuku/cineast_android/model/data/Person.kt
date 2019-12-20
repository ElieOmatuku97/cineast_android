package elieomatuku.cineast_android.model.data

abstract class Person  : Content() {
    abstract val id: Int?
    abstract val name: String?
    abstract val profile_path: String?
}