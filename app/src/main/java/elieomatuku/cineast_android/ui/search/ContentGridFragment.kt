package elieomatuku.cineast_android.ui.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import elieomatuku.cineast_android.ui.adapter.ContentAdapter
import elieomatuku.cineast_android.ui.details.people.PeopleActivity
import elieomatuku.cineast_android.ui.fragment.BaseFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_search.*
import org.kodein.di.generic.instance


/**
 * Created by elieomatuku on 2021-05-30
 */

class ContentGridFragment<VM : ContentViewModel>(private val viewModelClass: Class<VM>, private val contentActivityClass: Class<Activity>) : BaseFragment() {
    companion object {
//        fun newMoviesGridFragment(): ContentGridFragment<ContentViewModel> {
//
//        }
//
//        fun newPeopleGridFragment(): ContentGridFragment<ContentViewModel> {
//            return ContentGridFragment(, PeopleActivity::class.java)
//        }

    }

    private val contentSelectPublisher: PublishSubject<Content> by lazy {
        PublishSubject.create<Content>()
    }

    val contentSelectObservable: Observable<Content>
        get() = contentSelectPublisher.hide()

    private val gridView by lazy {
        grid_view
    }

    var gridLayoutManager: GridLayoutManager? = null
    private val adapter: ContentAdapter by lazy {
        ContentAdapter(contentSelectPublisher, R.layout.holder_grid_content)
    }

    private lateinit var viewModel: VM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)

        viewModel = ViewModelProvider(this).get<VM>(viewModelClass)

        return rootView
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


