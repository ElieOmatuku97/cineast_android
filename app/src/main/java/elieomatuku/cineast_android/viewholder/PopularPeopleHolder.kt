package elieomatuku.cineast_android.viewholder


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.activity.ItemListActivity
import elieomatuku.cineast_android.adapter.PopularPeopleItemAdapter
import elieomatuku.cineast_android.business.model.data.Personality
import elieomatuku.cineast_android.business.model.data.Person
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_people.view.*

class PopularPeopleHolder(itemView: View) : RecyclerView.ViewHolder (itemView){

    companion object {
        fun createView(parent: ViewGroup): View{
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_people, parent, false)
        }

        fun newInstance(parent: ViewGroup): PopularPeopleHolder {
            return PopularPeopleHolder(createView(parent))
        }
    }

    private val seeAllView by lazy {
        itemView.see_all
    }

    fun update(personalities: List<Personality>, onPersonalityClickPublisher: PublishSubject<Person>) {
        itemView.recyclerview_people.adapter = PopularPeopleItemAdapter (personalities, onPersonalityClickPublisher)
        itemView.recyclerview_people.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

        seeAllView.setOnClickListener {
            ItemListActivity.startItemListActivity(itemView.context, personalities,  R.string.popular_people)
        }
    }
}