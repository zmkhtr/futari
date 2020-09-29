package com.jawabdulu.app.ui.activity

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jawabdulu.app.R
import com.jawabdulu.app.adaptersimport.AppRecyclerAdapter
import com.jawabdulu.app.db.QuizDatabase
import com.jawabdulu.app.models.App
import com.jawabdulu.app.models.QuizResponse
import com.jawabdulu.app.preferences.Preferences
import com.jawabdulu.app.repository.AppRepository
import com.jawabdulu.app.repository.QuizRepository
import com.jawabdulu.app.viewModel.AppViewModelFactory
import com.jawabdulu.app.viewModel.QuizViewModel
import com.jawabdulu.app.viewModel.QuizViewModelFactory
import kotlinx.android.synthetic.main.activity_apps.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*

class AppsActivity : AppCompatActivity(), AppRecyclerAdapter.Interaction {
    private val TAG = "AppsActivity"

    private var lock: MutableList<String> = mutableListOf()
    private var appList: MutableList<App> = mutableListOf()
    private var sortedList: List<App> = mutableListOf()
    lateinit var appAdapter: AppRecyclerAdapter

    lateinit var viewModel : QuizViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apps)

        val quizRepository = QuizRepository(QuizDatabase(this))
        val viewModelProviderFactory = QuizViewModelFactory(application, quizRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(QuizViewModel::class.java)

        lock = Preferences.getListPackageName()

        toolbar.title = resources.getString(R.string.app_name)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        setupRecyclerView()
        getInstalledApps()
        searchApp()
        saveLokalQuiz()
    }

    private fun searchApp(){
        var job : Job? = null
        edtAppNameSearch.addTextChangedListener {editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                if(editable.toString().isNotEmpty()){
                    val filtered = sortedList.filter { it.namaAplikasi.toLowerCase(Locale.ROOT).contains(editable.toString().toLowerCase(
                        Locale.ROOT))
                    }
                    Log.d(TAG, "searchApp: $filtered" )
                    appAdapter.submitList(filtered)
                } else {
                    appAdapter.submitList(sortedList)
                }
            }
        }
    }

    private fun getInstalledApps() {
        viewModel.apps.observe(this, Observer { packs ->
            for (i in packs.indices) {
                val p = packs[i]
                Log.d(TAG, "getInstalledApps: " + packs.size)

                val appName = p.applicationInfo.loadLabel(this.packageManager).toString()
                val icon = p.applicationInfo.loadIcon(this.packageManager)
                if (lock.isNotEmpty()){

                    if (lock.contains(packs[i].packageName)) {
                        if (isSystemPackage(p)) {
//                            appList.add(App(appName, icon, true, packs[i].packageName, true))
                        } else {
                            appList.add(App(appName, icon, true, packs[i].packageName, false))
                        }
                    } else {
                        if (isSystemPackage(p)) {
//                            appList.add(App(appName, icon, false, packs[i].packageName, true))
                        } else {
                            appList.add(App(appName, icon, false, packs[i].packageName, false))
                        }
                    }
                } else {
                    if (isSystemPackage(p)) {
//                        appList.add(App(appName, icon, false, packs[i].packageName, true))
                    } else {
                        appList.add(App(appName, icon, false, packs[i].packageName, false))
                    }
                }
            }
        })

        CoroutineScope(Dispatchers.Main).launch {
            sortedList = appList.sortedBy { it.packageName }
//            sortedList = sortedList.distinctBy { it.isSystemPackage }
            sortedList = sortedList.sortedByDescending { it.locked }
            appAdapter.submitList(sortedList)
            progressBarApps.visibility = View.GONE
        }
    }

    private fun isSystemPackage(p: PackageInfo): Boolean {
        return p.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    override fun onItemSelected(position: Int, item: App) {
        if (item.locked) {
            lock.remove(item.packageName)
            Log.d(TAG, "onItemSelected: remove " + lock.size)
            Preferences.setListPackageName(lock)
            Toast.makeText(this, "Kunci aplikasi dibuka", Toast.LENGTH_SHORT).show()
            item.locked = false
        } else {
            lock.add(item.packageName)
            Log.d(TAG, "onItemSelected: add " + lock.size)
            Preferences.setListPackageName(lock)
            Toast.makeText(this, "Berhasil mengunci aplikasi", Toast.LENGTH_SHORT)
                .show()
            item.locked = true
        }
        appAdapter.notifyDataSetChanged()
    }

    private fun setupRecyclerView(){
        appAdapter = AppRecyclerAdapter(this)

        rvAllApps.apply {
            adapter = appAdapter
            layoutManager = LinearLayoutManager(this@AppsActivity)
        }

    }

    fun saveLokalQuiz(){
        val jsonFileString = getJsonDataFromAsset(applicationContext, "quiz.json")
        Log.i("data", jsonFileString)

        val gson = Gson()
        val quizType = object : TypeToken<List<QuizResponse.QuizResponseItem>>() {}.type

        val quizes: List<QuizResponse.QuizResponseItem> = gson.fromJson(jsonFileString, quizType)

        for (quiz in quizes) {
            viewModel.saveQuiz(quiz)
        }
    }

    private  fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}