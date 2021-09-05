package elieomatuku.cineast_android.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.extensions.Contents
import elieomatuku.cineast_android.ui.contents.ContentsActivity
import elieomatuku.cineast_android.ui.contents.ContentsAdapter
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_people.view.*

class PeopleHolder(itemView: View, private val onPeopleClickPublisher: PublishSubject<Content>) : ContentHolder(itemView) {

    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_people, parent, false)
        }

        fun newInstance(parent: ViewGroup, onPersonalityClickPublisher: PublishSubject<Content>): PeopleHolder {
            return PeopleHolder(createView(parent), onPersonalityClickPublisher)
        }
    }

    private val seeAllView by lazy {
        itemView.see_all
    }

    private val adapter: ContentsAdapter by lazy {
        ContentsAdapter(onPeopleClickPublisher)
    }

    private val listView: RecyclerView by lazy {
        itemView.recyclerview_people
    }

    override fun update(contents: Contents) {
        adapter.contents = contents.value?.toMutableList() ?: mutableListOf()
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
        listView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

        seeAllView.setOnClickListener {
            ContentsActivity.startActivity(itemView.context, contents, contents.titleResources)
        }
    }
}
