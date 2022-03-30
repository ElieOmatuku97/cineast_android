package elieomatuku.cineast_android.details.person

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Image
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.domain.model.PersonDetails
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.databinding.FragmentContentDetailsBinding
import elieomatuku.cineast_android.details.BareOverviewFragment
import elieomatuku.cineast_android.details.MoviesFragment
import elieomatuku.cineast_android.details.gallery.GalleryFragment
import elieomatuku.cineast_android.utils.DividerItemDecorator
import elieomatuku.cineast_android.utils.ContentUtils
import elieomatuku.cineast_android.utils.UiUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import java.io.Serializable
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_content_details.*

class PersonFragment : BaseFragment() {
    companion object {
        const val OVERVIEW = "overview"
        const val KNOWN_FOR = "known_for"
        const val PEOPLE_KEY = "peopleApi"
    }

    private val viewModel: PersonViewModel by viewModel<PersonViewModel>()
    private lateinit var binding: FragmentContentDetailsBinding
    private val args: PersonFragmentArgs by navArgs()

    private val onProfileClickedPicturePublisher: PublishSubject<Int> by lazy {
        PublishSubject.create<Int>()
    }

    private val onProfileClickedPictureObservable: Observable<Int>
        get() = onProfileClickedPicturePublisher.hide()


    private val onSegmentedButtonsPublisher: PublishSubject<Pair<String, PersonDetails>> by lazy {
        PublishSubject.create<Pair<String, PersonDetails>>()
    }

    private val onSegmentedButtonsObservable: Observable<Pair<String, PersonDetails>>
        get() = onSegmentedButtonsPublisher.hide()

    val adapter: PersonAdapter by lazy {
        PersonAdapter(onProfileClickedPicturePublisher, onSegmentedButtonsPublisher)
    }

    private val listView: RecyclerView by lazy {
        list_view_container
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

        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(requireContext())

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
                    goToGallery(viewModel.posters)
                }
        )
    }

//    override fun onSupportNavigateUp(): Boolean {
//        if (!super.onSupportNavigateUp()) {
//            onBackPressed()
//        }
//
//        return true
//    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        this.menuInflater.inflate(R.menu.item_menu, menu)
//        if (menu != null) {
//            menu.findItem(R.id.action_share)?.let {
//                val colorRes = R.color.color_orange_app
//                UiUtils.tintMenuItem(it, this, colorRes)
//            }
//        }
//        return true
//    }

//    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
//        menu?.findItem(R.id.action_share)?.let {
//            it.isVisible = ContentUtils.supportsShare(viewModel.person?.id)
//        }
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item?.itemId) {
//            android.R.id.home -> onSupportNavigateUp()
//
//            R.id.action_share -> {
//                onShareMenuClicked(this)
//            }
//
//            else -> super.onOptionsItemSelected(item)
//        }
//        return true
//    }

    private fun onShareMenuClicked(activity: Activity) {
        val path = "person"
        val shareIntent: Intent? =
            UiUtils.getShareIntent(viewModel.person?.name, viewModel.person?.id, path)

        // Make sure there is an activity that supports the intent
        if (shareIntent?.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(
                Intent.createChooser(
                    shareIntent,
                    activity.getString(R.string.share_title)
                )
            )
        }
    }

    private fun updateView(
        person: PersonDetails?,
        knownFor: List<Movie>?
    ) {
        if (person != null && knownFor != null) {
//            invalidateOptionsMenu()
            adapter.personDetails = person

            val dividerItemDecoration = DividerItemDecorator(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.item_decoration,
                    requireContext().theme
                )
            )
            listView.addItemDecoration(dividerItemDecoration)

            adapter.notifyDataSetChanged()
            initDetailsFragment(person)
        }
    }

    private fun initDetailsFragment(personDetails: PersonDetails) {
        childFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            BareOverviewFragment.newInstance(
                getString(R.string.biography),
                personDetails.biography
            )
        ).commit()
    }

    private fun goToGallery(posters: List<Image>) {
        val galleryFragment = GalleryFragment.newInstance()

        val args = Bundle()
        args.putSerializable(GalleryFragment.POSTERS, posters as Serializable)
        galleryFragment.arguments = args

        childFragmentManager.beginTransaction()
            .add(android.R.id.content, galleryFragment, null).addToBackStack(null).commit()
    }

    private fun updateErrorView(errorMsg: String?) {
        adapter.errorMessage = errorMsg
        adapter.notifyDataSetChanged()
    }

    private fun gotoTab(
        displayAndPersonDetails: Pair<String, PersonDetails>
    ) {
        val fragment = when (displayAndPersonDetails.first) {
            OVERVIEW -> {
                BareOverviewFragment.newInstance(
                    getString(R.string.biography),
                    displayAndPersonDetails.second.biography
                )
            }
            KNOWN_FOR -> {
                MoviesFragment.newInstance(
                    viewModel.knownForMovies,
                    getString(R.string.cast),
                    displayAndPersonDetails.second.name
                )
            }
            else -> null
        }

        if (fragment != null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment).commit()
        }
    }
}
