package elieomatuku.cineast_android.ui.settings

import android.os.Bundle
import android.os.Handler
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.client.TmdbUserClient
import elieomatuku.cineast_android.domain.model.Account
import elieomatuku.cineast_android.domain.model.CineastError
import elieomatuku.cineast_android.ui.fragment.WebviewFragment
import elieomatuku.cineast_android.ui.home.HomeActivity
import org.kodein.di.generic.instance
import timber.log.Timber

class LoginWebviewFragment : WebviewFragment() {
    companion object {
        private const val URL = "url"

        fun newInstance(url: String?): WebviewFragment {
            val fragment = LoginWebviewFragment()
            val args = Bundle()
            url?.let {
                args.putString(URL, it)
            }

            fragment.arguments = args
            return fragment
        }
    }

    private val tmdbUserClient: TmdbUserClient by App.kodein.instance()
    val handler: Handler = Handler()

    override fun closeIconListener() {
        val activity: HomeActivity = this.activity as HomeActivity

        tmdbUserClient.getSession(
            tmdbUserClient.getRequestToken(),
            object : AsyncResponse<Pair<String, Account>> {
                override fun onSuccess(response: Pair<String, Account>?) {

                    handler.post {
                        response?.let {
                            (activity).sessionPublisher.onNext(it)
                        }
                    }
                }

                override fun onFail(error: CineastError) {
                    Timber.d("error : $error")
                }
            }
        )
        super.closeIconListener()
    }
}
