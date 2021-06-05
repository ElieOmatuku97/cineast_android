package elieomatuku.cineast_android.ui.search.people

import elieomatuku.cineast_android.ui.contents.ContentGridViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by elieomatuku on 2021-06-05
 */

class PeopleGridViewModel : ContentGridViewModel() {
    init {
        GlobalScope.launch {
            getContent()
        }
    }

    override fun getContent() {
        GlobalScope.launch {
//            contentService.personalities()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            {
//                                vu.hideLoading()
//                                vu.populateGridView(it)
//                            },
//                            { error ->
//                                vu.updateErrorView(error.message)
//                            }
//                    )

            contentService.popularMovies().doAfterNext {
                contentLiveData.value = it
            }.doOnError {
                errorMsgLiveData.value = it.message
            }
        }
    }
}
