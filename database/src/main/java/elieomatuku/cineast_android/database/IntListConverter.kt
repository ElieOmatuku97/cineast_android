package elieomatuku.cineast_android.database

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types


/**
 * Created by elieomatuku on 2019-12-08
 */


class IntListConverter  {

    companion object {
        val jsonAdapter: JsonAdapter<List<Int>>
            get() {
                val moshi = Moshi.Builder().build()
                val listMyData = Types.newParameterizedType(List::class.java, Int::class.javaObjectType)
                return moshi.adapter(listMyData)
            }

        @TypeConverter
        @JvmStatic
        fun fromString(value: String?): List<Int> {
            if (value.isNullOrEmpty()) {
                return listOf()
            } else {
                return jsonAdapter.fromJson(value)?.toList() ?: listOf()
            }
        }

        @TypeConverter
        @JvmStatic
        fun fromList(value: List<Int>?): String {
            val list = value?.toList() ?: listOf()
            if (list.isEmpty()) {
                return ""
            } else {
                return jsonAdapter.toJson(list)
            }
        }

    }


}


