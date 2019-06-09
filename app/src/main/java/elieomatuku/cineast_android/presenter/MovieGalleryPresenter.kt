package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.business.business.model.data.Poster
import elieomatuku.cineast_android.vu.MovieGalleryVu


class MovieGalleryPresenter: BasePresenter<MovieGalleryVu> () {
    companion object {
        val TAG: String by lazy {
            MovieGalleryPresenter::class.java.simpleName
        }
        const val POSTERS = "posters"
        const val MOVIE_POSTER_PATH = "movie_poster_path"
        const val ITEM_ID = "item_id"
    }

    override fun onLink(vu: MovieGalleryVu, inState: Bundle?, args: Bundle) {
        val posters: List<Poster> = args.getParcelableArrayList<Parcelable>(POSTERS) as List<Poster>
        vu.updateImages(posters)
        super.onLink(vu, inState, args)
    }
}