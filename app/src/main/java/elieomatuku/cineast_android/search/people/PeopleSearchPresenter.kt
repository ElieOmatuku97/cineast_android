package elieomatuku.cineast_android.search.people

import android.os.Bundle
import elieomatuku.cineast_android.core.model.Person
import elieomatuku.cineast_android.presenter.BasePresenter
import elieomatuku.cineast_android.vu.PeopleSearchVu
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class PeopleSearchPresenter : BasePresenter<PeopleSearchVu>() {
    companion object {
        const val SCREEN_NAME_KEY = "screen_name"
        const val SCREEN_NAME = "Search"
        const val PEOPLE_KEY = "peopleApi"
    }


    override fun onLink(vu: PeopleSearchVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        vu.showLoading()

        rxSubs.add(contentService.personalities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    vu.hideLoading()
                    vu.updateList(it)

                }, { error ->
                    vu.updateErrorView(error.message)
                })
        )

        rxSubs.add(vu.peopleSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ actor: Person ->
                    val params = Bundle()
                    params.putString(SCREEN_NAME_KEY, SCREEN_NAME)
                    params.putParcelable(PEOPLE_KEY, actor)
                    vu.gotoPeople(params)
                }))
    }

}