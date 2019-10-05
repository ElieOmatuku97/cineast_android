package elieomatuku.cineast_android.presenter

import android.os.Bundle
import elieomatuku.cineast_android.vu.UserListVu
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber


class UserListPresenter : ListPresenter<UserListVu>() {

    companion object {

        const val DISPLAY_FAVORITE_LIST = "favorite_list_key"
        const val DISPLAY_WATCH_LIST = "watch_list_key"

    }

    private var isWatchList: Boolean = false
    private var isFavoriteList: Boolean = false

    override fun onLink(vu: UserListVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        isFavoriteList = args.getBoolean(DISPLAY_FAVORITE_LIST)
        isWatchList = args.getBoolean(DISPLAY_WATCH_LIST)



        rxSubs.add(vu.onMovieRemovedObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (isWatchList) {
                        userService.removeMovieFromWatchList(it)
                    } else if (isFavoriteList) {
                        userService.removeMovieFromFavoriteList(it)
                    }
                }, { t: Throwable ->
                    Timber.e("onMovieRemovedObservable failed $t")

                }))
    }


}