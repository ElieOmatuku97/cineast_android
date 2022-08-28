package elieomatuku.cineast_android.viewholder

import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.extensions.DiscoverWidget

/**
 * Created by elieomatuku on 2019-12-29
 */

interface ContentHolder {
    fun update(content: DiscoverWidget)
    fun update(content: List<Content>, titleRes: Int)
}
