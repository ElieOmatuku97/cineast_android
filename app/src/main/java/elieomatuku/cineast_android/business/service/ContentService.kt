package elieomatuku.cineast_android.business.service

import elieomatuku.cineast_android.domain.model.Genre
import io.reactivex.Maybe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext


class ContentService : CoroutineScope {
    private val job: Job by lazy { SupervisorJob() }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job


    fun genres(): Maybe<List<Genre>> {
        return Maybe.empty()
    }
}
