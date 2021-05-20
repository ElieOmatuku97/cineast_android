package elieomatuku.cineast_android.ui.details.poster

import android.os.Bundle
import elieomatuku.cineast_android.ui.details.gallery.GalleryPresenter
import elieomatuku.cineast_android.ui.presenter.BasePresenter

class PosterPresenter : BasePresenter<PosterVu>() {
    override fun onLink(vu: PosterVu, inState: Bundle?, args: Bundle) {
        vu.updateImage(args.getString(GalleryPresenter.MOVIE_POSTER_PATH))
        super.onLink(vu, inState, args)
    }
}
