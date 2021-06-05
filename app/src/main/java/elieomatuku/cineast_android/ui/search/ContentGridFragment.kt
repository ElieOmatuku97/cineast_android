package elieomatuku.cineast_android.ui.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.business.service.ContentService
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.ui.contents.ContentsAdapter
import elieomatuku.cineast_android.ui.details.people.PeopleActivity
import elieomatuku.cineast_android.ui.fragment.BaseFragment
import elieomatuku.cineast_android.ui.search.movie.MoviesGridPresenter
import elieomatuku.cineast_android.utils.Constants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_search.*
import org.kodein.di.generic.instance
import timber.log.Timber

/**
 * Created by elieomatuku on 2021-05-30
 */

class ContentGridFragment<VM : ContentViewModel>(private val viewModelClass: Class<VM>, private val contentActivityClass: Class<Activity>) : BaseFragment() {
    companion object {
        fun newMoviesGridFragment(): ContentGridFragment<ContentViewModel> {

        }

        fun newPeopleGridFragment(): ContentGridFragment<ContentViewModel> {
            return ContentGridFragment(, PeopleActivity::class.java)
        }
    }

    private val contentSelectPublisher: PublishSubject<Content> by lazy {
        PublishSubject.create<Content>()
    }

    private val contentSelectObservable: Observable<Content>
        get() = contentSelectPublisher.hide()

    private val gridView by lazy {
        grid_view
    }

    var gridLayoutManager: GridLayoutManager? = null
    private val adapter: ContentsAdapter by lazy {
        ContentsAdapter(contentSelectPublisher, R.layout.holder_grid_content)
    }

    private lateinit var viewModel: VM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)

        viewModel = ViewModelProvider(this).get<VM>(viewModelClass)

        return rootView
    }

    override fun onResume() {
        super.onResume()

        rxSubs.add(
                contentSelectObservable
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { movie: Content ->
                                    val params = Bundle()
                                    params.putString(Constants.SCREEN_NAME_KEY, MoviesGridPresenter.SCREEN_NAME)
                                    params.putParcelable(MoviesGridPresenter.CONTENT_KEY, movie)
                                    params.putParcelableArrayList(MoviesGridPresenter.MOVIE_GENRES_KEY, genres as ArrayList<out Parcelable>)
                                    gotoContent(params)
                                },
                                { t: Throwable ->
                                    Timber.d("movieSelectObservable failed:$t")
                                }
                        )
        )

    }

    fun populateGridView(content: List<Content>) {
        adapter.contents = content.toMutableList()
        gridView.layoutManager = gridLayoutManager
        adapter.notifyDataSetChanged()
    }

    fun updateErrorView(errorMsg: String?) {
        adapter.errorMessage = errorMsg
        gridView.layoutManager = LinearLayoutManager(activity)
        adapter.notifyDataSetChanged()
    }

    fun gotoContent(params: Bundle) {
        val intent = Intent(activity, contentActivityClass)
        intent.putExtras(params)
        activity?.startActivity(intent)
    }
}

abstract class ContentViewModel : ViewModel() {

    protected val contentService: ContentService by App.kodein.instance()

    abstract fun getContent(): LiveData<Content>
}
