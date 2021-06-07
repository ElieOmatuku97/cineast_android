package elieomatuku.cineast_android.ui.details.people

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.KnownFor
import elieomatuku.cineast_android.core.model.Person
import elieomatuku.cineast_android.core.model.PersonalityDetails
import elieomatuku.cineast_android.core.model.Poster
import elieomatuku.cineast_android.ui.details.MoviesFragment
import elieomatuku.cineast_android.ui.details.gallery.GalleryFragment
import elieomatuku.cineast_android.ui.details.people.overview.BareOverviewFragment
import elieomatuku.cineast_android.ui.vu.ToolbarVu
import elieomatuku.cineast_android.utils.DividerItemDecorator
import elieomatuku.cineast_android.utils.UiUtils
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.vu_movie.view.*
import java.util.ArrayList

class PeopleVu(
    inflater: LayoutInflater,
    activity: Activity,
    fragmentWrapper: FragmentWrapper?,
    parentView: ViewGroup?
) : ToolbarVu(
    inflater,
    activity = activity,
    fragmentWrapper = fragmentWrapper,
    parentView = parentView
) {

    companion object {
        const val OVERVIEW = "overview"
        const val KNOWN_FOR = "known_for"
    }

    override val toolbar: Toolbar?
        get() = rootView.toolbar

    override fun getRootViewLayoutId(): Int {
        return R.layout.vu_movie
    }

    private val listView: RecyclerView by lazy {
        rootView.list_view_container
    }

    private val onProfileClickedPicturePublisher: PublishSubject<Int> by lazy {
        PublishSubject.create<Int>()
    }

    val onProfileClickedPictureObservable: Observable<Int>
        get() = onProfileClickedPicturePublisher.hide()

    val personPresentedPublisher: PublishSubject<Person>? by lazy {
        if (activity is PeopleActivity) {
            activity.personPresentedPublisher
        } else {
            null
        }
    }

    private val onSegmentedButtonsPublisher: PublishSubject<Pair<String, PersonalityDetails>> by lazy {
        PublishSubject.create<Pair<String, PersonalityDetails>>()
    }

    val onSegmentedButtonsObservable: Observable<Pair<String, PersonalityDetails>>
        get() = onSegmentedButtonsPublisher.hide()

    val adapter: PeopleSummaryAdapter by lazy {
        PeopleSummaryAdapter(onProfileClickedPicturePublisher, onSegmentedButtonsPublisher)
    }

    private var knownFor: List<KnownFor> = listOf()

    override fun onCreate() {
        super.onCreate()

        if (toolbar != null) {
            UiUtils.initToolbar(activity as AppCompatActivity, toolbar)
        }

        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(activity)
    }

    fun updateVu(personalityDetails: PersonalityDetails?, screenName: String?, knownFor: List<KnownFor>?) {
        if (personalityDetails != null && screenName != null && knownFor != null) {
            toolbar?.title = screenName

            adapter.personalityDetails = personalityDetails
            this.knownFor = knownFor.toMutableList()

            val dividerItemDecoration = DividerItemDecorator(ResourcesCompat.getDrawable(activity.resources, R.drawable.item_decoration, activity.theme))
            listView.addItemDecoration(dividerItemDecoration)

            adapter.notifyDataSetChanged()
            initDetailsFragment(personalityDetails)
        }
    }

    private fun initDetailsFragment(personalityDetails: PersonalityDetails) {
        (activity as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, BareOverviewFragment.newInstance(personalityDetails)).commit()
    }

    fun goToGallery(posters: List<Poster>?) {
        val galleryFragment = GalleryFragment.newInstance()

        val args = Bundle()
        args.putParcelableArrayList(GalleryFragment.POSTERS, posters as ArrayList<out Parcelable>)
        galleryFragment.arguments = args

        (activity as AppCompatActivity).supportFragmentManager.beginTransaction().add(android.R.id.content, galleryFragment, null).addToBackStack(null).commit()
    }

    fun updateErrorView(errorMsg: String?) {
        adapter.errorMessage = errorMsg
        adapter.notifyDataSetChanged()
    }

    fun gotoTab(displayAndPersonalityDetails: Pair<String, PersonalityDetails>) {
        val fragment = when (displayAndPersonalityDetails.first) {
            OVERVIEW -> {
                BareOverviewFragment.newInstance(displayAndPersonalityDetails.second)
            }
            KNOWN_FOR -> {
                MoviesFragment.newInstance(knownFor.mapNotNull { it.toMovie() }, activity.getString(R.string.cast), displayAndPersonalityDetails.second.name)
            }
            else -> null
        }

        if (fragment != null) {
            (activity as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        }
    }
}
