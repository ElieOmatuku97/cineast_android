package elieomatuku.cineast_android.presentation.fragment

import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import elieomatuku.cineast_android.presentation.R
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.presentation.base.BaseDialogFragment

class RateDialogFragment : BaseDialogFragment() {
    companion object {
        const val TAG = "fragment_rate_dialog"
        const val MOVIE_KEY = "movie_key"

        fun newInstance(movie: Movie?): RateDialogFragment {
            val args = Bundle()
            args.putSerializable(MOVIE_KEY, movie)

            val fragment = RateDialogFragment()
            fragment.arguments = args

            return fragment
        }
    }

    private val viewModel: RateViewModel by viewModels()

    private var movie: Movie? = null
    private var submitBtn: TextView? = null
    private var ratingBar: AppCompatRatingBar? = null
    private var dialogTitle: TextView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity?.layoutInflater
        val dialogView = inflater?.inflate(R.layout.fragment_rate_dialog, null)

        dialogTitle = dialogView?.findViewById<TextView>(R.id.dialog_title)

        ratingBar = dialogView?.findViewById<AppCompatRatingBar>(R.id.rating_bar)
        ratingBar?.rating?.let {
            displayRating(it)
        }

        submitBtn = dialogView?.findViewById<TextView>(R.id.dialog_submit)
        movie = arguments?.getSerializable(MOVIE_KEY) as Movie

        val dialog = Dialog(requireContext())

        dialogView?.let {
            dialog.setContentView(it)
        }

        activity?.let {
            dialog.window?.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    it,
                    R.drawable.bg_rate_dialog
                )
            )
        }

        onRatingInput()
        onSubmitClick()
        dialog.show()

        return dialog
    }

    private fun onSubmitClick() {
        submitBtn?.setOnClickListener {
            val rating = ratingBar?.rating?.toDouble()

            rating?.let { _rating ->
                movie?.let {
                    viewModel.rateMovie(it, _rating)
                }
            }

            dialog?.cancel()
        }
    }

    private fun onRatingInput() {
        ratingBar?.setOnRatingBarChangeListener { _, rating, _ ->
            displayRating(rating)
        }
    }

    private fun displayRating(rating: Float) {
        dialogTitle?.text =
            String.format("%s %.1f", activity?.getString(R.string.rate_this_movie), rating)
    }
}
