package elieomatuku.cineast_android.domain.model

import java.io.Serializable


/**
 * Created by elieomatuku on 2021-09-05
 */

interface Content: Serializable {
    val id: Int
    val name: String?
    val imagePath: String?
    val title: String?
    val subTitle: String?
}