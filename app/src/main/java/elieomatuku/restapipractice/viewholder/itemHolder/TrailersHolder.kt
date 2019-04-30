package elieomatuku.restapipractice.viewholder.itemHolder



import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.restapipractice.R
import elieomatuku.restapipractice.adapter.TrailersAdapter
import elieomatuku.restapipractice.business.business.model.data.Trailer
import kotlinx.android.synthetic.main.holder_trailers.view.*


class TrailersHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView (parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_trailers, parent, false)
        }

        fun newInstance(parent: ViewGroup): TrailersHolder {
            return TrailersHolder(createView(parent))
        }
    }

    private val listView: RecyclerView by lazy {
        itemView.trailers_view
    }

    val adapter: TrailersAdapter by lazy {
        TrailersAdapter()
    }


    fun update (movieTrailers: List<Trailer>) {
        val trailersVideoIds = mutableListOf<String?>()
        val trailersVideosTitleMap  = mutableMapOf<String?, String?>()

        movieTrailers.forEach {
            trailersVideoIds.add(it.key)
            trailersVideosTitleMap[it.key] = it.name
        }

        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        adapter.trailers = trailersVideoIds as List<String>
        adapter.trailersVideosTitleMap = trailersVideosTitleMap as Map<String, String>
        adapter.notifyDataSetChanged()
    }
}