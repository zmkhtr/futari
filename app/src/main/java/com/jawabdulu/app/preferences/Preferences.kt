package com.jawabdulu.app.preferences

import com.jawabdulu.app.models.Anak
import com.jawabdulu.app.models.OrangTua
import com.orhanobut.hawk.Hawk.*

object Preferences {

    private const val PASS_CODE_KEY ="PASS_CODE_KEY"
    private const val FIRST_OPEN_KEY ="FIRST_OPEN_KEY"
    private const val FIRST_OPEN_SLIDER_KEY ="FIRST_OPEN_SLIDER_KEY"
    private const val FIRST_OPEN_TEMP_KEY ="FIRST_OPEN_TEMP_KEY"

    private const val ANAK_KEY ="ANAK_KEY"
    private const val ORANG_TUA_KEY ="ORANG_TUA_KEY"


    private const val PACKAGE_KEY ="PACKAGE_KEY"
    private const val WRONG_COUNT_KEY ="COUNT_KEY"


    private const val LAPORAN_KEY ="LAPORAN_KEY"
    private const val CURRENT_APP_KEY ="CURRENT_APP_KEY"

    fun setLaporanHarian(enable: Boolean){
        put(LAPORAN_KEY, enable)
    }

    fun isLaporanHarianEnable() : Boolean {
        return get(LAPORAN_KEY, true)
    }

    fun setWrongCount(int : Int) {
        put(WRONG_COUNT_KEY, int)
    }
    fun getWrongCount() : Int {
        return get(WRONG_COUNT_KEY, 0)
    }

    fun getUserPassCodeList() : List<String>? {
        val list : MutableList<String> = mutableListOf()
        list.add(getUserPassCode()!!)
        return get(PASS_CODE_KEY, list)
    }

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

    fun setIsUserFirstOpenSlider(isFirstOpen: Boolean){
        put(FIRST_OPEN_SLIDER_KEY, isFirstOpen)
    }

    fun isFirstOpenSlider() : Boolean {
        return get(FIRST_OPEN_SLIDER_KEY, true)
    }

    fun setIsUserFirstOpenTemp(isFirstOpen: Boolean){
        put(FIRST_OPEN_TEMP_KEY, isFirstOpen)
    }

    fun isFirstOpenTemp() : Boolean {
        return get(FIRST_OPEN_TEMP_KEY, true)
    }

    fun setDataAnak(anak: Anak){
        put(ANAK_KEY, anak)
    }

    fun getDataAnak() : Anak? {
        return get(ANAK_KEY, null)
    }

    fun setDataOrangTua(orangTua : OrangTua){
        put(ORANG_TUA_KEY, orangTua)
    }

    fun getDataOrangTua() : OrangTua? {
        return get(ORANG_TUA_KEY, null)
    }

    fun setListPackageName(packageName : MutableList<String>) {
        put(PACKAGE_KEY, packageName)
    }

    fun getListPackageName() : MutableList<String> {
        return get(PACKAGE_KEY, ArrayList())
    }

    fun deleteListPackage() {
        delete(PACKAGE_KEY)
    }

    fun getCurrentApp() : String?{
        return get(CURRENT_APP_KEY, null)
    }

    fun setCurrentApp(currentApp : String) {
        put(CURRENT_APP_KEY, currentApp)
    }

}