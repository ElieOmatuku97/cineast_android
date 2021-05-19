package elieomatuku.cineast_android.ui.details.movie.overview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R

class BottomHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_bottom, parent, false)
        }

        fun newInstance(parent: ViewGroup): BottomHolder {
            return BottomHolder(createView(parent))
        }
    }
}
