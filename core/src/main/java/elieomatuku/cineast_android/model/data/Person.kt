package elieomatuku.cineast_android.model.data

abstract class Person  : Widget() {
    abstract val id: Int?
    abstract val name: String?
    abstract val profile_path: String?
}