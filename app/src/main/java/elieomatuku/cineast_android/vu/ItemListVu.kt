package elieomatuku.cineast_android.vu

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.MovieListAdapter
import elieomatuku.cineast_android.adapter.PopularPeopleItemAdapter
import elieomatuku.cineast_android.business.model.data.Movie
import elieomatuku.cineast_android.business.model.data.Person
import elieomatuku.cineast_android.business.model.data.Widget
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


class ItemListVu(inflater: LayoutInflater, activity: Activity, fragmentWrapper: FragmentWrapper?, parentView: ViewGroup?) : ListVu(inflater, activity = activity, fragmentWrapper = fragmentWrapper, parentView = parentView) {


    private val personSelectPublisher: PublishSubject<Person> by lazy {
        PublishSubject.create<Person>()
    }

    val personSelectObservable: Observable<Person>
        get() = personSelectPublisher.hide()

    override fun setUpListView(widgets: List<Widget>) {
        if (areWidgetsMovies(widgets)) {
            adapter = MovieListAdapter(widgets as List<Movie>, movieSelectPublisher, R.layout.holder_movie_list)
            listView.adapter = adapter
            adapter.notifyDataSetChanged()

        } else {
            listView.adapter = PopularPeopleItemAdapter(widgets as List<Person>, personSelectPublisher, R.layout.holder_people_list)
        }
    }


    private fun areWidgetsMovies(widgets: List<Widget?>): Boolean {
        return (widgets[FIRST_WIDGET_TYPE_OCCURENCE] != null) && (widgets[FIRST_WIDGET_TYPE_OCCURENCE] is Movie)
    }
}