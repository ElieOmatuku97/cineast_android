package elieomatuku.cineast_android.ui.discover

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import elieomatuku.cineast_android.R
import kotlinx.android.synthetic.main.holder_login.view.*


class LoginViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_login, parent, false)
        }

        fun newInstance(parent: ViewGroup): LoginViewHolder {
            return LoginViewHolder(createView(parent))
        }
    }

    private val notLoggedInView: TextView? by lazy {
        itemView.no_login_view
    }

    private val loginView: TextView? by lazy {
        itemView.login_view
    }

    fun update(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            notLoggedInView?.visibility = View.GONE
            loginView?.text = itemView.context.getString(R.string.settings_logout)

        } else {
            notLoggedInView?.visibility = View.VISIBLE
            loginView?.text = itemView.context.getString(R.string.settings_login)
        }
    }
}