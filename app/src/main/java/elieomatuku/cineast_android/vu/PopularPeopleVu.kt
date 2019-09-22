package elieomatuku.cineast_android.vu

import android.app.Activity
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.PopularPeopleItemAdapter
import elieomatuku.cineast_android.business.model.data.People
import elieomatuku.cineast_android.business.model.data.Person
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_search.view.*



class PopularPeopleVu (inflater: LayoutInflater,
                       activity: Activity,
                       fragmentWrapper: FragmentWrapper?,
                       parentView: ViewGroup?) : BaseVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView) {

    override fun getRootViewLayoutId() = R.layout.fragment_search


    private val gridView by lazy {
        rootView.grid_view
    }

    private val GRIDVIEW_NUMBER_OF_COLUMNS = 2

    var gridLayoutManager: GridLayoutManager? = null
    var peopleAdapter : PopularPeopleItemAdapter? =  null

    private val peopleSelectPublisher: PublishSubject<Person> by lazy {
        PublishSubject.create<Person>()
    }

    val peopleSelectObservable: Observable<Person>
        get() = peopleSelectPublisher.hide()


    override fun onCreate() {
        super.onCreate()

        gridLayoutManager = GridLayoutManager(this.fragmentWrapper?.support?.context, GRIDVIEW_NUMBER_OF_COLUMNS)
        gridView.layoutManager = gridLayoutManager

    }

    fun updateList (people: List <People>?) {
        if (people != null) {
            peopleAdapter = PopularPeopleItemAdapter(people, peopleSelectPublisher, R.layout.holder_popular_people)
            gridView.adapter = peopleAdapter
            peopleAdapter?.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gridView.adapter = null
        gridView.layoutManager = null
        gridLayoutManager = null
    }
}