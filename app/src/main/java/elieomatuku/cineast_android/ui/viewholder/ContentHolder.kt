package elieomatuku.cineast_android.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.extensions.Contents

/**
 * Created by elieomatuku on 2019-12-29
 */

abstract class ContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun update(content: Contents)
}
