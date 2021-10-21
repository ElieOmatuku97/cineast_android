package elieomatuku.cineast_android.details.movie.overview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Trailer
import kotlinx.android.synthetic.main.holder_trailers.view.*

class TrailersHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_trailers, parent, false)
        }

        fun newInstance(parent: ViewGroup): TrailersHolder {
            return TrailersHolder(createView(parent))
        }
    }

    private val listView: RecyclerView by lazy {
        itemView.trailers_view
    }

    private val adapter: TrailersAdapter by lazy {
        TrailersAdapter()
    }

    private val root: ConstraintLayout by lazy {
        itemView.root
    }

    fun update(movieTrailers: List<Trailer>) {
        val trailersVideoIds = mutableListOf<String?>()
        val trailersVideosTitleMap = mutableMapOf<String?, String?>()

        if (!movieTrailers.isNullOrEmpty()) {

            root.visibility = View.VISIBLE

            movieTrailers.forEach {
                trailersVideoIds.add(it.key)
                trailersVideosTitleMap[it.key] = it.name
            }

            listView.adapter = adapter
            listView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter.trailers = trailersVideoIds as List<String>
            adapter.trailersVideosTitleMap = trailersVideosTitleMap as Map<String, String>
            adapter.notifyDataSetChanged()
        } else {
            root.visibility = View.GONE
        }
    }
}
