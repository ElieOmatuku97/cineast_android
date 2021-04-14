package elieomatuku.cineast_android.ui.details.movie.overview

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.MovieFacts
import kotlinx.android.synthetic.main.holder_movie_facts.view.*

class MovieFactsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_movie_facts, parent, false)
        }

        fun newInstance(parent: ViewGroup): MovieFactsHolder {
            return MovieFactsHolder(createView(parent))
        }
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
        movieFacts?.let {
            rootView.visibility = View.VISIBLE
            releaseDateView.text = displayFacts(itemView.context.getString(R.string.release_date), movieFacts.release_date)
            runtimeView.text = displayFacts(itemView.context.getString(R.string.runtime), movieFacts.runtimeInHoursAndMinutes)
            budgetView.text = displayFacts(itemView.context.getString(R.string.budget), String.format("$%,.2f", movieFacts.budget?.toDouble()))
            revenueView.text = displayFacts(itemView.context.getString(R.string.revenue), String.format("$%,.2f", movieFacts.revenue?.toDouble()))
        } ?: hideRootView()

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