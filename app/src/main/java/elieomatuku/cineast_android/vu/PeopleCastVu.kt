package elieomatuku.cineast_android.vu

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.PeopleCastAdapter
import elieomatuku.cineast_android.business.business.model.data.PeopleCast
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_movie.view.*

class PeopleCastVu (inflater: LayoutInflater,
                    activity: Activity,
                    fragmentWrapper: FragmentWrapper?,
                    parentView: ViewGroup?) : BaseVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView) {

    override fun getRootViewLayoutId(): Int {
        return R.layout.holder_movie
    }

    private val itemSelectPublisher: PublishSubject<Int> by lazy {
        PublishSubject.create<Int>()
    }

    val itemSelectObservable: Observable<Int>
        get() = itemSelectPublisher.hide()


    private val listView : RecyclerView by lazy {
        rootView.recyclerview_popular_movie
    }

    private val sectionTitleView: TextView by lazy {
        rootView.section_title
    }

    override fun onCreate() {
        super.onCreate()
        sectionTitleView.text = activity.getText(R.string.cast)
    }


    fun updateVu( peopleCast: List<PeopleCast>){
        val adapter = PeopleCastAdapter(peopleCast , itemSelectPublisher)
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        adapter.notifyDataSetChanged()
    }


    override fun onDestroy() {
        super.onDestroy()
        listView.adapter = null
        listView.layoutManager = null
    }
}