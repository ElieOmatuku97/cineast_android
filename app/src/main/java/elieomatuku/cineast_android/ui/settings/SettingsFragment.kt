package elieomatuku.cineast_android.ui.settings

import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import elieomatuku.cineast_android.BuildConfig
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.AccessToken
import elieomatuku.cineast_android.domain.model.Account
import elieomatuku.cineast_android.ui.base.BasePreferenceFragmentCompat
import elieomatuku.cineast_android.ui.home.HomeActivity
import elieomatuku.cineast_android.ui.settings.user_movies.UserMoviesActivity
import elieomatuku.cineast_android.utils.WebLink
import elieomatuku.cineast_android.utils.consume
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

class SettingsFragment : BasePreferenceFragmentCompat(), WebLink<AccessToken?> {
    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    private var logInBtn: Preference? = null
    private var favoritesBtn: Preference? = null
    private var watchListBtn: Preference? = null
    private var ratedBtn: Preference? = null
    private var userName: Preference? = null

    private val viewModel: SettingsViewModel by sharedViewModel<SettingsViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = getString(R.string.pref_app_settings)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView called.")
        setPreferencesFromResource(R.xml.settings, null)
        setUpPreferenceViews()

        val appVersion = findPreference<Preference>(getString(R.string.pref_app_version))
        val summary = SpannableString("${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
        summary.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_accent
                )
            ), 0, summary.length, 0
        )
        appVersion?.summary = summary

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            updateState(state.isLoggedIn, state.account)
            state.accessToken.consume {
                gotoWebview(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume called.")

        logInBtn?.setOnPreferenceClickListener {
            if (!viewModel.isLoggedIn) {
                viewModel.getAccessToken()
            } else {
                updateState(false)
                viewModel.logout()
            }
            true
        }

        watchListBtn?.setOnPreferenceClickListener {
            UserMoviesActivity.gotoWatchList(requireContext())
            true
        }

        favoritesBtn?.setOnPreferenceClickListener {
            UserMoviesActivity.gotoFavorites(requireContext())
            true
        }

        ratedBtn?.setOnPreferenceClickListener {
            this@SettingsFragment.context?.let {
                UserMoviesActivity.gotoRatedMovies(it)
            }

            true
        }

//        (activity as HomeActivity).rxSubs.add(
//            (activity as HomeActivity).sessionPublisher.hide()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    { sessionResponse: Pair<String?, Account> ->
//                        updateState(!sessionResponse.first.isNullOrEmpty(), sessionResponse.second)
//                    },
//                    { t: Throwable ->
//                        Timber.e("sessionPublisher failed $t")
//                    }
//                )
//        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearPreferenceViews()
    }

    private fun updateState(isLoggedIn: Boolean, account: Account? = null) {
        logInBtn?.title =
            if (isLoggedIn) activity?.getString(R.string.settings_logout) else activity?.getString(R.string.settings_login)
        userName?.isVisible = isLoggedIn
        if (account?.username != null) {
            val summary = SpannableString(account.username)
            summary.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_white
                    )
                ), 0, summary.length, 0
            )
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
                .appendPath(it.requestToken)
                .build()
                .toString()

            val fm = activity?.supportFragmentManager
            fm?.beginTransaction()
                ?.add(android.R.id.content, LoginWebViewFragment.newInstance(authenticateUrl), null)
                ?.addToBackStack(null)?.commit()
        }
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
