package elieomatuku.cineast_android.settings


import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.BuildConfig
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.settings.userlist.UserListActivity
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.core.model.AccessToken
import elieomatuku.cineast_android.core.model.CineastError
import elieomatuku.cineast_android.business.client.TmdbUserClient
import elieomatuku.cineast_android.utils.WebLink
import org.kodein.di.generic.instance
import timber.log.Timber
import android.text.style.ForegroundColorSpan
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import elieomatuku.cineast_android.home.MainActivity
import elieomatuku.cineast_android.core.model.Account
import io.reactivex.android.schedulers.AndroidSchedulers


class MyTMBDFragment : PreferenceFragmentCompat(), WebLink<AccessToken?> {
    companion object {
        fun newInstance(): MyTMBDFragment {
            return MyTMBDFragment()
        }
    }

    private val tmdbUserClient: TmdbUserClient by App.kodein.instance()
    private var logInBtn: Preference? = null
    private var favoritesBtn: Preference? = null
    private var watchListBtn: Preference? = null
    private var ratedBtn: Preference? = null
    private var userName: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = getString(R.string.pref_app_settings)
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume called.")

        logInBtn?.setOnPreferenceClickListener {
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

        watchListBtn?.setOnPreferenceClickListener {
            UserListActivity.gotoWatchList(requireContext())
            true
        }

        favoritesBtn?.setOnPreferenceClickListener {
            UserListActivity.gotoFavoriteList(requireContext())
            true
        }

        ratedBtn?.setOnPreferenceClickListener {
            this@MyTMBDFragment.context?.let {
                UserListActivity.gotoRatedMovies(it)
            }

            true
        }

        (activity as MainActivity).rxSubs.add((activity as MainActivity).sessionPublisher.hide()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ sessionResponse: Pair<String?, Account> ->
                    updateState(!sessionResponse.first.isNullOrEmpty(), sessionResponse.second)
                }, { t: Throwable ->
                    Timber.e("sessionPublisher failed $t")
                })
        )
    }

    private fun updateState(isLoggedIn: Boolean, account: Account? = null) {
        logInBtn?.title = if (isLoggedIn) activity?.getString(R.string.settings_logout) else activity?.getString(R.string.settings_login)
        userName?.isVisible = isLoggedIn
        if ((account?.username) != null || (tmdbUserClient.getUsername() != null)) {
            val summary = SpannableString(account?.username ?: tmdbUserClient.getUsername())
            summary.setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.color_white)), 0, summary.length, 0)
            userName?.summary = summary
        } else {
            userName?.isVisible = false
        }

        favoritesBtn?.isVisible = isLoggedIn
        watchListBtn?.isVisible = isLoggedIn
        ratedBtn?.isVisible = isLoggedIn
    }

    override fun gotoWebview(value: AccessToken?) {
        value?.let {
            val authenticateUrl = Uri.parse(context?.getString(R.string.authenticate_url))
                    .buildUpon()
                    .appendPath(it.request_token)
                    .build()
                    .toString()

            val fm = activity?.supportFragmentManager
            fm?.beginTransaction()?.add(android.R.id.content, LoginWebviewFragment.newInstance(authenticateUrl), null)?.addToBackStack(null)?.commit()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView called.")
        setPreferencesFromResource(R.xml.settings, null)
        setUpPreferenceViews()

        val appVersion = findPreference(getString(R.string.pref_app_version))
        val summary = SpannableString("${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
        summary.setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.color_accent)), 0, summary.length, 0)
        appVersion.summary = summary

        updateState(tmdbUserClient.isLoggedIn())

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearPreferenceViews()
    }

    private fun setUpPreferenceViews() {
        logInBtn = findPreference(getString(R.string.pref_logout))
        favoritesBtn = findPreference(getString(R.string.pref_favorites))
        watchListBtn = findPreference(getString(R.string.pref_watchlist))
        ratedBtn = findPreference(getString(R.string.pref_rated))
        userName = findPreference(getString(R.string.pref_settings_username))
    }

    private fun clearPreferenceViews() {
        preferenceScreen = null
        logInBtn = null
        favoritesBtn = null
        watchListBtn = null
        ratedBtn = null
        userName = null
    }
}