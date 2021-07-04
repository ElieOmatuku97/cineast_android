package elieomatuku.cineast_android.data.repository.model


data class PeopleEntity (
    val people: List<Person> = listOf()
)


data class Person(
    val profilePath: String?,
    val adult: Boolean?,
    val id: Int,
    val name: String?
)
