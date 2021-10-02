package elieomatuku.cineast_android.ui.details.person

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
import elieomatuku.cineast_android.domain.model.*
import elieomatuku.cineast_android.ui.details.BareOverviewFragment
import elieomatuku.cineast_android.ui.details.MoviesFragment
import elieomatuku.cineast_android.ui.details.gallery.GalleryFragment
import elieomatuku.cineast_android.ui.vu.ToolbarVu
import elieomatuku.cineast_android.utils.DividerItemDecorator
import elieomatuku.cineast_android.utils.UiUtils
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_content_details.view.*
import java.util.ArrayList

class PersonVu(
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
        return R.layout.activity_content_details
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
        null

    }

    private val onSegmentedButtonsPublisher: PublishSubject<Pair<String, PersonDetails>> by lazy {
        PublishSubject.create<Pair<String, PersonDetails>>()
    }

    val onSegmentedButtonsObservable: Observable<Pair<String, PersonDetails>>
        get() = onSegmentedButtonsPublisher.hide()

    val adapter: PersonAdapter by lazy {
        PersonAdapter(onProfileClickedPicturePublisher, onSegmentedButtonsPublisher)
    }

    private var knownFor: List<Movie> = listOf()

    override fun onCreate() {
        super.onCreate()

        if (toolbar != null) {
            UiUtils.initToolbar(activity as AppCompatActivity, toolbar)
        }

        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(activity)
    }

    fun updateVu(
        personDetails: PersonDetails?,
        screenName: String?,
        knownFor: List<Movie>?
    ) {
        if (personDetails != null && screenName != null && knownFor != null) {
            toolbar?.title = screenName

            adapter.personDetails = personDetails
            this.knownFor = knownFor.toMutableList()

            val dividerItemDecoration = DividerItemDecorator(
                ResourcesCompat.getDrawable(
                    activity.resources,
                    R.drawable.item_decoration,
                    activity.theme
                )
            )
            listView.addItemDecoration(dividerItemDecoration)

            adapter.notifyDataSetChanged()
            initDetailsFragment(personDetails)
        }
    }

    private fun initDetailsFragment(personDetails: PersonDetails) {
        (activity as FragmentActivity).supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            BareOverviewFragment.newInstance(
                activity.getString(R.string.biography),
                personDetails.biography
            )
        ).commit()
    }

    fun goToGallery(posters: List<Image>?) {
        val galleryFragment = GalleryFragment.newInstance()

        val args = Bundle()
        args.putParcelableArrayList(GalleryFragment.POSTERS, posters as ArrayList<out Parcelable>)
        galleryFragment.arguments = args

        (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
            .add(android.R.id.content, galleryFragment, null).addToBackStack(null).commit()
    }

    fun updateErrorView(errorMsg: String?) {
        adapter.errorMessage = errorMsg
        adapter.notifyDataSetChanged()
    }

    fun gotoTab(displayAndPersonDetails: Pair<String, PersonDetails>) {
        val fragment = when (displayAndPersonDetails.first) {
            OVERVIEW -> {
                BareOverviewFragment.newInstance(
                    activity.getString(R.string.biography),
                    displayAndPersonDetails.second.biography
                )
            }
            KNOWN_FOR -> {
                MoviesFragment.newInstance(
                    knownFor,
                    activity.getString(R.string.cast),
                    displayAndPersonDetails.second.name
                )
            }
            else -> null
        }

        if (fragment != null) {
            (activity as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment).commit()
        }
    }
}
