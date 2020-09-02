package com.jawabdulu.app.ui.fragment

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jawabdulu.app.R
import com.jawabdulu.app.adaptersimport.AppRecyclerAdapter
import com.jawabdulu.app.models.App
import com.jawabdulu.app.models.AppModel
import com.jawabdulu.app.preferences.Preferences
import kotlinx.android.synthetic.main.fragment_all_apps.*
import java.util.*
import kotlin.math.log


class AllAppsFragment : BaseFragment(R.layout.fragment_all_apps), AppRecyclerAdapter.Interaction {

    private val TAG = "AllAppsFragment"

    private var lock: MutableList<String> = mutableListOf()
    private var appVM: List<AppModel> = ArrayList()
    private var appList: MutableList<App> = mutableListOf()
    lateinit var appAdapter: AppRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getPersistentView(inflater, container, savedInstanceState, R.layout.fragment_all_apps)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!hasInitializedRootView) {
            hasInitializedRootView = true
        }

//        viewModel.quiz.observe(viewLifecycleOwner, Observer { response ->
//
//        })
//
//        viewModelApp.getAllLockedApp().observe(viewLifecycleOwner, Observer {
//            lock = it
//        })

//        Preferences.deleteListPackage()
        lock = Preferences.getListPackageName()


        setupRecyclerView()
        getInstalledApps()
    }



    private fun getInstalledApps() {
        val packs: List<PackageInfo> = requireActivity().packageManager.getInstalledPackages(0)
        for (i in packs.indices) {
            val p = packs[i]
            Log.d(TAG, "getInstalledApps: " + packs.size)
            if (lock.isNotEmpty()){

            if (lock.contains(packs[i].packageName)) {
                if (!isSystemPackage(p)) {
                    val appName = p.applicationInfo.loadLabel(requireActivity().packageManager).toString()
                    val icon = p.applicationInfo.loadIcon(requireActivity().packageManager)

//                    val iconValue = R.drawable.icon
                    appList.add(App(appName, icon, true, packs[i].packageName))
                }
            } else {
                if (!isSystemPackage(p)) {
                    val appName = p.applicationInfo.loadLabel(requireActivity().packageManager).toString()
                    val icon = p.applicationInfo.loadIcon(requireActivity().packageManager)
                    appList.add(App(appName, icon, false, packs[i].packageName))
                }
            }
            } else {
                if (!isSystemPackage(p)) {
                    val appName = p.applicationInfo.loadLabel(requireActivity().packageManager).toString()
                    val icon = p.applicationInfo.loadIcon(requireActivity().packageManager)
                    appList.add(App(appName, icon, false, packs[i].packageName))
                }
            }
        }
        appAdapter.submitList(appList)
    }

    private fun isSystemPackage(p: PackageInfo): Boolean {
        return p.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    private fun setupRecyclerView(){
        appAdapter = AppRecyclerAdapter(this@AllAppsFragment)

        rvAllApps.apply {
            adapter = appAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }

    override fun onItemSelected(position: Int, item: App) {
//        val app: App = appList[position]

//        Log.d(TAG, "onItemSelected: $position")
        if (item.locked) {
            lock.remove(item.packageName)
            Log.d(TAG, "onItemSelected: remove " + lock.size)
            Preferences.setListPackageName(lock)
            Toast.makeText(activity, "Kunci aplikasi dibuka", Toast.LENGTH_SHORT).show()
            item.locked = false
        } else {
            lock.add(item.packageName)
            Log.d(TAG, "onItemSelected: add " + lock.size)
            Preferences.setListPackageName(lock)
            Toast.makeText(activity, "Berhasil mengunci aplikasi", Toast.LENGTH_SHORT)
                .show()
            item.locked = true
        }
        appAdapter.notifyDataSetChanged()
    }
}