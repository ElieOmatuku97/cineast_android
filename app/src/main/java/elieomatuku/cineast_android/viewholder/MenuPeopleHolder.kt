package elieomatuku.cineast_android.viewholder

import androidx.appcompat.widget.AppCompatRadioButton
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import kotlinx.android.synthetic.main.holder_menu_people.view.*

class MenuPeopleHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_menu_people, parent, false)
        }

        fun newInstance(parent: ViewGroup): MenuPeopleHolder {
            return MenuPeopleHolder(createView(parent))
        }
    }

    val overviewSegmentBtn: AppCompatRadioButton by lazy {
        itemView.overview
    }

    val knownForSegmentBtn: AppCompatRadioButton by lazy {
        itemView.known_for_view
    }

}