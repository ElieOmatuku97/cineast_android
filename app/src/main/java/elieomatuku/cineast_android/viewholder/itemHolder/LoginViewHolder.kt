package elieomatuku.cineast_android.viewholder.itemHolder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.cineast_android.R


class LoginViewHolder(itemView: View): RecyclerView.ViewHolder (itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_login, parent, false)
        }

        fun newInstance(parent: ViewGroup): LoginViewHolder {
            return LoginViewHolder(createView(parent))
        }

    }
}