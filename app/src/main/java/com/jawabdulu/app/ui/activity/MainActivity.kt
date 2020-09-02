package com.jawabdulu.app.ui.activity

import android.app.ActivityManager
import android.app.AppOpsManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.jawabdulu.app.R
import com.jawabdulu.app.db.QuizDatabase
import com.jawabdulu.app.repository.AppRepository
import com.jawabdulu.app.repository.BigBoxRepository
import com.jawabdulu.app.repository.QuizRepository
import com.jawabdulu.app.servicejava.BackgroundService
import com.jawabdulu.app.util.Resource
import com.jawabdulu.app.viewModel.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    lateinit var viewModel : QuizViewModel
    lateinit var viewModelApp : AppViewModel
    lateinit var viewModelBigBox : BigBoxApiViewModel
    private var mServiceIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val quizRepository = QuizRepository(QuizDatabase(this))
        val appRepository = AppRepository(QuizDatabase(this))
        val viewModelProviderFactory = QuizViewModelFactory(application, quizRepository)
        val viewModelProviderFactoryApp = AppViewModelFactory(application, appRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(QuizViewModel::class.java)
        viewModelApp = ViewModelProvider(this, viewModelProviderFactoryApp).get(AppViewModel::class.java)



        val bigBoxRepository = BigBoxRepository()
        val viewModelProviderFactoryBigBox = BigBoxViewModelFactory(application, bigBoxRepository)
        viewModelBigBox = ViewModelProvider(this, viewModelProviderFactoryBigBox).get(BigBoxApiViewModel::class.java)


        viewModel.quiz.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { quizResponse ->
                        for (quiz in quizResponse) {
                            viewModel.saveQuiz(quiz)
                        }
                    }
                }
                is Resource.Error -> {
//                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured $message")
                        Toast.makeText(this, "An error occured $message", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
//                    showProgressBar()
                }
            }
        })


        toolbar.title = resources.getString(R.string.app_name)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent))

//        bottomNavigationView.setOnNavigationItemReselectedListener {
//
//        }
//
//        mainNavHostFragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
//
//            for(menuItem in bottomNavigationView.menu.iterator()){
//                menuItem.isEnabled = true
//            }
//
//            val menu = bottomNavigationView.menu.findItem(destination.id)
//            menu?.isEnabled = false
//        }


        bottomNavigationView.setupWithNavController(mainNavHostFragment.findNavController())






        validatePermission()
    }

    private fun validatePermission() {

        if (!isAccessGranted()) {
            AlertDialog.Builder(this)
                .setTitle("USAGE STATE Permission")
                .setMessage("Mohon perbolehkan permission USAGE STATE untuk aplikasi ini pada Setting")
                .setPositiveButton(
                    "Allow"
                ) { dialog, which -> startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)) }
                .setNegativeButton(
                    "Abort"
                ) { dialog, which -> }
                .show()
        }
        mServiceIntent = Intent(this, BackgroundService::class.java)
        if (!isMyServiceRunning(BackgroundService::class.java)) {
            startService(mServiceIntent)
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (serviceInfo in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == serviceInfo.service.className) {
                Log.d(TAG, "Running")
                return true
            }
        }
        Log.d(TAG, "isMyServiceRunning: not running")
        return false
    }

    private fun isAccessGranted(): Boolean {
        return try {
            val packageManager = packageManager
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            val appOpsManager = getSystemService(APP_OPS_SERVICE) as AppOpsManager
            var mode = 0
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    applicationInfo.uid, applicationInfo.packageName
                )
            }
            mode == AppOpsManager.MODE_ALLOWED
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}