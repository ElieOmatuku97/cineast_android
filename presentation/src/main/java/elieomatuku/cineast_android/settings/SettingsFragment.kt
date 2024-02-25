package elieomatuku.cineast_android.settings

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.fragment.findNavController
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.domain.model.AccessToken
import elieomatuku.cineast_android.materialtheme.ui.theme.AppTheme
import elieomatuku.cineast_android.settings.usercontents.UserContentsActivity
import elieomatuku.cineast_android.utils.WebLink
import elieomatuku.cineast_android.utils.consume

class SettingsFragment : BaseFragment(), WebLink<AccessToken?> {

    private val viewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    SettingsScreen(
                        viewModel = viewModel,
                        onLoginClick = {
                            viewModel.onLoginClicked()
                        },
                        onWatchListClick = { UserContentsActivity.gotoWatchList(requireContext()) },
                        onRatedClick = { UserContentsActivity.gotoRatedMovies(requireContext()) },
                        onFavoritesClick = { UserContentsActivity.gotoFavorites(requireContext()) }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            if (state.isLoggedIn) {
                viewModel.getAccount()
            }
            state.accessToken.consume {
                gotoLink(it)
            }
        }
    }

    override fun gotoLink(value: AccessToken?) {
        value?.let {
            val authenticateUrl = Uri.parse(context?.getString(R.string.authenticate_url))
                .buildUpon()
                .appendPath(it.requestToken)
                .build()
                .toString()
            val directions = SettingsFragmentDirections.navigateToLogin(authenticateUrl)
            findNavController().navigate(directions)
        }
    }
}

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onLoginClick: () -> Unit,
    onWatchListClick: () -> Unit,
    onRatedClick: () -> Unit,
    onFavoritesClick: () -> Unit
) {
    LifecycleResumeEffect {
        viewModel.isLoggedIn()

        onPauseOrDispose {
        }
    }

    val viewState by viewModel.viewState.observeAsState()
    viewState?.apply {
        Surface {
            LazyColumn {
                if (isLoggedIn) {
                    item {
                        SettingItem(
                            title = stringResource(id = R.string.settings_username),
                            summary = account?.username ?: String()
                        ) {}
                    }

                    item {
                        SettingItem(title = stringResource(id = R.string.settings_favorites)) {
                            onFavoritesClick()
                        }
                    }

                    item {
                        SettingItem(title = stringResource(id = R.string.settings_watchlist)) {
                            onWatchListClick()
                        }
                    }

                    item {
                        SettingItem(title = stringResource(id = R.string.settings_rated)) {
                            onRatedClick()
                        }
                    }
                }

                item {
                    val title =
                        if (isLoggedIn) stringResource(R.string.settings_logout) else stringResource(
                            R.string.settings_login
                        )
                    SettingItem(title = title) {
                        onLoginClick()
                    }
                }

                item {
                    //        val appVersion = findPreference(getString(R.string.pref_app_version))
//        val summary = SpannableString("${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
//        summary.setSpan(
//            ForegroundColorSpan(
//                ContextCompat.getColor(
//                    requireContext(),
//                    R.color.color_accent
//                )
//            ), 0, summary.length, 0
//        )
//        appVersion?.summary = summary
                    SettingItem(title = stringResource(id = R.string.settings_app_version)) {}
                }
            }
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    summary: String = String(),
    onItemClick: () -> Unit
) {
    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()
        .clickable { onItemClick() }) {
        Text(
            title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
        )
        if (summary.isNotBlank()) {
            Text(
                summary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}