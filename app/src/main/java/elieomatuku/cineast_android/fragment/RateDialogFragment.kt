package elieomatuku.cineast_android.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatRatingBar
import android.widget.TextView
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.business.model.data.Movie
import elieomatuku.cineast_android.business.service.UserService
import org.kodein.di.generic.instance
import timber.log.Timber


class RateDialogFragment : DialogFragment() {
    companion object {
        const val TAG = "fragment_rate_dialog"
        const val MOVIE_KEY = "movie_key"

        fun newInstance(movie: Movie?): RateDialogFragment {
            val args = Bundle()
            args.putParcelable(MOVIE_KEY, movie)

            val fragment = RateDialogFragment()
            fragment.arguments = args

            return fragment
        }
    }

    private val userService : UserService by App.kodein.instance()
    private var movie: Movie? = null
    private var submitBtn: TextView? = null
    private var ratingBar: AppCompatRatingBar? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity?.layoutInflater
        val dialogView = inflater?.inflate(R.layout.fragment_rate_dialog, null)

        ratingBar = dialogView?.findViewById<AppCompatRatingBar>(R.id.rating_bar)
        submitBtn = dialogView?.findViewById<TextView>(R.id.dialog_submit)
        movie = arguments?.getParcelable(MOVIE_KEY)


        val dialog = Dialog(activity)

        dialogView?.let {
            dialog.setContentView(it)
        }

        activity?.let {
            dialog.window?.setBackgroundDrawable(ContextCompat.getDrawable(it, R.drawable.bg_rate_dialog))
        }


        onSubmitClick()
        dialog.show()

        return dialog
    }


    private fun onSubmitClick() {
        submitBtn?.setOnClickListener {
            val rating = ratingBar?.rating?.toDouble()

            rating?.let {_rating ->
                movie?.let {
                    userService.postMovieRate(it, _rating)
                }
            }

            dialog.cancel()
        }
    }
}