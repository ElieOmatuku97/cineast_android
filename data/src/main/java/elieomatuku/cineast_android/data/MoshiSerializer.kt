package elieomatuku.cineast_android.data

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type


class MoshiSerializer<T>(val type: Type) : Serializer<T> {

    override fun toJson(t: T): String {
        return jsonAdapter.toJson(t)
    }

    override fun fromJson(serialized: String): T? {
        return jsonAdapter.fromJson(serialized)
    }

    private val jsonAdapter: JsonAdapter<T> by lazy {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        moshi.adapter<T>(type)
    }
}

