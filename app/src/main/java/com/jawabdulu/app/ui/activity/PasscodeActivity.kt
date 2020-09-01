package com.jawabdulu.app.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andrognito.pinlockview.PinLockListener
import com.jawabdulu.app.R
import com.jawabdulu.app.preferences.Preferences
import kotlinx.android.synthetic.main.activity_passcode.*


class PasscodeActivity : BaseActivity() {
    private val TAG = "PasscodeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passcode)

        setPassCode()
        changeTitle()
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
                } else {
                    Preferences.setIsUserFirstOpen(true)
                    reloadActivity()
                    createToast("Passcode tidak sama")
                }
            } else if (Preferences.getUserPassCode() == passCode) {
                startActivity(Intent(this, MainActivity::class.java))
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
}