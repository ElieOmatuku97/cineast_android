package elieomatuku.cineast_android.contents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.search.SearchFragment.Companion.GRID_VIEW_NUMBER_OF_COLUMNS
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * Created by elieomatuku on 2021-05-30
 */

abstract class ContentGridFragment :
    BaseFragment() {
    private val contentSelectPublisher: PublishSubject<Content> by lazy {
        PublishSubject.create<Content>()
    }

    private val contentSelectObservable: Observable<Content>
        get() = contentSelectPublisher.hide()

    private val gridView by lazy {
        grid_view
    }

    private var gridLayoutManager: GridLayoutManager? = null
    private val adapter: ContentsAdapter by lazy {
        ContentsAdapter(contentSelectPublisher, R.layout.holder_grid_content)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        vu.showLoading()

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onResume() {
        super.onResume()

        rxSubs.add(
            contentSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { content: Content ->
                        gotoContent(content)
                    },
                    { t: Throwable ->
                    }
                )
        )
    }

    protected fun updateView(content: List<Content>) {
        gridView.adapter = adapter
//        vu.hideLoading()
        adapter.contents = content.toMutableList()
        gridView.layoutManager = GridLayoutManager(context, GRID_VIEW_NUMBER_OF_COLUMNS)
        adapter.notifyDataSetChanged()
    }

    protected fun updateErrorView(errorMsg: String?) {
//        vu.hideLoading()
        adapter.errorMessage = errorMsg
        gridView.layoutManager = LinearLayoutManager(activity)
        adapter.notifyDataSetChanged()
    }

    protected abstract fun gotoContent(content: Content)
}
