package elieomatuku.cineast_android.details.person

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.PersonDetails
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.contents.ContentsActivity
import elieomatuku.cineast_android.databinding.FragmentContentDetailsBinding
import elieomatuku.cineast_android.details.BareOverviewWidget
import elieomatuku.cineast_android.details.movie.MovieFragmentDirections
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.utils.ContentUtils
import elieomatuku.cineast_android.utils.DividerItemDecorator
import elieomatuku.cineast_android.utils.UiUtils
import elieomatuku.cineast_android.widgets.MoviesWidget
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.Observable

class PersonFragment : BaseFragment() {
    companion object {
        const val OVERVIEW = "overview"
        const val KNOWN_FOR = "known_for"
    }

    private val viewModel: PersonViewModel by viewModel()
    private lateinit var binding: FragmentContentDetailsBinding
    private lateinit var menuHost: MenuHost
    private val args: PersonFragmentArgs by navArgs()

    private val onProfilePictureClickedPublisher: PublishSubject<Int> by lazy {
        PublishSubject.create()
    }

    private val onProfilePictureClickedObservable: Observable<Int>
        get() = onProfilePictureClickedPublisher.hide()

    private val onProfileLinkClickedPublisher: PublishSubject<String> by lazy {
        PublishSubject.create()
    }

    private val onProfileLinkClickedObservable: Observable<String>
        get() = onProfileLinkClickedPublisher.hide()

    private val onSegmentedButtonsPublisher: PublishSubject<Pair<String, PersonDetails>> by lazy {
        PublishSubject.create()
    }

    private val onSegmentedButtonsObservable: Observable<Pair<String, PersonDetails>>
        get() = onSegmentedButtonsPublisher.hide()

    private val adapter: PersonAdapter by lazy {
        PersonAdapter(
            onProfilePictureClickedPublisher,
            onSegmentedButtonsPublisher,
            onProfileLinkClickedPublisher
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.item_menu, menu)
                menu.findItem(R.id.action_share)?.apply {
                    isVisible = ContentUtils.supportsShare(viewModel.person?.id)
                    UiUtils.tintMenuItem(this, requireContext(), R.color.color_orange_app)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_share -> {
                        onShareMenuClicked()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.listViewContainer.adapter = adapter
        binding.listViewContainer.layoutManager = LinearLayoutManager(requireContext())

        val screenName = args.screenName
//        binding.toolbar.title = screenName
        val person = args.person
        viewModel.getPersonDetails(person)
        viewModel.getKnownForMovies(person)
        viewModel.getImages(person)

        viewModel.viewState.observe(viewLifecycleOwner) {
            if (it.isLoading) {
                showLoading(view)
            } else {
                hideLoading(view)
            }

            updateActionShare()
            updateView(it.person, it.knownFor)

            it.viewError?.consume()?.apply {
                updateErrorView(this.message)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        rxSubs.add(
            onSegmentedButtonsObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    gotoTab(it)
                }
        )

        rxSubs.add(
            onProfilePictureClickedObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    navigateToGallery()
                }
        )

        rxSubs.add(
            onProfileLinkClickedObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    goToWebsite(it)
                }
        )
    }

    private fun updateActionShare() {
        menuHost.invalidateMenu()
    }

    private fun onShareMenuClicked() {
        val path = "person"
        val shareIntent: Intent? =
            UiUtils.getShareIntent(viewModel.person?.name, viewModel.person?.id, path)

        // Make sure there is an activity that supports the intent
        if (shareIntent?.resolveActivity(requireContext().packageManager) != null) {
            startActivity(
                Intent.createChooser(
                    shareIntent,
                    getString(R.string.share_title)
                )
            )
        }
    }

    private fun updateView(
        person: PersonDetails?,
        knownFor: List<Movie>?
    ) {
        if (person != null && knownFor != null) {
            adapter.personDetails = person

            val dividerItemDecoration = DividerItemDecorator(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.item_decoration,
                    requireContext().theme
                )
            )
            binding.listViewContainer.addItemDecoration(dividerItemDecoration)

            adapter.notifyDataSetChanged()
            initDetailsFragment(person)
        }
    }

    private fun initDetailsFragment(personDetails: PersonDetails) {
        binding.composeviewContainer.setContent {
            AppCompatTheme {
                BareOverviewWidget(
                    title = getString(R.string.biography),
                    overview = personDetails.biography ?: String()
                )
            }
        }
    }

    private fun navigateToGallery() {
        val directions =
            PersonFragmentDirections.navigateToGallery(
                viewModel.posters
                    .map { it.filePath }
                    .toTypedArray()
            )
        findNavController().navigate(directions)
    }

    private fun updateErrorView(errorMsg: String?) {
        adapter.errorMessage = errorMsg
        adapter.notifyDataSetChanged()
    }

    private fun gotoTab(
        displayAndPersonDetails: Pair<String, PersonDetails>
    ) {
        binding.composeviewContainer.setContent {
            AppCompatTheme {
                when (displayAndPersonDetails.first) {
                    OVERVIEW -> {
                        BareOverviewWidget(
                            title = getString(R.string.biography),
                            overview = displayAndPersonDetails.second.biography ?: String()
                        )
                    }
                    KNOWN_FOR -> {
                        MoviesWidget(
                            viewModelFactory = viewModelFactory,
                            movies = viewModel.knownForMovies,
                            sectionTitle = getString(R.string.cast),
                            onItemClick = { content, _ ->
                                gotoMovie(content)
                            },
                            onSeeAllClick = { movies ->
                                context?.let {
                                    ContentsActivity.startActivity(it, movies, R.string.movies)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun gotoMovie(content: Content) {
        if (content is Movie) {
            val directions = MovieFragmentDirections.navigateToMovieDetail(
                content.title ?: String(),
                content
            )
            findNavController().navigate(directions)
        }
    }

    private fun goToWebsite(url: String) {
        val directions = PersonFragmentDirections.navigateToWebsite(url)
        findNavController().navigate(directions)
    }
}
