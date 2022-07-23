package elieomatuku.cineast_android.details.person

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.PersonDetails
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.databinding.FragmentContentDetailsBinding
import elieomatuku.cineast_android.details.BareOverviewWidget
import elieomatuku.cineast_android.details.movie.MovieFragment
import elieomatuku.cineast_android.utils.Constants
import elieomatuku.cineast_android.utils.ContentUtils
import elieomatuku.cineast_android.utils.DividerItemDecorator
import elieomatuku.cineast_android.utils.UiUtils
import elieomatuku.cineast_android.widgets.MOVIE_GENRES_KEY
import elieomatuku.cineast_android.widgets.MOVIE_KEY
import elieomatuku.cineast_android.widgets.MoviesWidget
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.Observable
import java.io.Serializable

class PersonFragment : BaseFragment() {
    companion object {
        const val OVERVIEW = "overview"
        const val KNOWN_FOR = "known_for"
    }

    private val viewModel: PersonViewModel by viewModel()
    private lateinit var binding: FragmentContentDetailsBinding
    private val args: PersonFragmentArgs by navArgs()

    private val onProfileClickedPicturePublisher: PublishSubject<Int> by lazy {
        PublishSubject.create()
    }

    private val onProfileClickedPictureObservable: Observable<Int>
        get() = onProfileClickedPicturePublisher.hide()


    private val onSegmentedButtonsPublisher: PublishSubject<Pair<String, PersonDetails>> by lazy {
        PublishSubject.create()
    }

    private val onSegmentedButtonsObservable: Observable<Pair<String, PersonDetails>>
        get() = onSegmentedButtonsPublisher.hide()

    val adapter: PersonAdapter by lazy {
        PersonAdapter(onProfileClickedPicturePublisher, onSegmentedButtonsPublisher)
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

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.inflateMenu(R.menu.item_menu)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_share -> {
                    onShareMenuClicked()
                    true
                }
                else -> false
            }
        }

        binding.listViewContainer.adapter = adapter
        binding.listViewContainer.layoutManager = LinearLayoutManager(requireContext())

        val screenName = args.screenName
        binding.toolbar.title = screenName
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
            onProfileClickedPictureObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    navigateToGallery()
                }
        )
    }

    private fun updateActionShare() {
        binding.toolbar.menu?.findItem(R.id.action_share)?.apply {
            isVisible = ContentUtils.supportsShare(viewModel.person?.id)
            UiUtils.tintMenuItem(this, requireContext(), R.color.color_orange_app)
        }
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
                            onItemClick = { content, genres ->
                                val params = Bundle()
                                if (content is Movie) {
                                    params.putString(Constants.SCREEN_NAME_KEY, content.title)
                                }
                                params.putSerializable(MOVIE_KEY, content)
                                params.putSerializable(
                                    MOVIE_GENRES_KEY,
                                    genres as Serializable
                                )
                                gotoMovie(params)
                            },
                            onSeeAllClick = {
                                context?.let {
//                                    ContentsActivity.startActivity(it, movies, R.string.movies)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun gotoMovie(params: Bundle) {
        val intent = Intent(activity, MovieFragment::class.java)
        intent.putExtras(params)
        activity?.startActivity(intent)
    }
}
