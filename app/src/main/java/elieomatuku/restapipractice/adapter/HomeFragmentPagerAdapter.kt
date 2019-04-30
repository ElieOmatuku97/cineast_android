package elieomatuku.restapipractice.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import elieomatuku.restapipractice.fragment.DiscoverFragment
import elieomatuku.restapipractice.fragment.MyTMBDFragment
import elieomatuku.restapipractice.fragment.SearchFragment
import elieomatuku.restapipractice.R

class HomeFragmentPagerAdapter(fm: FragmentManager?): FragmentPagerAdapter(fm) {

    companion object {
        val titleResList: List<Int> by lazy {
            listOf(R.string.nav_title_discover,
                    R.string.nav_title_search,
                    R.string.nav_title_my_tmdb)
        }
    }

    override fun getCount(): Int {
       return titleResList.size
    }

    override fun getItem(position: Int): Fragment {
        return when (titleResList[position]){
            R.string.nav_title_discover ->  DiscoverFragment.newInstance()
            R.string.nav_title_search -> SearchFragment.newInstance()
            R.string.nav_title_my_tmdb -> MyTMBDFragment.newInstance()
            else -> MyTMBDFragment.newInstance()
        }
    }
}