package elieomatuku.cineast_android.ui.search.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import elieomatuku.cineast_android.core.model.Genre
import elieomatuku.cineast_android.ui.contents.ContentGridViewModel
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by elieomatuku on 2021-06-05
 */

class MoviesGridViewModel : ContentGridViewModel() {

    val genresLiveData: LiveData<List<Genre>> = LiveDataReactiveStreams.fromPublisher(contentService.genres().subscribeOn(Schedulers.io()).toFlowable())

    init {
        getContent()
    }

    override fun getContent() {
        GlobalScope.launch {
            contentService.popularMovies().doAfterNext {
                contentLiveData.value = it
            }.doOnError {
                errorMsgLiveData.value = it.message
            }
        }
    }
}
