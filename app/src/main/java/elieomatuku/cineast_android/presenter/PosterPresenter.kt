package elieomatuku.cineast_android.presenter

import android.os.Bundle
import elieomatuku.cineast_android.vu.PosterVu


class PosterPresenter : BasePresenter<PosterVu>() {
    override fun onLink(vu: PosterVu, inState: Bundle?, args: Bundle) {
        vu.updateImage(args.getString(GalleryPresenter.MOVIE_POSTER_PATH))
        super.onLink(vu, inState, args)
    }
}