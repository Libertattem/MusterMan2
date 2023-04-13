package com.osees.musterman

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

import androidx.lifecycle.LiveData
import java.io.File

class MainSharedPreferences (private val context: Context) {

    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val defaultTypeSharedPreferences: String = "main"
    private val defaultRouteIndex: Int = 0
    private val defaultNumberRoute: Int = 0
    private val defaultCashFlowsIndex: Int = 0
    private val defaultNumberCashFlows: Int = 0

    private val defaultMeters: Int = 0
    private val defaultUnusableMeters: Int = 0

    private val defaultTotalTime: Int = 0
    private val defaultStartTime: Int = 0
    private val defaultEndTime: Int = 0

    private val defaultCashFlows: Int = 0 //
    private val defaultSum: Int = 0 // общая сумма
    private val defaultTransferSum: Int = 0 //

    private val mainSharedPreferencesName: String = "main"

    fun isCreated (sharedPreferencesName: String): Boolean {
        //Log.d("My_test", "sharedPreferences count: " + File(context.filesDir?.parent + "/shared_prefs/").list()?.size.toString())
        val sharedPreference = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        return sharedPreference.all.isNotEmpty()

    }

    fun findRouteSharedPrefs (typeOfSharedPreferences: String) : List<SharedPreferences> {
        val listOfSharedPreferences: MutableList<SharedPreferences> = mutableListOf()
        val sharedPreferencesAll: Array<out String> = File(context.filesDir?.parent + "/shared_prefs/").list()!!

        Log.d("My_test", "sharedPreferencesAll count: ${sharedPreferencesAll.size}")
        val findText = "${typeOfSharedPreferences}_"
        for (i in sharedPreferencesAll){
            if (i.contains(findText)) {
                val sharedPreferences = context.getSharedPreferences(i.replace(".xml", ""), Context.MODE_PRIVATE)
                listOfSharedPreferences.add(sharedPreferences)
            }
        }
        Log.d("My_test", "listOfSharedPreferences count: ${listOfSharedPreferences.size}")
        return if (listOfSharedPreferences.size > 0){
            listOfSharedPreferences.sortedBy { it.getInt("${findText}index", 0) }}
        else { listOf() }
    }

    fun createDefaultMainPref () {
        val mainPrefs = context.getSharedPreferences(mainSharedPreferencesName, Context.MODE_PRIVATE)
        val mainPrefsEditor = mainPrefs.edit()
        mainPrefsEditor.putString("type_shared_prefs", defaultTypeSharedPreferences)
        mainPrefsEditor.putInt("route_index", defaultRouteIndex)
        mainPrefsEditor.putInt("number_route", defaultNumberRoute)
        mainPrefsEditor.putInt("cash_flows_index", defaultCashFlowsIndex)
        mainPrefsEditor.putInt("number_cash_flows", defaultNumberCashFlows)

        mainPrefsEditor.putInt("meters", defaultMeters)
        mainPrefsEditor.putInt("unusable_meters", defaultUnusableMeters)

        mainPrefsEditor.putInt("total_time", defaultTotalTime)
        mainPrefsEditor.putInt("start_time", defaultStartTime)
        mainPrefsEditor.putInt("end_time", defaultEndTime)

        mainPrefsEditor.putInt("cash_flows", defaultCashFlows)
        mainPrefsEditor.putInt("sum", defaultSum)
        mainPrefsEditor.putInt("transfer_sum", defaultTransferSum)
        mainPrefsEditor.commit()
    }

    fun deleteSharedPreferences (sharedPrefsOfRouteObject: SharedPreferences? = null, deleteAllRoute: Boolean = false,
                                 deleteAllConsumption:Boolean = false, clearAllSharedPreferences: Boolean = false) {
        val sharedPreferencesDir = File(context.filesDir?.parent + "/shared_prefs/")

        fun deleteSharedPrefs(sharedPrefs: SharedPreferences, nameOfSharedPrefs: String){
            val fileSharedPrefs = File("$sharedPreferencesDir/$nameOfSharedPrefs.xml")
            sharedPrefs.edit().clear().commit()
            fileSharedPrefs.delete()
        }

        if (clearAllSharedPreferences){
            for (i in sharedPreferencesDir.list()!!){
                val sharedPrefsName: String = i.replace(".xml", "")
                val deletedSharedPrefs = context.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE)
                deleteSharedPrefs(deletedSharedPrefs, sharedPrefsName)
            }
        }

        if (deleteAllRoute) {
            val typeSharedPrefs = "route"
            val findSharedPrefs = findRouteSharedPrefs(typeSharedPrefs)
            for (i in findSharedPrefs) {
                val indexOfSharedPrefs = i.getInt("${typeSharedPrefs}_index", 0)
                val sharedPrefsName = "${typeSharedPrefs}_$indexOfSharedPrefs"
                deleteSharedPrefs(i, sharedPrefsName)
            }
        }

        if(deleteAllConsumption){
            val typeSharedPrefs = "consumption"
            val findSharedPrefs = findRouteSharedPrefs(typeSharedPrefs)
            for (i in findSharedPrefs) {
                val indexOfSharedPrefs = i.getInt("${typeSharedPrefs}_index", 0)
                val sharedPrefsName = "${typeSharedPrefs}_$indexOfSharedPrefs"
                deleteSharedPrefs(i, sharedPrefsName)
            }
        }

        if (sharedPrefsOfRouteObject != null){
            val typeSharedPrefs = sharedPrefsOfRouteObject.getString("type_shared_prefs", "none")
            if (typeSharedPrefs != "none"){
                val indexOfSharedPrefs = sharedPrefsOfRouteObject.getInt("${typeSharedPrefs}_index", 0)
                val sharedPrefsName = "${typeSharedPrefs}_$indexOfSharedPrefs"
                deleteSharedPrefs(sharedPrefsOfRouteObject, sharedPrefsName)
            }
        }
    }

    fun putDataInMainPref (map: Map<String, Int>) {
        val mainPrefs = context.getSharedPreferences(mainSharedPreferencesName, Context.MODE_PRIVATE)
        val mainPrefsEditor = mainPrefs.edit()
        for ((key, item) in map){
            if (key == "route_index" || key == "cash_flows_index" || key == "total_time"
                || key == "start_time" || key == "end_time") {
                    mainPrefsEditor.putInt(key,item)
            }
            else {
                val oldItem: Int = mainPrefs.getInt(key, 0)
                mainPrefsEditor.putInt(key, oldItem + item)
            }
        }
        mainPrefsEditor.apply()
    }

    fun routeSharedPrefEditor (map: Map<String, Any>): SharedPreferences {
        val type = map["type_shared_prefs"] as String
        val pref = context.getSharedPreferences(type + "_${map[type + "_index"]}", Context.MODE_PRIVATE)

        val prefEditor = pref.edit()
        for ((key, item) in map){
            when (item) {
                is Int -> {
                    prefEditor.putInt(key, item)
                }
                is String -> {
                    prefEditor.putString(key, item)
                }
                is Boolean -> {
                    prefEditor.putBoolean(key, item)
                }
            }
        }
        //prefEditor.apply()
        prefEditor.commit()
        return pref
    }

    fun getLastRouteIndex(): Int {
        val routeSharedPreferences = findRouteSharedPrefs("route")
        if (routeSharedPreferences.isNotEmpty()) {
            return routeSharedPreferences.last().getInt("route_index", 0)
        }
        return 0
    }

    fun getLastConsumptionIndex(): Int {
        val routeSharedPreferences = findRouteSharedPrefs("consumption")
        if (routeSharedPreferences.isNotEmpty()) {
            return routeSharedPreferences.last().getInt("consumption_index", 0)
        }
        return 0
    }
}


abstract class SharedPreferenceLiveData<T>(val sharedPrefs: SharedPreferences,
                                           val key: String,
                                           val defValue: T) : LiveData<T>() {

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == this.key) {
            value = getValueFromPreferences(key, defValue)
        }
    }

    abstract fun getValueFromPreferences(key: String, defValue: T): T

    override fun onActive() {
        super.onActive()
        value = getValueFromPreferences(key, defValue)
        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onInactive() {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
        super.onInactive()
    }
}

class SharedPreferenceIntLiveData(sharedPrefs: SharedPreferences, key: String, defValue: Int) :
    SharedPreferenceLiveData<Int> (sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: Int): Int = sharedPrefs.getInt(key, defValue)
}

class SharedPreferenceStringLiveData(sharedPrefs: SharedPreferences, key: String, defValue: String) :
    SharedPreferenceLiveData<String>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: String): String = sharedPrefs.getString(key, defValue)!!
}

class SharedPreferenceBooleanLiveData(sharedPrefs: SharedPreferences, key: String, defValue: Boolean) :
    SharedPreferenceLiveData<Boolean>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: Boolean): Boolean = sharedPrefs.getBoolean(key, defValue)
}

class SharedPreferenceFloatLiveData(sharedPrefs: SharedPreferences, key: String, defValue: Float) :
    SharedPreferenceLiveData<Float>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: Float): Float = sharedPrefs.getFloat(key, defValue)
}

class SharedPreferenceLongLiveData(sharedPrefs: SharedPreferences, key: String, defValue: Long) :
    SharedPreferenceLiveData<Long>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: Long): Long = sharedPrefs.getLong(key, defValue)
}

class SharedPreferenceStringSetLiveData(sharedPrefs: SharedPreferences, key: String, defValue: Set<String>) :
    SharedPreferenceLiveData<Set<String>>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: Set<String>): Set<String> = sharedPrefs.getStringSet(key, defValue)!!
}

fun SharedPreferences.intLiveData(key: String, defValue: Int): SharedPreferenceLiveData<Int> {
    return SharedPreferenceIntLiveData(this, key, defValue)
}

fun SharedPreferences.stringLiveData(key: String, defValue: String): SharedPreferenceLiveData<String> {
    return SharedPreferenceStringLiveData(this, key, defValue)
}

fun SharedPreferences.booleanLiveData(key: String, defValue: Boolean): SharedPreferenceLiveData<Boolean> {
    return SharedPreferenceBooleanLiveData(this, key, defValue)
}

fun SharedPreferences.floatLiveData(key: String, defValue: Float): SharedPreferenceLiveData<Float> {
    return SharedPreferenceFloatLiveData(this, key, defValue)
}

fun SharedPreferences.longLiveData(key: String, defValue: Long): SharedPreferenceLiveData<Long> {
    return SharedPreferenceLongLiveData(this, key, defValue)
}

fun SharedPreferences.stringSetLiveData(key: String, defValue: Set<String>): SharedPreferenceLiveData<Set<String>> {
    return SharedPreferenceStringSetLiveData(this, key, defValue)
}