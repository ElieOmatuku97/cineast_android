package elieomatuku.cineast_android.vu

import android.app.Activity
import android.graphics.Canvas
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.LayoutInflater
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.MovieItemAdapter
import kotlinx.android.synthetic.main.vu_movie.view.*
import android.support.v7.widget.RecyclerView
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.res.ResourcesCompat
import elieomatuku.cineast_android.fragment.MovieGalleryFragment
import elieomatuku.cineast_android.fragment.OverviewFragment
import elieomatuku.cineast_android.activity.MovieActivity
import elieomatuku.cineast_android.business.business.model.data.*
import elieomatuku.cineast_android.presenter.MovieGalleryPresenter
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.ArrayList


class MovieVu(inflater: LayoutInflater,
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

    val moviePresentedPublisher: PublishSubject<Movie>? by lazy {
        if (activity is MovieActivity) {
            activity.moviePresentedPublisher
        } else {
            null
        }
    }

    fun updateVu(movieInfo: MovieInfo) {
        toolbar?.title = movieInfo.screenName
        updateListView(movieInfo)
        initializeFragmentOnMovieClicked(movieInfo)
    }

    private fun updateListView(movieInfo: MovieInfo) {
        val movieItemAdapter = MovieItemAdapter(movieInfo, onProfileClickedPicturePublisher)
        configureListView(movieItemAdapter, listView, getItemDecoration(R.drawable.item_decoration, activity))
    }

    private fun initializeFragmentOnMovieClicked(movieInfo: MovieInfo) {
        val initialFragmentOnMovieClicked = OverviewFragment.newInstance(movieInfo.movie, movieInfo.trailers, movieInfo.movieDetails)
        replaceFragment(initialFragmentOnMovieClicked)
    }

    private fun getItemDecoration(itemDecorationRes: Int, activity: Activity): RecyclerView.ItemDecoration? {
        val itemDecorationDrawable = ResourcesCompat.getDrawable(activity.resources, itemDecorationRes, activity.theme)
        return if (itemDecorationDrawable != null) {
            DividerItemDecorator(itemDecorationDrawable)
        } else {
            null
        }
    }

    private fun configureListView(movieItemAdapter: MovieItemAdapter, listView: RecyclerView, dividerItemDecoration: RecyclerView.ItemDecoration?){
        listView.adapter = movieItemAdapter
        listView.layoutManager = LinearLayoutManager (activity)
        if (dividerItemDecoration != null)
            listView.addItemDecoration(dividerItemDecoration)
        movieItemAdapter.notifyDataSetChanged()
    }

    private fun replaceFragment(fragment: Fragment) {
        (activity as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
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

    private fun addFragment(fragment: Fragment?, activity: Activity) {
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