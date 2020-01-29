package elieomatuku.cineast_android.viewholder.itemHolder

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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

        const val RELEASE_DATE = "Release date"
        const val RUNTIME = "Runtime"
        const val BUDGET = "Budget"
        const val REVENUE = "Revenue"
    }


    private val rootView: ConstraintLayout by lazy {
        itemView.root
    }

    private val releaseDateView: TextView by lazy {
        itemView.release_date_view
    }

    private val runtimeView: TextView by lazy {
        itemView.runtime_view
    }

    private val budgetView: TextView by lazy {
        itemView.budget_view
    }

    private val revenueView: TextView by lazy {
        itemView.revenue_view
    }

    fun update(movieFacts: MovieFacts?) {
        val formatter = DecimalFormat("#,###,###")

        movieFacts?.let {
            rootView.visibility = View.VISIBLE
            releaseDateView.text = displayFacts(RELEASE_DATE, movieFacts.release_date)
            runtimeView.text = displayFacts(RUNTIME, movieFacts.runtime)
            budgetView.text = displayFacts(BUDGET, movieFacts.budget )
            revenueView.text = displayFacts(REVENUE, movieFacts.revenue )
        } ?: hideRootView()

    }

    private fun displayFacts(factName: String, factValue: Int?): String {
        return factValue?.let {
            String.format("%s: %d", factName, it)
        } ?: String.format("%s: n/a", factName)
    }

    private fun displayFacts(factName: String, factValue: String?): String {
        return factValue?.let {
            String.format("%s: %s", factName, it)
        } ?: String.format("%s: n/a", factName)
    }

    private fun hideRootView() {
        rootView.visibility = View.GONE
    }
}