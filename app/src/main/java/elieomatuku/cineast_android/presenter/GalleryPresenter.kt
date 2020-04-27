package elieomatuku.cineast_android.presenter

import android.os.Bundle
import elieomatuku.cineast_android.core.model.Poster
import elieomatuku.cineast_android.vu.GalleryVu


class GalleryPresenter: BasePresenter<GalleryVu> () {
    companion object {
        const val POSTERS = "posters"
        const val MOVIE_POSTER_PATH = "movie_poster_path"

    }

    override fun onLink(vu: GalleryVu, inState: Bundle?, args: Bundle) {
        val posters: List<Poster> = args.getParcelableArrayList<Poster>(POSTERS) as List<Poster>
        vu.updateImages(posters)
        super.onLink(vu, inState, args)
    }
}