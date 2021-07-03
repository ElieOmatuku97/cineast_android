package elieomatuku.cineast_android.ui.contents

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import elieomatuku.cineast_android.domain.model.Genre
import elieomatuku.cineast_android.ui.details.BaseViewModel
import io.reactivex.schedulers.Schedulers

/**
 * Created by elieomatuku on 2021-06-02
 */

class ContentsViewModel : BaseViewModel() {
    val genresLiveData: LiveData<List<Genre>> = LiveDataReactiveStreams.fromPublisher(contentService.genres().subscribeOn(Schedulers.io()).toFlowable())
}
