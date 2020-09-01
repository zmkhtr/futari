package com.jawabdulu.app.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments: MutableList<Fragment> = ArrayList()
    private val titleFragments: MutableList<String> = ArrayList()


    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titleFragments.add(title)
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return titleFragments[position]
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}