package elieomatuku.cineast_android.presenter

import android.os.Bundle
import elieomatuku.cineast_android.vu.MoviePosterVu


class  MoviePosterPresenter: BasePresenter<MoviePosterVu>(){
    override fun onLink(vu: MoviePosterVu, inState: Bundle?, args: Bundle) {
        vu.updateImage(args.getString(GalleryPresenter.MOVIE_POSTER_PATH))
        super.onLink(vu, inState, args)
    }
}