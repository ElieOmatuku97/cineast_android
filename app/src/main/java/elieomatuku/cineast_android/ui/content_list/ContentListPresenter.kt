package elieomatuku.cineast_android.ui.content_list

import android.os.Bundle
import elieomatuku.cineast_android.core.model.Person
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.ui.discover.DiscoverPresenter
import elieomatuku.cineast_android.ui.presenter.ListPresenter
import io.reactivex.android.schedulers.AndroidSchedulers


class ContentListPresenter : ListPresenter<ContentListVu>() {


    override fun onLink(vu: ContentListVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        val listOfContents: List<Content>? = args.getParcelableArrayList(WIDGET_KEY)
        val screenNameRes = args.getInt(SCREEN_NAME_KEY)


        vu.updateVu(listOfContents, screenNameRes)


        rxSubs.add(vu.personSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { person: Person ->
                    val params = Bundle()
                    params.putString(SCREEN_NAME_KEY, DiscoverPresenter.SCREEN_NAME)
                    params.putParcelable(PEOPLE_KEY, person)
                    vu.gotoPeople(params)
                }
        )
    }


}