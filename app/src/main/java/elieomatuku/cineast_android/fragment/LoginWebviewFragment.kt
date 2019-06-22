package elieomatuku.cineast_android.fragment

import android.os.Bundle
import android.os.Handler
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.activity.MainActivity
import elieomatuku.cineast_android.business.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.business.service.UserService
import io.reactivex.subjects.PublishSubject
import org.kodein.di.generic.instance
import timber.log.Timber



class LoginWebviewFragment : WebviewFragment() {
    companion object {
        const val URL = "url"

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

    private val userService : UserService by App.kodein.instance()
    val handler: Handler = Handler()

    override fun closeIconListener() {
        val activity: MainActivity = this.activity as MainActivity

        userService.getSession(userService.getRequestToken(), object: AsyncResponse<String> {
            override fun onSuccess(result: String?) {

                handler.post {
                    result?.let {
                        (activity).sessionPublisher.onNext(it)
                    }
                }
            }

            override fun onFail(error: String) {
                Timber.d("error : $error")
            }
        })

        super.closeIconListener()
    }

}