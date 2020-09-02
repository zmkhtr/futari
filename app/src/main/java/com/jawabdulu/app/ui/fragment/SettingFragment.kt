package com.jawabdulu.app.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.jawabdulu.app.R
import com.jawabdulu.app.preferences.Preferences
import com.jawabdulu.app.util.Resource
import com.zubair.alarmmanager.builder.AlarmBuilder
import com.zubair.alarmmanager.enums.AlarmType
import com.zubair.alarmmanager.interfaces.IAlarmListener
import kotlinx.android.synthetic.main.activity_verifikasi_o_t_p.*
import kotlinx.android.synthetic.main.fragment_setting.*
import java.util.*
import java.util.concurrent.TimeUnit


class SettingFragment : BaseFragment(R.layout.fragment_setting), IAlarmListener {

    private val TAG = "SettingFragment"

    var builder: AlarmBuilder? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switchSettingLaporan.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                Preferences.setLaporanHarian(true)
            else 
                Preferences.setLaporanHarian(false)
        }
 
        tvSettingNoHP.text = Preferences.getDataOrangTua()!!.nomorHandphone

        builder = AlarmBuilder().with(requireActivity())
            .setTimeInMilliSeconds(TimeUnit.SECONDS.toMillis(10))
            .setId("UPDATE_INFO_SYSTEM_SERVICE")
            .setAlarmType(AlarmType.REPEAT)
            .setAlarm()
    }
    override fun onResume() {
        super.onResume()
        builder?.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        builder?.removeListener(this)
    }

    override fun perform(context: Context, intent: Intent) {
        val anak = Preferences.getDataAnak()
        val content = "Laporan Harian Jawab Dulu!, hari ini ${anak!!.nama} bisa menjawab ${anak.totalBenar} pertanyaan dengan benar loh ! dan aplikasi favorit ${anak.nama} adalah : Mobile Legend"

        Log.d(TAG, "perform: gg $content")
        viewModelBigBox.sendSMS(Preferences.getDataOrangTua()!!.nomorHandphone, content)
        viewModelBigBox.sms.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { it ->
                        Log.d(TAG, "perform: gg1 $it")
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e(TAG, "perform: gg An error occured $message")
                    }
                }
                is Resource.Loading -> {
                    //null
                }
            }
        })
    }

}