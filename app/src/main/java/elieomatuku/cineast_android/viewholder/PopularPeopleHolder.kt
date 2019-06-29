package elieomatuku.cineast_android.viewholder

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.activity.ItemListActivity
import elieomatuku.cineast_android.adapter.PopularPeopleItemAdapter
import elieomatuku.cineast_android.business.model.data.People
import elieomatuku.cineast_android.business.model.data.Person
import elieomatuku.cineast_android.utils.UiUtils
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

    fun update(popularPeople: List<People>, onPersonClickPublisher: PublishSubject<Person>) {
        itemView.recyclerview_people.adapter = PopularPeopleItemAdapter (popularPeople, onPersonClickPublisher)
        itemView.recyclerview_people.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

        seeAllView.setOnClickListener {
            val intent = Intent (itemView.context, ItemListActivity::class.java)
            val params = Bundle()
            params.putParcelableArrayList(UiUtils.WIDGET_KEY, popularPeople as ArrayList<out Parcelable>)
            params.putInt(UiUtils.SCREEN_NAME_KEY, R.string.popular_people)
            intent.putExtras(params)
            itemView.context.startActivity(intent)
        }
    }
}