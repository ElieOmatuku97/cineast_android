package elieomatuku.cineast_android.viewholder.itemHolder

import androidx.appcompat.widget.AppCompatRadioButton
import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import kotlinx.android.synthetic.main.holder_menu_movie.view.*


class MenuMovieHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_menu_movie, parent, false)
        }

        fun newInstance(parent: ViewGroup): MenuMovieHolder {
            return MenuMovieHolder(createView(parent))
        }
    }

    val overviewSegmentBtn: AppCompatRadioButton by lazy {
        itemView.overview
    }

    val similarSegmentBtn: AppCompatRadioButton by lazy {
        itemView.similar
    }

    val peopleSegmentBtn: AppCompatRadioButton by lazy {
        itemView.people
    }

}