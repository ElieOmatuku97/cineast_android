package elieomatuku.cineast_android.ui.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.core.model.Person
import elieomatuku.cineast_android.ui.adapter.PeopleAdapter
import elieomatuku.cineast_android.ui.content_list.ContentListActivity
import elieomatuku.cineast_android.ui.viewholder.ContentHolder
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_people.view.*

class PeopleHolder(itemView: View, private val onPeopleClickPublisher: PublishSubject<Person>) : ContentHolder(itemView) {

    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_people, parent, false)
        }

        fun newInstance(parent: ViewGroup, onPersonalityClickPublisher: PublishSubject<Person>): PeopleHolder {
            return PeopleHolder(createView(parent), onPersonalityClickPublisher)
        }
    }

    private val seeAllView by lazy {
        itemView.see_all
    }

    private val adapter: PeopleAdapter by lazy {
        PeopleAdapter(onPeopleClickPublisher)
    }

    private val listView: RecyclerView by lazy {
        itemView.recyclerview_people
    }

    override fun update(content: List<Content>, titleRes: Int?) {
        adapter.people = content.toMutableList() as MutableList<Person>
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
        listView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

        seeAllView.setOnClickListener {
            ContentListActivity.startItemListActivity(itemView.context, content, R.string.popular_people)
        }
    }
}
