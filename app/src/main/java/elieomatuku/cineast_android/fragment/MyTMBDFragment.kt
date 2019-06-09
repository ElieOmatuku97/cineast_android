package elieomatuku.cineast_android.fragment



import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.business.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.business.model.data.AccessToken
import elieomatuku.cineast_android.business.business.service.UserService
import elieomatuku.cineast_android.utils.UiUtils
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

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = getString(R.string.pref_app_settings)
        setPreferencesFromResource(R.xml.settings, null)
    }


    override fun onResume() {
        super.onResume()

        logInBtn.setOnPreferenceClickListener {
            Timber.d("Preference clicked")
            userService.getAccessToken(object: AsyncResponse <AccessToken>{
                override fun onSuccess(result: AccessToken?) {
                    Timber.d("token result:  $result")
                    if (result != null) {
                        val authenticateUrl = Uri.parse(it.context.getString(R.string.authenticate_url))
                                .buildUpon()
                                .appendPath(result.request_token)
                                .build()
                                .toString()

                        UiUtils.gotoWebview (authenticateUrl, this@MyTMBDFragment.activity as AppCompatActivity)
                    }
                }

                override fun onFail(error: String) {
                   Timber.d("error : $error")
                }
            })
            true
        }
    }

}