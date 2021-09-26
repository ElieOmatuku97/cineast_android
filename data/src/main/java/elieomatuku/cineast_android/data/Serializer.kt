package elieomatuku.cineast_android.data


interface Serializer<T> {

    fun toJson(t: T): String

    @Throws(Exception::class)
    fun fromJson(serialized: String): T?

}