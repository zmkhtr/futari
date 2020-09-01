package com.jawabdulu.app.preferences

import com.orhanobut.hawk.Hawk.get
import com.orhanobut.hawk.Hawk.put

object Preferences {
    private const val PASS_CODE_KEY ="PASS_CODE_KEY"
    private const val FIRST_OPEN_KEY ="FIRST_OPEN_KEY"
    private const val FIRST_OPEN_TEMP_KEY ="FIRST_OPEN_TEMP_KEY"


    fun setUserPassCode(passCode : String) {
        put(PASS_CODE_KEY, passCode)
    }

    fun getUserPassCode() : String? {
        return get(PASS_CODE_KEY, null)
    }

    fun setIsUserFirstOpen(isFirstOpen: Boolean){
        put(FIRST_OPEN_KEY, isFirstOpen)
    }

    fun isFirstOpen() : Boolean {
        return get(FIRST_OPEN_KEY, true)
    }


    fun setIsUserFirstOpenTemp(isFirstOpen: Boolean){
        put(FIRST_OPEN_TEMP_KEY, isFirstOpen)
    }

    fun isFirstOpenTemp() : Boolean {
        return get(FIRST_OPEN_TEMP_KEY, true)
    }
}