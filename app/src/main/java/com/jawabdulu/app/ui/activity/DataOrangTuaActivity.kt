package com.jawabdulu.app.ui.activity

import android.content.Intent
import android.os.Bundle
import com.jawabdulu.app.R
import com.jawabdulu.app.models.OrangTua
import com.jawabdulu.app.preferences.Preferences
import kotlinx.android.synthetic.main.activity_data_orang_tua.*

class DataOrangTuaActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_orang_tua)


        btnDataOrangTuaLanjutkan.setOnClickListener {
            val orangTua = OrangTua(
                etDataOrangTuaEmail.text.toString(),
                "+62" + charRemoveAt(etDataOrangTuaNoHp.text.toString(), 0),
                false
            )
            if (etDataOrangTuaEmail.text.isNullOrEmpty() || etDataOrangTuaNoHp.text.isNullOrEmpty()) {
                createToast("Tolong isi semua data")
            } else {
                Preferences.setDataOrangTua(orangTua)
                startActivity(Intent(this, VerifikasiOTPActivity::class.java))
                finish()
            }
        }
    }

    fun charRemoveAt(str: String, p: Int): String? {
        return str.substring(0, p) + str.substring(p + 1)
    }

}