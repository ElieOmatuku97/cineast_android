package elieomatuku.cineast_android.utils

/**
 * Created by elieomatuku on 2021-07-03
 */

import java.io.Serializable

data class ViewError(
    var title: String,
    var message: String,
    var code: Int = 400
) : Serializable
