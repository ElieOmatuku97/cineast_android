package elieomatuku.cineast_android.ui.contents

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.service.ContentService
import elieomatuku.cineast_android.core.model.Content
import org.kodein.di.generic.instance


/**
 * Created by elieomatuku on 2021-06-05
 */

abstract class ContentGridViewModel : ViewModel() {

    protected val contentService: ContentService by App.kodein.instance()
    val contentLiveData: MutableLiveData<List<Content>> = MutableLiveData()
    val errorMsgLiveData: MutableLiveData<String> = MutableLiveData()

    abstract fun getContent()
}