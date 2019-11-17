package elieomatuku.cineast_android.presenter

import android.os.Bundle
import elieomatuku.cineast_android.business.model.data.Person
import elieomatuku.cineast_android.business.model.data.Widget
import elieomatuku.cineast_android.vu.ItemListVu
import io.reactivex.android.schedulers.AndroidSchedulers


class ItemListPresenter : ListPresenter<ItemListVu>() {


    override fun onLink(vu: ItemListVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        val listOfWidgets: List<Widget>  = args.getParcelableArrayList(WIDGET_KEY)
        val screenNameRes = args.getInt(SCREEN_NAME_KEY)


        vu.updateVu(listOfWidgets, screenNameRes)


        rxSubs.add(vu.personSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ person: Person ->
                    val params = Bundle()
                    params.putString(SCREEN_NAME_KEY, DiscoverPresenter.SCREEN_NAME)
                    params.putParcelable(PEOPLE_KEY, person)
                    vu.gotoPeople(params)
                })
        )
    }


}