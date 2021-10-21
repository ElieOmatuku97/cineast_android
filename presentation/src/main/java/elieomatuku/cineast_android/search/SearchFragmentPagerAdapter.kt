package elieomatuku.cineast_android.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.search.movie.MoviesGridFragment
import elieomatuku.cineast_android.search.people.PeopleGridFragment
import timber.log.Timber

class SearchFragmentPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    companion object {
        val titleResList: List<Int> by lazy {
            listOf(
                R.string.movies,
                R.string.people
            )
        }
    }

    var content: List<Content> = listOf()
    override fun getItemCount(): Int {
        return titleResList.size
    }

    override fun createFragment(position: Int): Fragment {
        Timber.d("SearchFragmentPagerAdapter position: $position")
        return when (titleResList[position]) {
            R.string.movies -> MoviesGridFragment.newInstance()
            R.string.people -> PeopleGridFragment.newInstance()
            else -> MoviesGridFragment.newInstance()
        }
    }
}
