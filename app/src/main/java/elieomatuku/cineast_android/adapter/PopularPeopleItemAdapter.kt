package elieomatuku.cineast_android.adapter


import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.cineast_android.business.business.model.data.Person
import elieomatuku.cineast_android.viewholder.itemHolder.PopularPeopleItemHolder
import io.reactivex.subjects.PublishSubject


class PopularPeopleItemAdapter(private val popularPeople: List<Person>,
                               private val onItemClickPublisher: PublishSubject<Person>,
                               private val itemListLayoutRes: Int? = null): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
       return popularPeople.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PopularPeopleItemHolder.newInstance(parent, itemListLayoutRes)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PopularPeopleItemHolder).update(popularPeople[position])

        holder.itemView.setOnClickListener {
            onItemClickPublisher.onNext(popularPeople[position])
        }
    }
}