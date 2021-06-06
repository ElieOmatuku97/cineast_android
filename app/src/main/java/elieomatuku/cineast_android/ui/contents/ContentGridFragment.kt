package elieomatuku.cineast_android.ui.contents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.ui.fragment.BaseFragment
import elieomatuku.cineast_android.ui.search.SearchVu.Companion.GRID_VIEW_NUMBER_OF_COLUMNS
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_search.*
import timber.log.Timber

/**
 * Created by elieomatuku on 2021-05-30
 */

abstract class ContentGridFragment<VM : ContentGridViewModel>(private val viewModelClass: Class<VM>) : BaseFragment() {
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

    protected lateinit var viewModel: VM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)

//        vu.showLoading()

        viewModel = ViewModelProvider(this).get<VM>(viewModelClass)


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.contentLiveData.observe(viewLifecycleOwner, Observer { res ->

            updateView(res)
        })
        viewModel.errorMsgLiveData.observe(this.viewLifecycleOwner, Observer { res -> updateErrorView(res) })
    }

    override fun onResume() {
        super.onResume()

        rxSubs.add(
                contentSelectObservable
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { content: Content ->
                                    Timber.d("contentSelectObservable")
                                    gotoContent(content)
                                },
                                { t: Throwable ->
                                    Timber.d("movieSelectObservable failed:$t")
                                }
                        )
        )
    }

    private fun updateView(content: List<Content>) {
        gridView.adapter = adapter
//        vu.hideLoading()
        adapter.contents = content.toMutableList()
        gridView.layoutManager = GridLayoutManager(context, GRID_VIEW_NUMBER_OF_COLUMNS)
        adapter.notifyDataSetChanged()
    }

    private fun updateErrorView(errorMsg: String?) {
//        vu.hideLoading()
        adapter.errorMessage = errorMsg
        gridView.layoutManager = LinearLayoutManager(activity)
        adapter.notifyDataSetChanged()
    }

    protected abstract fun gotoContent(content: Content)
}


