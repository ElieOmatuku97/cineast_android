package elieomatuku.cineast_android.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.fragment.MoviesSearchFragment
import elieomatuku.cineast_android.fragment.PeopleSearchFragment
import timber.log.Timber


class SearchFragmentPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    companion object {
        val titleResList: List<Int> by lazy {
            listOf(R.string.movies,
                    R.string.people)
        }
    }

    var content : List<Content> = listOf()
    override fun getCount(): Int {
        return titleResList.size
    }

    override fun getItem(position: Int): Fragment {
        Timber.d("SearchFragmentPagerAdapter position: $position")
        return when (titleResList[position]){
            R.string.movies -> MoviesSearchFragment.newInstance()
            R.string.people -> PeopleSearchFragment.newInstance()
            else -> PeopleSearchFragment.newInstance()
        }
    }
}