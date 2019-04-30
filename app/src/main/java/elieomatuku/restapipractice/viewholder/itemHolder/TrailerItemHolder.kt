package elieomatuku.restapipractice.viewholder.itemHolder


import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import elieomatuku.restapipractice.fragment.YoutubeFragment
import elieomatuku.restapipractice.R
import elieomatuku.restapipractice.utils.UiUtils
import kotlinx.android.synthetic.main.holder_trailer_item.view.*


class TrailerItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
           return LayoutInflater.from(parent.context).inflate(R.layout.holder_trailer_item, parent, false)
        }

        fun newInstance(parent: ViewGroup): TrailerItemHolder {
            return TrailerItemHolder(createView(parent))
        }
    }

    private val trailerThumbnailView: ImageView by lazy {
        itemView.trailer_thumbnail
    }

    private val trailerTitleView : TextView by lazy {
        itemView.trailer_title
    }

    fun update(trailerKey: String, trailerName: String?) {
        Log.d(TrailerItemHolder::class.java.simpleName, "Path: $trailerKey and name: $trailerName")

        Picasso.get()
                .load(UiUtils.getYoutubeThumbnailPath(trailerKey, "default.jpg" /*itemView.context.getString(R.string.param_video_size))*/))
                .into(trailerThumbnailView)

        trailerThumbnailView.setOnClickListener {
            val youtubeFragment = YoutubeFragment.newInstance(trailerKey)
            val fm = (itemView.context as AppCompatActivity).supportFragmentManager

            if (youtubeFragment != null && fm != null) {
                fm.beginTransaction().add(android.R.id.content, youtubeFragment, null).addToBackStack(null).commit()
            }
        }

//        if (!trailerName.isNullOrEmpty()) {
            trailerTitleView.visibility = View.VISIBLE
            trailerTitleView.text = trailerName
//        } else {
//            trailerTitleView.visibility = View.GONE
//        }

    }
}