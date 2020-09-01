package com.jawabdulu.app.ui.fragment

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.jawabdulu.app.R
import com.jawabdulu.app.models.AppModel
import java.util.*


class AllAppsFragment : BaseFragment(R.layout.fragment_all_apps) {

    private val TAG = "AllAppsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.quiz.observe(viewLifecycleOwner, Observer { response ->

        })

    }

//    private fun getInstalledApps(): List<AppModel>? {
//        val res: List<AppModel> = ArrayList<AppModel>()
//        val packs: List<PackageInfo> = requireActivity().packageManager.getInstalledPackages(0)
//        for (i in packs.indices) {
//            val p = packs[i]
//            Log.d(TAG, "getInstalledApps: " + packs.size)
//            if (lock.contains(packs[i].packageName)) {
//                if (isSystemPackage(p) == false) {
//                    val appName = p.applicationInfo.loadLabel(getPackageManager()).toString()
//                    val icon = p.applicationInfo.loadIcon(getPackageManager())
//                    appList.add(Aplikasi(appName, icon, R.drawable.ic_lock, packs[i].packageName))
//                }
//            } else {
//                if (isSystemPackage(p) == false) {
//                    val appName = p.applicationInfo.loadLabel(getPackageManager()).toString()
//                    val icon = p.applicationInfo.loadIcon(getPackageManager())
//                    appList.add(
//                        Aplikasi(
//                            appName,
//                            icon,
//                            R.drawable.ic_lock_open,
//                            packs[i].packageName
//                        )
//                    )
//                }
//            }
//        }
//        return res
//    }
//
//    private fun isSystemPackage(p: PackageInfo): Boolean {
//        return p.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
//    }
}