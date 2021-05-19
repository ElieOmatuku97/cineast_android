package elieomatuku.cineast_android.ui.search.people

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Person
import elieomatuku.cineast_android.core.model.Personality
import elieomatuku.cineast_android.ui.common_adapter.PeopleLisAdapter
import elieomatuku.cineast_android.ui.common_vu.BaseVu
import elieomatuku.cineast_android.ui.search.SearchVu.Companion.GRID_VIEW_NUMBER_OF_COLUMNS
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_search.view.*

class PeopleSearchVu(
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

    private val gridView by lazy {
        rootView.grid_view
    }

    var gridLayoutManager: GridLayoutManager? = null
    private val adapter: PeopleLisAdapter by lazy {
        PeopleLisAdapter(peopleSelectPublisher, R.layout.holder_popular_people)
    }

    private val peopleSelectPublisher: PublishSubject<Person> by lazy {
        PublishSubject.create<Person>()
    }

    val peopleSelectObservable: Observable<Person>
        get() = peopleSelectPublisher.hide()

    override fun onCreate() {
        super.onCreate()

        gridView.adapter = adapter
        gridLayoutManager = GridLayoutManager(this.fragmentWrapper?.support?.context, GRID_VIEW_NUMBER_OF_COLUMNS)
        gridView.layoutManager = gridLayoutManager
    }

    fun updateList(people: List<Personality>?) {
        if (people != null) {
            adapter.popularPersonalities = people.toMutableList()
            gridView.layoutManager = gridLayoutManager
            adapter.notifyDataSetChanged()
        }
    }

    fun updateErrorView(errorMsg: String?) {
        adapter.errorMessage = errorMsg
        gridView.layoutManager = LinearLayoutManager(activity)
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        gridView.adapter = null
        gridView.layoutManager = null
        gridLayoutManager = null
    }
}
