package com.jawabdulu.app.ui.fragment

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jawabdulu.app.R
import com.jawabdulu.app.adaptersimport.AppRecyclerAdapter
import com.jawabdulu.app.adaptersimport.AppWithoutLockRecyclerAdapter
import com.jawabdulu.app.models.App
import com.jawabdulu.app.models.AppModel
import com.jawabdulu.app.preferences.Preferences
import kotlinx.android.synthetic.main.fragment_all_apps.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.ArrayList

class ProfileFragment : BaseFragment(R.layout.fragment_profile), AppWithoutLockRecyclerAdapter.Interaction  {
    private val TAG = "ProfileFragment"

    private var lock: MutableList<String> = mutableListOf()
    private var appVM: List<AppModel> = ArrayList()
    private var appList: MutableList<App> = mutableListOf()
    lateinit var appAdapter: AppWithoutLockRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getPersistentView(inflater, container, savedInstanceState, R.layout.fragment_profile)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!hasInitializedRootView) {
            hasInitializedRootView = true
        }

        lock = Preferences.getListPackageName()
        setupRecyclerView()
        getInstalledApps()

        bindView()
    }

    private fun bindView(){
        val anak = Preferences.getDataAnak()
        tvProfileBenar.text = "Total jawaban benar : ${anak!!.totalBenar}"
        tvProfileSalah.text = "Total jawaban Salah : ${anak.totalSalah}"
        tvProfileNamaAnak.text = anak.nama
        tvProfileTglLahir.text = anak.tanggalLahir
        tvProfileKelas.text = "${anak.kelas} SD"
    }

    private fun setupRecyclerView(){
        appAdapter = AppWithoutLockRecyclerAdapter(this@ProfileFragment)

        rvProfile.apply {
            adapter = appAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }

    private fun getInstalledApps() {
        val packs: List<PackageInfo> = requireActivity().packageManager.getInstalledPackages(0)
        for (i in packs.indices) {
            val p = packs[i]
            Log.d(TAG, "getInstalledApps: " + packs.size)
            if (lock.isNotEmpty()){

                if (lock.contains(packs[i].packageName)) {
                    if (!isSystemPackage(p)) {
                        val appName =
                            p.applicationInfo.loadLabel(requireActivity().packageManager).toString()
                        val icon = p.applicationInfo.loadIcon(requireActivity().packageManager)

//                    val iconValue = R.drawable.icon
                        appList.add(App(appName, icon, true, packs[i].packageName))
                    }
                }
            }
        }
        appAdapter.submitList(appList)
    }

    private fun isSystemPackage(p: PackageInfo): Boolean {
        return p.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    override fun onItemSelected(position: Int, item: App) {
        //nothing
    }
}