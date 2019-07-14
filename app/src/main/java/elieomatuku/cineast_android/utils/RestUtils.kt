package elieomatuku.cineast_android.utils


object RestUtils {


    const val REQUEST_TOKEN_KEY = "request_token_key"
    const val SESSION_ID_KEY = "session_id_key"
    const val ACCOUNT_ID_KEY = "account_id_key"


     val API_KEY: String by lazy {
        elieomatuku.cineast_android.BuildConfig.api_key
    }

}