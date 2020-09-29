package com.jawabdulu.app.ui.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.jawabdulu.app.R
import com.jawabdulu.app.preferences.Preferences

class IntroSliderActivity : AppIntro2() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(AppIntroFragment.newInstance(
            title = "Jawab Dulu! \nsebelum Bermain.",
            description = "Kunci Aplikasi atau permainan dengan Quiz Menarik untuk menambah pengetahuan anak",
            imageDrawable = R.drawable.kids,
            backgroundColor = ContextCompat.getColor(this, R.color.white),
            titleColor = ContextCompat.getColor(this, R.color.colorAccent),
            descriptionColor = ContextCompat.getColor(this, R.color.color_black)
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "Permission",
            description = "Izinkan semua permission yang diperlukan agar aplikasi berjalan dengan semestinya",
            imageDrawable = R.drawable.ic_key,
            backgroundColor = ContextCompat.getColor(this, R.color.white),
            titleColor = ContextCompat.getColor(this, R.color.colorAccent),
            descriptionColor = ContextCompat.getColor(this, R.color.color_black)
        ))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        Preferences.setIsUserFirstOpenSlider(false)
        startActivity(Intent(this@IntroSliderActivity, DataAnakActivity::class.java))
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        Preferences.setIsUserFirstOpenSlider(false)
        startActivity(Intent(this@IntroSliderActivity, DataAnakActivity::class.java))
        finish()
    }
}