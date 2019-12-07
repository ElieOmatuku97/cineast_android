package elieomatuku.cineast_android.presenter

import android.os.Bundle
import elieomatuku.cineast_android.model.data.Poster
import elieomatuku.cineast_android.vu.MovieGalleryVu


class MovieGalleryPresenter: BasePresenter<MovieGalleryVu> () {
    companion object {
        const val POSTERS = "posters"
        const val MOVIE_POSTER_PATH = "movie_poster_path"

    }

    override fun onLink(vu: MovieGalleryVu, inState: Bundle?, args: Bundle) {
        val posters: List<Poster> = args.getParcelableArrayList<Poster>(POSTERS) as List<Poster>
        vu.updateImages(posters)
        super.onLink(vu, inState, args)
    }
}