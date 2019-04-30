package elieomatuku.restapipractice.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import elieomatuku.restapipractice.R
import elieomatuku.restapipractice.business.business.model.data.Widget
import elieomatuku.restapipractice.fragment.PopularMoviesFragment
import elieomatuku.restapipractice.fragment.PopularPeopleFragment
import timber.log.Timber


class SearchFragmentPagerAdapter(fm: FragmentManager?): FragmentPagerAdapter(fm) {
    companion object {
        val titleResList: List<Int> by lazy {
            listOf(R.string.movies,
                    R.string.people)
        }
    }

    var widget : List<Widget> = listOf()
    override fun getCount(): Int {
        return titleResList.size
    }

    override fun getItem(position: Int): Fragment {
        Timber.d("SearchFragmentPagerAdapter position: $position")
        return when (titleResList[position]){
            R.string.movies -> PopularMoviesFragment.newInstance()
            R.string.people -> PopularPeopleFragment.newInstance()
            else -> PopularPeopleFragment.newInstance()
        }
    }
}