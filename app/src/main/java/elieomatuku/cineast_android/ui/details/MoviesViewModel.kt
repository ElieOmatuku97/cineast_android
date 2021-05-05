package elieomatuku.cineast_android.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.service.ContentService
import elieomatuku.cineast_android.core.model.Genre
import org.kodein.di.generic.instance


/**
 * Created by elieomatuku on 2021-05-06
 */

class MoviesViewModel : ViewModel() {

    protected val contentService: ContentService by App.kodein.instance()


    val genresLiveData: LiveData<List<Genre>> = LiveDataReactiveStreams.fromPublisher(contentService.genres().toFlowable())


}
