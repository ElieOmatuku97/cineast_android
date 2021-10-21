package elieomatuku.cineast_android.data

interface PrefManager {
    fun set(key: String, value: String)

    fun get(key: String, fallback: String?): String?

    fun remove(key: String)
}
