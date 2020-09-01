package com.jawabdulu.app.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.jawabdulu.app.R
import com.jawabdulu.app.adapters.SectionPageAdapter
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager(container)

    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = SectionPageAdapter(activity?.supportFragmentManager!!)
        adapter.addFragment(AllAppsFragment(), "Semua Aplikasi")
        adapter.addFragment(LockedAppsFragment(), "Aplikasi Terkunci")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }
}