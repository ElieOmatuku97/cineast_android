package elieomatuku.cineast_android.fragment


import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.BuildConfig
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.activity.MainActivity
import elieomatuku.cineast_android.activity.UserListActivity
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.core.model.AccessToken
import elieomatuku.cineast_android.core.model.CineastError
import elieomatuku.cineast_android.business.client.TmdbUserClient
import elieomatuku.cineast_android.utils.WebLink
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import timber.log.Timber
import android.text.style.ForegroundColorSpan
import android.text.SpannableString
import androidx.core.content.ContextCompat


class MyTMBDFragment : PreferenceFragmentCompat(), WebLink<AccessToken?> {
    companion object {
        fun newInstance(): MyTMBDFragment {
            return MyTMBDFragment()
        }
    }

    private val tmdbUserClient: TmdbUserClient by App.kodein.instance()

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


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = getString(R.string.pref_app_settings)
        setPreferencesFromResource(R.xml.settings, null)

        val appVersion = findPreference(getString(R.string.pref_app_version))
        val summary = SpannableString("${getString(R.string.version_name)} (${BuildConfig.VERSION_CODE})")
        summary.setSpan(ForegroundColorSpan(ContextCompat.getColor(context!!, R.color.color_accent)), 0, summary.length, 0)
        appVersion.summary = summary


        updateState(tmdbUserClient.isLoggedIn())
    }

    override fun onResume() {
        super.onResume()

        updateState(tmdbUserClient.isLoggedIn())

        logInBtn.setOnPreferenceClickListener {
            if (!tmdbUserClient.isLoggedIn()) {
                tmdbUserClient.getAccessToken(object : AsyncResponse<AccessToken> {
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
                tmdbUserClient.logout()
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
                    Timber.e("sessionPublisher failed $t")
                })
        )
    }

    private fun updateState(isLoggedIn: Boolean) {
        logInBtn.title =  if (isLoggedIn) activity?.getString(R.string.settings_logout) else activity?.getString(R.string.settings_login)
        favoritesBtn.isVisible = isLoggedIn
        watchListBtn.isVisible = isLoggedIn
        ratedBtn.isVisible = isLoggedIn
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