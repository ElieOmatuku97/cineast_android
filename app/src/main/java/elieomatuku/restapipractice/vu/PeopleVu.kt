package elieomatuku.restapipractice.vu

import android.app.Activity
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import elieomatuku.restapipractice.R
import elieomatuku.restapipractice.activity.PeopleActivity
import elieomatuku.restapipractice.adapter.PeopleItemAdapter
import elieomatuku.restapipractice.business.business.model.data.*
import elieomatuku.restapipractice.fragment.MovieGalleryFragment
import elieomatuku.restapipractice.fragment.OverviewPeopleFragment
import elieomatuku.restapipractice.presenter.MovieGalleryPresenter
import elieomatuku.restapipractice.utils.UiUtils
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.vu_movie.view.*
import java.util.ArrayList

class PeopleVu(inflater: LayoutInflater,
               activity: Activity,
               fragmentWrapper: FragmentWrapper?,
               parentView: ViewGroup?) : ToolbarVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView) {

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

    override fun onCreate() {
        super.onCreate()

        if (toolbar != null) {
            UiUtils.initToolbar(activity as AppCompatActivity, toolbar)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        listView.adapter = null
        listView.layoutManager = null
    }

    fun updateVu(peopleDetails: PeopleDetails?, screenName: String?, peopleMovies: List<PeopleCast>?) {
        if (peopleDetails != null && screenName != null && peopleMovies != null) {
            toolbar?.title = screenName
            Log.d(PeopleVu::class.java.simpleName, "results: $peopleDetails & screenName: $screenName")
            val peopleItemAdapter = PeopleItemAdapter(peopleDetails, peopleMovies, onProfileClickedPicturePublisher)

            listView.adapter = peopleItemAdapter
            listView.layoutManager = LinearLayoutManager (activity)
            val dividerItemDecoration = getItemDecoration(R.drawable.item_decoration, activity)
            if ( dividerItemDecoration != null)
                listView.addItemDecoration(dividerItemDecoration)
            peopleItemAdapter.notifyDataSetChanged()
            initializeFragmentOnPeopleClicked(peopleDetails.biography)
        }
    }

    private fun getItemDecoration(itemDecorationRes: Int, activity: Activity): RecyclerView.ItemDecoration? {
        val itemDecorationDrawable = ResourcesCompat.getDrawable(activity.resources, itemDecorationRes, activity.theme)
        return if (itemDecorationDrawable != null) {
            DividerItemDecorator(itemDecorationDrawable)
        } else {
            null
        }
    }

    private fun initializeFragmentOnPeopleClicked(peopleBio: String?) {
        if (peopleBio != null) {
            val initialFragmentOnMovieClicked = OverviewPeopleFragment.newInstance(peopleBio)
            (activity as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, initialFragmentOnMovieClicked).commit()
        }
    }

    fun goToGallery(posters: List<Poster>?) {
        val galleryFragment = MovieGalleryFragment.newInstance()
        galleryFragment.arguments = getArgs(posters)
        addFragment(galleryFragment, activity)
    }
    private fun getArgs(posters: List<Poster>?): Bundle {
        val args = Bundle()
        args.putParcelableArrayList(MovieGalleryPresenter.POSTERS, posters as ArrayList<out Parcelable>)
        return args
    }

    private fun addFragment(fragment: Fragment, activity: Activity) {
        val fm = (activity as AppCompatActivity).supportFragmentManager
        if (fragment != null && fm != null) {
            fm.beginTransaction().add(android.R.id.content, fragment, null).addToBackStack(null).commit()
        }

    }

    inner class DividerItemDecorator(private val mDivider: Drawable) : RecyclerView.ItemDecoration() {
        override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
            val dividerLeft = parent.paddingLeft
            val dividerRight = parent.width - parent.paddingRight

            val childCount = parent.childCount
            for (i in 1 until childCount) {
                val child = parent.getChildAt(i)

                val params = child.layoutParams as RecyclerView.LayoutParams

                val dividerTop = child.bottom + params.bottomMargin
                val dividerBottom = dividerTop + mDivider.intrinsicHeight

                mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                mDivider.draw(canvas)
            }
        }
    }
}