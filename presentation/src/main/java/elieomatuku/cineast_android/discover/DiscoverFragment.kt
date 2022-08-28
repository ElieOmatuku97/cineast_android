package elieomatuku.cineast_android.discover

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.*
import elieomatuku.cineast_android.extensions.getWidgets
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.contents.ContentsActivity
import elieomatuku.cineast_android.databinding.FragmentDiscoverBinding
import elieomatuku.cineast_android.extensions.DiscoverWidget
import elieomatuku.cineast_android.extensions.asListOfType
import elieomatuku.cineast_android.fragment.WebViewFragment
import elieomatuku.cineast_android.settings.LoginWebViewFragment
import kotlinx.android.synthetic.main.fragment_discover.*
import elieomatuku.cineast_android.utils.*
import elieomatuku.cineast_android.widgets.MoviesWidget
import elieomatuku.cineast_android.widgets.PeopleWidget

class DiscoverFragment : BaseFragment() {

    private val viewModel: DiscoverViewModel by viewModel()
    private lateinit var binding: FragmentDiscoverBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWithNavController(
            binding.toolbar,
            findNavController(),
            AppBarConfiguration(findNavController().graph)
        )
        binding.toolbar.title = this.getString(R.string.nav_title_discover)

//        binding.recyclerview.adapter = adapter
//        val itemDecoration =
//            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
//        val drawable: Drawable? = ResourcesCompat.getDrawable(
//            resources,
//            R.drawable.item_decoration,
//            activity?.theme
//        )
//        if (drawable != null) {
//            itemDecoration.setDrawable(drawable)
//        }
//        binding.recyclerview.addItemDecoration(itemDecoration)
//        binding.recyclerview.layoutManager = LinearLayoutManager(activity)

        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            if (state.isLoading) {
                showLoading(requireView())
            } else {
                dismissRefreshLayout()
                hideLoading(requireView())
            }

            state.viewError.consume {
                updateErrorView(it.message)
            }
            updateLoginState(state.isLoggedIn)
            updateView(state.discoverContents, state.isLoggedIn)
            state.accessToken.consume {
                gotoWebView(it)
            }
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.getDiscoverContent()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getIsLoggedIn()
    }

    private fun updateView(
        discoverContents: DiscoverContents?,
        isLoggedIn: Boolean
    ) {
        binding.composeView.setContent {
            AppCompatTheme {
                discoverContents?.apply {
                    LazyColumn {
                        items(getWidgets()) { widget ->
                            when (widget) {
                                is DiscoverWidget.Header -> {
                                    LazyRow(
                                        modifier = Modifier
                                            .height(dimensionResource(id = R.dimen.holder_header_item_height))
                                    ) {
                                        items(
                                            widget.value.asListOfType<Movie>() ?: emptyList()
                                        ) { movie ->
                                            HeaderItem(movie = movie) {
                                                gotoMovie(it)
                                            }
                                        }
                                    }
                                }
                                is DiscoverWidget.People -> {
                                    PeopleWidget(
                                        people = widget.value.asListOfType() ?: emptyList(),
                                        sectionTitle = requireContext().getString(widget.titleResources),
                                        onItemClick = {
                                            if (it is Person) {
                                                gotoPerson(it)
                                            }
                                        }
                                    ) {
                                        val pair = Pair(it, widget.titleResources)
                                        ContentsActivity.startActivity(requireContext(), pair.first, pair.second)
                                    }
                                }
                                is DiscoverWidget.Movies -> {
                                    MoviesWidget(
                                        viewModelFactory = viewModelFactory,
                                        movies = widget.value.asListOfType() ?: emptyList(),
                                        sectionTitle = requireContext().getString(widget.titleResources),
                                        onItemClick = { content, _ ->
                                            if (content is Movie) {
                                                gotoMovie(content)
                                            }
                                        },
                                        onSeeAllClick = {
                                            val pair = Pair(it, widget.titleResources)
                                            ContentsActivity.startActivity(requireContext(), pair.first, pair.second)
                                        }
                                    )
                                }
                                is DiscoverWidget.Login -> {
                                    LoginItem(isLoggedIn = isLoggedIn) {
                                        if (!viewModel.isLoggedIn()) {
                                            viewModel.logIn()
                                        } else {
                                            viewModel.logout()
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

//        discoverContents?.let {
//            adapter.filteredContents =
//                discoverContents.getFilteredWidgets()
//            adapter.isLoggedIn = isLoggedIn
//            adapter.notifyDataSetChanged()
//            binding.recyclerview.visibility = View.VISIBLE
//            dismissRefreshLayout()
//        }
    }

    private fun updateErrorView(errorMsg: String?) {
//        adapter.errorMessage = errorMsg
//        adapter.notifyDataSetChanged()
//        binding.recyclerview.visibility = View.VISIBLE
//        dismissRefreshLayout()
    }

    private fun gotoWebView(accessToken: AccessToken?) {
        accessToken?.let {
            val authenticateUrl = Uri.parse(resources.getString(R.string.authenticate_url))
                .buildUpon()
                .appendPath(it.requestToken)
                .build()
                .toString()

            val webViewFragment: WebViewFragment =
                LoginWebViewFragment.newInstance(authenticateUrl)
            val fm = (activity as AppCompatActivity).supportFragmentManager

            fm.beginTransaction().add(android.R.id.content, webViewFragment, null)
                .addToBackStack(null).commit()
        }
    }

    private fun updateLoginState(isLoggedIn: Boolean) {
//        adapter.isLoggedIn = isLoggedIn
//        adapter.notifyDataSetChanged()
    }

    private fun dismissRefreshLayout() {
        refreshLayout.isRefreshing = false
    }

    private fun gotoMovie(movie: Movie) {
        val directions = DiscoverFragmentDirections.navigateToMovieDetail(
            getString(R.string.nav_title_discover),
            movie
        )
        findNavController().navigate(directions)
    }

    private fun gotoPerson(person: Person) {
        val directions = DiscoverFragmentDirections.navigateToPersonDetail(
            getString(R.string.nav_title_discover),
            person
        )
        findNavController().navigate(directions)
    }
}
