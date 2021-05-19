package elieomatuku.cineast_android.ui.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.ui.search.movie.MoviesSearchFragment
import elieomatuku.cineast_android.ui.search.people.PeopleSearchFragment
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
            R.string.movies -> MoviesSearchFragment.newInstance()
            R.string.people -> PeopleSearchFragment.newInstance()
            else -> PeopleSearchFragment.newInstance()
        }
    }
}
