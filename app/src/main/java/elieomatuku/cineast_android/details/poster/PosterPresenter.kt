package elieomatuku.cineast_android.details.poster

import android.os.Bundle
import elieomatuku.cineast_android.details.gallery.GalleryPresenter
import elieomatuku.cineast_android.presenter.BasePresenter


class PosterPresenter : BasePresenter<PosterVu>() {
    override fun onLink(vu: PosterVu, inState: Bundle?, args: Bundle) {
        vu.updateImage(args.getString(GalleryPresenter.MOVIE_POSTER_PATH))
        super.onLink(vu, inState, args)
    }
}