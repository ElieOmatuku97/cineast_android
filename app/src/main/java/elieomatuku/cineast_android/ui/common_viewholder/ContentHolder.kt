package elieomatuku.cineast_android.ui.common_viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.core.model.Content


/**
 * Created by elieomatuku on 2019-12-29
 */

abstract class ContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    abstract fun update(content: Pair<Int, List<Content>>)

}