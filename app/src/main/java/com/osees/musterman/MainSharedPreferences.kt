package com.osees.musterman

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.util.Log
import android.view.LayoutInflater

import androidx.lifecycle.LiveData
import com.osees.musterman.databinding.RouteItemBinding
import com.osees.musterman.ui.route.RouteViewModel
import java.io.File

class MainSharedPreferences (private val context: Context) {


    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val defaultTypeSharedPreferences: String = "main"

    private val defaultHot: Int = 0
    private val defaultCold: Int = 0
    private val defaultSalary: Int = 120
    private val defaultPetrol: Int = 350

    private val defaultUnusableHot: Int = 0
    private val defaultUnusableCold: Int = 0

    private val defaultProcessedRoutes: Int = 0

    private val defaultTotalTime: String = "00:00"
    private val defaultStartTime: String = "00:00"
    private val defaultEndTime: String = "00:00"

    private val defaultProfit: Int = 0
    private val defaultLoss: Int = 0

    private val defaultSum: Int = 0 // общая сумма
    private val defaultTransferSum: Int = 0 //

    private val mainSharedPreferencesName: String = "main"

    private val defaultBasePetrol = 350

    fun isCreated (sharedPreferencesName: String): Boolean {
        //Log.d("My_test", "sharedPreferences count: " + File(context.filesDir?.parent + "/shared_prefs/").list()?.size.toString())
        val sharedPreference = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        return sharedPreference.all.isNotEmpty()

    }


    private fun setDefaultValuesForMainSharedPrefs(mainSharedPreferencesEditor: SharedPreferences.Editor){
        mainSharedPreferencesEditor.putString("type_shared_prefs", defaultTypeSharedPreferences)

        mainSharedPreferencesEditor.putInt("hot", defaultHot)
        mainSharedPreferencesEditor.putInt("cold", defaultCold)

        mainSharedPreferencesEditor.putInt("salary", defaultSalary)
        mainSharedPreferencesEditor.putInt("petrol", defaultPetrol)

        mainSharedPreferencesEditor.putInt("unusable_hot", defaultUnusableHot)
        mainSharedPreferencesEditor.putInt("unusable_cold", defaultUnusableCold)

        mainSharedPreferencesEditor.putInt("processed_routes", defaultProcessedRoutes)

        mainSharedPreferencesEditor.putString("total_time", defaultTotalTime)
        mainSharedPreferencesEditor.putString("start_time", defaultStartTime)
        mainSharedPreferencesEditor.putString("end_time", defaultEndTime)

        mainSharedPreferencesEditor.putInt("profit", defaultProfit)
        mainSharedPreferencesEditor.putInt("loss", defaultLoss)

        mainSharedPreferencesEditor.putInt("sum", defaultSum)
        mainSharedPreferencesEditor.putInt("transfer_sum", defaultTransferSum)
    }

    fun clearMainSharedPreferences(){
        val mainPrefs = context.getSharedPreferences(mainSharedPreferencesName, Context.MODE_PRIVATE)
        val mainPrefsEditor = mainPrefs.edit()
        mainPrefsEditor.clear()
        setDefaultValuesForMainSharedPrefs(mainPrefsEditor)
        mainPrefsEditor.apply()
    }

    fun putInMainSharedPrefs(map: Map<String, Any>){
        val mainPrefs = context.getSharedPreferences(mainSharedPreferencesName, Context.MODE_PRIVATE)
        val mainPrefsEditor = mainPrefs.edit()
        for ((key, item) in map){
            when (item) {
                is Int -> {
                    val oldData = mainPrefs.getInt(key, 0)
                    val newData = oldData + item
                    mainPrefsEditor.putInt(key, newData)
                }
                is String -> {
                    mainPrefsEditor.putString(key, item)
                }
                is Boolean -> {
                    mainPrefsEditor.putBoolean(key, item)
                }
            }
        }
        mainPrefsEditor.commit()
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
        setDefaultValuesForMainSharedPrefs(mainPrefsEditor)
        mainPrefsEditor.commit()
    }

    fun deleteSharedPreferences (sharedPrefsOfRouteObject: SharedPreferences? = null, deleteAllRoute: Boolean = false,
                                 deleteAllConsumption:Boolean = false, deleteAllSharedPreferences: Boolean = false) {
        val sharedPreferencesDir = File(context.filesDir?.parent + "/shared_prefs/")

        fun deleteSharedPrefs(sharedPrefs: SharedPreferences, nameOfSharedPrefs: String){
            val fileSharedPrefs = File("$sharedPreferencesDir/$nameOfSharedPrefs.xml")
            sharedPrefs.edit().clear().commit()
            fileSharedPrefs.delete()
        }

        if (deleteAllSharedPreferences){
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

    fun routeSharedPrefEditor (map: Map<String, Any>): SharedPreferences {
        val type = map["type_shared_prefs"] as String
        val name: String = type + "_${map[type + "_index"]}"
        val isEdit: Boolean = isCreated(name)
        val pref = context.getSharedPreferences(name, Context.MODE_PRIVATE)

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

    fun routeSharedPrefEditor1 (map: Map<String, Any>): Pair<String, Boolean> {
        val type = map["type_shared_prefs"] as String
        val name: String = type + "_${map[type + "_index"]}"
        val pref = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        val isEdit: Boolean = pref.all.isNotEmpty()

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
        return Pair(name, isEdit)
    }



    fun routeEditorWithLiveData(isEdit: Boolean, nameShared: String) {
        val sharedPreference = context.getSharedPreferences(nameShared, Context.MODE_PRIVATE)
        val map: Map<String, Any> = sharedPreference.all as Map<String, Any>

        /**EXAMPLE MAP<String, Int> for route object:
         *    mapOf(
         *       "route_index"   to 1,
         *       "current_time"  to 22:10
         *       "elapsed_time"  to 1200,
         *       "price"         to 600,
         *       "sum"           to 1200,
         *       "transfer_sum"  to 600,
         *      "discount_check" to false,
         *       "discount"      to 0,
         *       "hot"           to 2,
         *       "cold"          to 0,
         *       "unusable_hot"  to 1,
         *       "unusable_cold" to 0
         *       "cause_unusable_hot"  to "определение отн. погрешности",
         *       "cause_unusable_cold" to ""
         *    )
         */


        val routeIndex: Int = map["route_index"] as Int
        val price: Int = map["price"] as Int
        val hot: Int = map["hot"] as Int
        val cold: Int= map["cold"] as Int
        val discount: Int = map["discount"]  as Int
        val discountCheck: Boolean = map["discount_check"] as Boolean
        val unusableHot: Int = map["unusable_hot"] as Int
        val unusableCold: Int= map["unusable_cold"] as Int
        val sum: Int = map["sum"] as Int
        val transferSum: Int = map["transfer_sum"] as Int

        val currentTime: String = map["current_time"] as String
        val elapsedTime: Int = map["elapsed_time"] as Int
        val causeUnusableHot = map["cause_unusable_hot"] as String
        val causeUnusableCold = map["cause_unusable_cold"] as String
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

fun SharedPreferences.Editor.putMap(sharedPrefs: SharedPreferences, map: Map<String, *>){

    val prefEditor = sharedPrefs.edit()
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
}

abstract class SharedPreferences(): SharedPreferences.Editor {
    class putMap(map: Map<String, *>){
        fun putMap(map: Map<String, *>){}

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