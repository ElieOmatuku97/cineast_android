package elieomatuku.cineast_android.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import timber.log.Timber


class EmptyStateHolder (itemView: View) : RecyclerView.ViewHolder (itemView) {
    companion object {
        fun createView(parent: ViewGroup): View{
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_empty_state, parent, false)
        }

        fun newInstance(parent: ViewGroup): EmptyStateHolder {
            return EmptyStateHolder(createView(parent))
        }
    }



    fun update() {

        Timber.d("empty state update function called")



    }

}