package com.jawabdulu.app.ui.activity

import android.annotation.TargetApi
import android.app.ActivityManager
import android.app.AppOpsManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.andrognito.pinlockview.PinLockListener
import com.jawabdulu.app.R
import com.jawabdulu.app.preferences.Preferences
import com.jawabdulu.app.servicejava.BackgroundService
import kotlinx.android.synthetic.main.activity_passcode.*


class PasscodeActivity : BaseActivity() {
    private val TAG = "PasscodeActivity"
    val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 700
    private var mServiceIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passcode)

        setPassCode()
        changeTitle()
        checkPermission()
        validatePermission()
    }

    private fun changeTitle(){
        if (Preferences.isFirstOpen()) {
            tvPassCodeTitle.text = getString(R.string.title_set_pass_code)
            tvPassCodeDescription.text = getString(R.string.desc_set_pass_code)
        } else if (Preferences.isFirstOpenTemp()) {
                tvPassCodeTitle.text = getString(R.string.title_confirm_pass_code)
                tvPassCodeDescription.text = getString(R.string.desc_confirm_pass_code)
        }
    }

    private fun setPassCode(){
        pin_lock_view.setPinLockListener(mPinLockListener)
        pin_lock_view.attachIndicatorDots(indicator_dots)
    }

    private val mPinLockListener: PinLockListener = object : PinLockListener {
        override fun onComplete(pin: String) {
            Log.d(TAG, "Pin complete: $pin")

            passCodeHandler(pin)
        }

        override fun onEmpty() {
            Log.d(TAG, "Pin empty")
        }

        override fun onPinChange(pinLength: Int, intermediatePin: String) {
            Log.d(
                TAG,
                "Pin changed, new length $pinLength with intermediate pin $intermediatePin"
            )
        }
    }

    private fun passCodeHandler(passCode : String){
        if (Preferences.isFirstOpen()) {
            Preferences.setIsUserFirstOpen(false)
            Preferences.setUserPassCode(passCode)
            reloadActivity()
        } else {
            if (Preferences.isFirstOpenTemp()) {
                if (Preferences.getUserPassCode() == passCode) {
                    Preferences.setIsUserFirstOpenTemp(false)
                    startActivity(Intent(this, MainActivity::class.java))
                    createToast("Passcode berhasil dipasang")
                    finish()
                } else {
                    Preferences.setIsUserFirstOpen(true)
                    reloadActivity()
                    createToast("Passcode tidak sama")
                }
            } else if (Preferences.getUserPassCode() == passCode) {
                createToast("Passcode benar")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                createToast("Passcode salah")
                reloadActivity()
            }
        }
    }

    private fun reloadActivity(){
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }




    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                AlertDialog.Builder(this)
                    .setTitle("APP OVERLAY Permission")
                    .setMessage("Mohon perbolehkan permission APP OVERLAY untuk aplikasi ini pada Setting")
                    .setPositiveButton(
                        "Allow"
                    ) { dialog, which ->
                            val intent = Intent(
                                Settings.ACTION_MANAGE_OVERLAY_PERMISSION
//                                Uri.parse("package:$packageName")
                            )
                        startActivity(intent)
                    }
                    .setNegativeButton(
                        "Abort"
                    ) { dialog, which -> }
                    .show()

            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // You don't have permission
                checkPermission()
            } else {
                reloadActivity()
            }
        }
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


}