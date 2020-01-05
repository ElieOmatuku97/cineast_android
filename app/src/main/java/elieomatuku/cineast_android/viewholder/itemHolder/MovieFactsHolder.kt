package elieomatuku.cineast_android.viewholder.itemHolder

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.MovieFacts
import kotlinx.android.synthetic.main.holder_movie_facts.view.*
import java.text.DecimalFormat

class MovieFactsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_movie_facts, parent, false)
        }

        fun newInstance(parent: ViewGroup): MovieFactsHolder {
            return MovieFactsHolder(createView(parent))
        }
    }


    val releaseDateView: TextView by lazy {
        itemView.release_date_view
    }

    val runtimeView: TextView by lazy {
        itemView.runtime_view
    }

    val budgetView: TextView by lazy {
        itemView.budget_view
    }

    val revenueView: TextView by lazy {
        itemView.revenue_view
    }

    fun update(movieFacts: MovieFacts) {
        val formatter = DecimalFormat("#,###,###")
        releaseDateView.text = "Release date: ${movieFacts.release_date}"
        runtimeView.text = "Runtime: ${movieFacts.runtime}"
        budgetView.text = "Budget: $${formatter.format(movieFacts.budget)}"
        revenueView.text = "Revenue: $${movieFacts.revenue}"
    }
}