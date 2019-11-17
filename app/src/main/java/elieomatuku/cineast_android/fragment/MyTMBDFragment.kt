package elieomatuku.cineast_android.fragment


import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.activity.MainActivity
import elieomatuku.cineast_android.activity.UserListActivity
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.model.data.AccessToken
import elieomatuku.cineast_android.business.model.data.CineastError
import elieomatuku.cineast_android.business.service.UserService
import elieomatuku.cineast_android.utils.WebLink
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import timber.log.Timber


class MyTMBDFragment : PreferenceFragmentCompat(), WebLink<AccessToken?> {
    companion object {
        fun newInstance(): MyTMBDFragment {
            return MyTMBDFragment()
        }
    }

    private val userService: UserService by App.kodein.instance()

    private val logInBtn: Preference by lazy {
        findPreference(getString(R.string.pref_logout))
    }

    private val favoritesBtn: Preference by lazy {
        findPreference(getString(R.string.pref_favorites))
    }

    private val watchListBtn: Preference by lazy {
        findPreference(getString(R.string.pref_watchlist))
    }

    private val ratedBtn: Preference by lazy {
        findPreference(getString(R.string.pref_rated))
    }

    private val handler: Handler = Handler()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = getString(R.string.pref_app_settings)
        setPreferencesFromResource(R.xml.settings, null)

        updateState(userService.isLoggedIn())
    }

    override fun onResume() {
        super.onResume()

        updateState(userService.isLoggedIn())

        logInBtn.setOnPreferenceClickListener {
            if (!userService.isLoggedIn()) {
                userService.getAccessToken(object : AsyncResponse<AccessToken> {
                    override fun onSuccess(response: AccessToken?) {
                        Timber.d("token result:  $response")
                        gotoWebview(response)
                    }

                    override fun onFail(error: CineastError) {
                        Timber.d("error : $error")
                    }
                })
            } else {
                updateState(false)
                userService.logout()
            }
            true
        }

        watchListBtn.setOnPreferenceClickListener {
            this@MyTMBDFragment.context?.let {
                UserListActivity.gotoWatchList(it)
            }
            true
        }

        favoritesBtn.setOnPreferenceClickListener {
            this@MyTMBDFragment.context?.let {
                UserListActivity.gotoFavoriteList(it)
            }

            true
        }

        ratedBtn.setOnPreferenceClickListener {
            this@MyTMBDFragment.context?.let {
                UserListActivity.gotoRatedMovies(it)
            }

            true
        }

        (activity as MainActivity).rxSubs.add((activity as MainActivity).sessionPublisher.hide()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ sessionId: String? ->
                    updateState(!sessionId.isNullOrEmpty())
                }, { t: Throwable ->
                    Timber.e("sessionPublisher failed", t)
                })
        )
    }

    private fun updateState(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            logInBtn.title = activity?.getString(R.string.settings_logout)
            favoritesBtn.isVisible = true
            watchListBtn.isVisible = true
            ratedBtn.isVisible = true
        } else {
            logInBtn.title = activity?.getString(R.string.settings_login)
            favoritesBtn.isVisible = false
            watchListBtn.isVisible = false
            ratedBtn.isVisible = false
        }
    }

    override fun gotoWebview(value: AccessToken?) {
        value?.let {
            val authenticateUrl = Uri.parse(context?.getString(R.string.authenticate_url))
                    .buildUpon()
                    .appendPath(it.request_token)
                    .build()
                    .toString()

            val webviewFragment: WebviewFragment? = LoginWebviewFragment.newInstance(authenticateUrl)
            val fm = activity?.supportFragmentManager

            if (webviewFragment != null && fm != null) {
                fm.beginTransaction().add(android.R.id.content, webviewFragment, null).addToBackStack(null).commit()
            }
        }
    }
}