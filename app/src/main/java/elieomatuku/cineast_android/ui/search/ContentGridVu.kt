package elieomatuku.cineast_android.ui.search

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.ui.adapter.ContentAdapter
import elieomatuku.cineast_android.ui.search.SearchVu.Companion.GRID_VIEW_NUMBER_OF_COLUMNS
import elieomatuku.cineast_android.ui.vu.BaseVu
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_search.view.*

class ContentGridVu(
    inflater: LayoutInflater,
    activity: Activity,
    fragmentWrapper: FragmentWrapper?,
    parentView: ViewGroup?
) : BaseVu(
    inflater,
    activity = activity,
    fragmentWrapper = fragmentWrapper,
    parentView = parentView
) {

    override fun getRootViewLayoutId() = R.layout.fragment_search

    private val contentSelectPublisher: PublishSubject<Content> by lazy {
        PublishSubject.create<Content>()
    }

    val contentSelectObservable: Observable<Content>
        get() = contentSelectPublisher.hide()

    private val gridView by lazy {
        rootView.grid_view
    }

    var gridLayoutManager: GridLayoutManager? = null
    private val adapter: ContentAdapter by lazy {
        ContentAdapter(contentSelectPublisher, R.layout.holder_grid_content)
    }

    override fun onCreate() {
        super.onCreate()
        gridLayoutManager = GridLayoutManager(this.fragmentWrapper?.support?.context, GRID_VIEW_NUMBER_OF_COLUMNS)
        gridView.adapter = adapter
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
}

