package com.jawabdulu.app.ui.activity

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.jawabdulu.app.R
import com.jawabdulu.app.models.Anak
import com.jawabdulu.app.preferences.Preferences
import kotlinx.android.synthetic.main.activity_data_anak.*
import java.text.SimpleDateFormat
import java.util.*


class DataAnakActivity : BaseActivity() {
    private val TAG = "DataAnakActivity"

    val myCalendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_anak)

        Log.d(TAG, "onCreate: 1 " + Preferences.getDataAnak())

        btnDataAnakTuaLanjutkan.setOnClickListener {
            val anak = Anak(etDataAnakNama.text.toString(), etDataAnakTglLahir.text.toString(), etDataAnakKelasAnak.text.toString(),0,0)
            if (etDataAnakNama.text.isNullOrEmpty() || etDataAnakTglLahir.text.isNullOrEmpty() || etDataAnakKelasAnak.text.isNullOrEmpty()) {
                createToast("Tolong isi semua data")
            } else {
                Preferences.setDataAnak(anak)
                Log.d(TAG, "onCreate: 2 " + Preferences.getDataAnak())
                startActivity(Intent(this, PasscodeActivity::class.java))
                finish()
            }
        }

        val date =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                updateLabel()
            }

        etDataAnakTglLahir.setOnClickListener {
            Log.d(TAG, "onCreate: ASa")
            DatePickerDialog(
                this@DataAnakActivity, date, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        dataAnak()
    }

    private fun updateLabel() {
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        etDataAnakTglLahir.setText(sdf.format(myCalendar.time))
    }

    private fun dataAnak(){
        val kelas =
            arrayOf<String?>(
                "Kelas 1",
                "Kelas 2",
                "Kelas 3",
                "Kelas 4",
                "Kelas 5",
                "Kelas 6"
            )

        val adapter: ArrayAdapter<String?> = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            kelas
        )
        etDataAnakKelasAnak.setAdapter(adapter)
    }


}