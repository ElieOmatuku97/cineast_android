package elieomatuku.cineast_android.cache

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * Created by elieomatuku on 2019-12-08
 */

class IntListConverter {

    companion object {
        private val jsonAdapter: JsonAdapter<List<Int>>
            get() {
                val moshi = Moshi.Builder().build()
                val listMyData =
                    Types.newParameterizedType(List::class.java, Int::class.javaObjectType)
                return moshi.adapter(listMyData)
            }

        @TypeConverter
        @JvmStatic
        fun fromString(value: String?): List<Int> {
            return if (value.isNullOrEmpty()) {
                listOf()
            } else {
                jsonAdapter.fromJson(value)?.toList() ?: listOf()
            }
        }

        @TypeConverter
        @JvmStatic
        fun fromList(value: List<Int>?): String {
            val list = value?.toList() ?: listOf()
            return if (list.isEmpty()) {
                ""
            } else {
                jsonAdapter.toJson(list)
            }
        }
    }
}
