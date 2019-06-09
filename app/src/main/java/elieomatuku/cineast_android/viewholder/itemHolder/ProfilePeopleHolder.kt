package elieomatuku.cineast_android.viewholder.itemHolder

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.business.business.model.data.PeopleDetails
import elieomatuku.cineast_android.utils.UiUtils
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_profile_people.view.*

class ProfilePeopleHolder(itemView: View, private val onProfileClickedPicturePublisher: PublishSubject<Int>): RecyclerView.ViewHolder(itemView) {

    companion object {
        fun createView(parent: ViewGroup): View{
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_profile_people, parent, false)
        }

        fun newInstance(parent: ViewGroup, onProfileClickedPicturePublisher: PublishSubject<Int>): ProfilePeopleHolder {
            return ProfilePeopleHolder(createView(parent), onProfileClickedPicturePublisher)
        }
    }


    private val peopleNameView: TextView by lazy {
        itemView.people_name_view
    }

    private val peopleDateOfBirthView: TextView by lazy {
        itemView.people_dateofbirth_view
    }

    private val peoplePlaceofbirthView: TextView by lazy {
        itemView.people_placeofbirth_view
    }

    private val profileImageView: AppCompatImageView by lazy {
        itemView.profile_image
    }

    private val homepageView: TextView by lazy {
        itemView.homepage_view
    }

    fun update(peopleDetails: PeopleDetails?) {
        val imageUrl: String? =  if (peopleDetails?.profile_path != null) {
            UiUtils.getImageUrl(peopleDetails.profile_path,  itemView.context.getString(R.string.image_small))
        } else null

        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .into(profileImageView)
        }

        profileImageView.setOnClickListener {
            if (peopleDetails?.id != null)
                onProfileClickedPicturePublisher.onNext(peopleDetails.id)
        }

        if (!peopleDetails?.name.isNullOrEmpty()) {
            peopleNameView.text = peopleDetails?.name
        } else {
            peopleNameView.visibility = View.GONE
        }

        val birthDay = peopleDetails?.birthday
        if (birthDay != null) {
            peopleDateOfBirthView.text = birthDay
        } else {
            peopleDateOfBirthView.visibility = View.GONE
        }

        if (!peopleDetails?.place_of_birth.isNullOrEmpty()) {
            peoplePlaceofbirthView.text = peopleDetails?.place_of_birth
        } else {
            peoplePlaceofbirthView.visibility = View.GONE
        }


        if (!peopleDetails?.homepage.isNullOrEmpty() ) {
            homepageView.visibility = View.VISIBLE
            val spannable = SpannableString(Html.fromHtml(peopleDetails?.homepage))
            Linkify.addLinks(spannable, Linkify.WEB_URLS)
            homepageView.setMovementMethod(LinkMovementMethod.getInstance())
            homepageView.setText(linkify(spannable), TextView.BufferType.SPANNABLE)

        } else {
            homepageView.visibility = View.GONE
        }

    }

    private fun linkify(spannable: Spannable): Spannable {
        val spans = spannable.getSpans(0, spannable.length, URLSpan::class.java)
        for (urlSpan in spans) {
            configSpannableLinkify(urlSpan, spannable, object : URLSpan(urlSpan.url) {
                override fun onClick(view: View) {
                    UiUtils.gotoWebview(url, itemView.context as AppCompatActivity )
                }
            } )
        }
        return spannable
    }

    //Todo: Rename this method
    private fun configSpannableLinkify (urlSpan: URLSpan, spannable: Spannable, linkSpan: URLSpan) {
        val spanStart = spannable.getSpanStart(urlSpan)
        val spanEnd = spannable.getSpanEnd(urlSpan)
        spannable.setSpan(linkSpan, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.removeSpan(urlSpan)
    }
}