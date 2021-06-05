package elieomatuku.cineast_android.ui.search.people

import android.os.Bundle
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.ui.presenter.BasePresenter
import elieomatuku.cineast_android.ui.search.ContentGridVu
import elieomatuku.cineast_android.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PeopleSearchPresenter : BasePresenter<ContentGridVu>() {
    companion object {
        const val SCREEN_NAME = "Search"
        const val CONTENT_KEY = "peopleApi"
    }

    override fun onLink(vu: ContentGridVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        vu.showLoading()

        rxSubs.add(
            contentService.personalities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        vu.hideLoading()
                        vu.populateGridView(it)
                    },
                    { error ->
                        vu.updateErrorView(error.message)
                    }
                )
        )

        rxSubs.add(
            vu.contentSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { content: Content ->
                    val params = Bundle()
                    params.putString(Constants.SCREEN_NAME_KEY, SCREEN_NAME)
                    params.putParcelable(CONTENT_KEY, content)
                    vu.gotoPeople(params)
                }
        )
    }
}
