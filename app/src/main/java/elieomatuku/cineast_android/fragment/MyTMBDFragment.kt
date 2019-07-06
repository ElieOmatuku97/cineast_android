package elieomatuku.cineast_android.fragment



import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.activity.MainActivity
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.model.data.AccessToken
import elieomatuku.cineast_android.business.model.data.Movie
import elieomatuku.cineast_android.business.service.UserService
import elieomatuku.cineast_android.utils.UiUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import timber.log.Timber


class MyTMBDFragment: PreferenceFragmentCompat() {
    companion object {
        fun newInstance(): MyTMBDFragment {
            return MyTMBDFragment()
        }
    }

    private val userService : UserService by App.kodein.instance()

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
                    override fun onSuccess(result: AccessToken?) {
                        Timber.d("token result:  $result")
                        if (result != null) {
                            val authenticateUrl = Uri.parse(it.context.getString(R.string.authenticate_url))
                                    .buildUpon()
                                    .appendPath(result.request_token)
                                    .build()
                                    .toString()

                            UiUtils.gotoLoginWebview(authenticateUrl, this@MyTMBDFragment.activity as AppCompatActivity)
                        }
                    }

                    override fun onFail(error: String) {
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
            userService.getWatchList( object: AsyncResponse<List<Movie>> {
                override fun onSuccess(result: List<Movie>?) {

                    handler.post {
                        result?.let {
                            val movies = it

                            this@MyTMBDFragment.context?.let {
                                UiUtils.startItemListActivity(it, movies, R.string.settings_watchlist, true)
                            }

                        }
                    }
                }

                override fun onFail(error: String) {
                    Timber.d("error : $error")
                }
            })

            true
        }

        (activity as MainActivity).rxSubs.add( (activity as MainActivity).sessionPublisher.hide()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({sessionId: String? ->
                    updateState(!sessionId.isNullOrEmpty())
                }, {t: Throwable ->
                    Timber.e("sessionPublisher failed", t)
                })
        )
    }

    private fun updateState(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            logInBtn.title = activity?.getString(R.string.settings_logout)
            favoritesBtn.isVisible =  true
            watchListBtn.isVisible = true
            ratedBtn.isVisible = true
        } else {
            logInBtn.title = activity?.getString(R.string.settings_login)
            favoritesBtn.isVisible = false
            watchListBtn.isVisible = false
            ratedBtn.isVisible = false
        }
    }
}