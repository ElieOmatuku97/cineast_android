package elieomatuku.cineast_android.business.service

import android.content.Context
import android.content.SharedPreferences
import elieomatuku.cineast_android.utils.ValueStore

class PrefsStore(storeKey: String, appContext: Context) : ValueStore {

    private val prefs: SharedPreferences by kotlin.lazy {
        appContext.getSharedPreferences(storeKey, android.content.Context.MODE_PRIVATE)
    }

    override fun set(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun get(key: String, fallback: String?): String? {
        return prefs.getString(key, fallback)
    }

    override fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}