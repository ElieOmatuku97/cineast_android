package elieomatuku.cineast_android.ui.content_list

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.core.model.Person
import elieomatuku.cineast_android.ui.adapter.ContentAdapter
import elieomatuku.cineast_android.ui.adapter.PeopleAdapter
import elieomatuku.cineast_android.ui.vu.ListVu
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ContentListVu(inflater: LayoutInflater, activity: Activity, fragmentWrapper: FragmentWrapper?, parentView: ViewGroup?) :
    ListVu(inflater, activity = activity, fragmentWrapper = fragmentWrapper, parentView = parentView) {

    override val adapter: RecyclerView.Adapter<*>
        get() = contentAdapter

    private val personSelectPublisher: PublishSubject<Person> by lazy {
        PublishSubject.create<Person>()
    }

    val personSelectObservable: Observable<Person>
        get() = personSelectPublisher.hide()

    private val contentAdapter: ContentAdapter by lazy {
        ContentAdapter(movieSelectPublisher, R.layout.holder_movie_list)
    }

    private val popularPeopleItemAdapter: PeopleAdapter by lazy {
        PeopleAdapter(personSelectPublisher, R.layout.holder_people_list)
    }

    override fun setUpListView(contents: List<Content>) {
        if (areWidgetsMovies(contents)) {
            contentAdapter.contents = contents.toMutableList()
            listView.adapter = contentAdapter
            contentAdapter.notifyDataSetChanged()
        } else {
            popularPeopleItemAdapter.people = contents as MutableList<Person>
            listView.adapter = popularPeopleItemAdapter
            popularPeopleItemAdapter.notifyDataSetChanged()
        }
    }

    private fun areWidgetsMovies(contents: List<Content?>): Boolean {
        return (contents[FIRST_WIDGET_TYPE_OCCURENCE] != null) && (contents[FIRST_WIDGET_TYPE_OCCURENCE] is Movie)
    }
}
