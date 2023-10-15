package com.osees.musterman.ui.route

import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.lifecycle.*
import com.osees.musterman.MainSharedPreferences

class RouteViewModel : ViewModel() {

    private lateinit var mainSharedPreferences: MainSharedPreferences

    private val defaultButtonSample: Map<Int, List<Int>> = mapOf(
        //Arg1 - горячие, Arg2 - холодные, Arg3 - горячие н/г,Arg4 - холодные н/г
        0 to listOf(2, 0, 0, 0),
        1 to listOf(0, 2, 0, 0),
        2 to listOf(1, 1, 0, 0),
        3 to listOf(0, 1, 1, 0),
        4 to listOf(1, 0, 0, 1),
        5 to listOf(2, 2, 0, 0))
    private val defaultPrice: Int = 600
    private val defaultDiscount: Int = 50
    private val defaultRouteIndex: Int = 1
    private val defaultTransferSum: Int = 0
    private val defaultHot: Int = 0
    private val defaultCold: Int = 0
    private val defaultUnusableHot: Int = 0
    private val defaultUnusableCold: Int = 0
    private val defaultDiscountCheck: Boolean = false
    private val defaultCauseUnusableHot: String = ""
    private val defaultCauseUnusableCold: String = ""
    private val defaultLocation: String = "Екатеринбург"

    private val defaultConsumptionSum: Int = 0
    private val defaultConsumptionCause: String = "Еда"
    private val defaultConsumptionIsProfitCheck: Boolean = false
    private val defaultConsumptionIndex: Int = 1

    private val defaultBottomRouteOpened: Boolean = false
    private val defaultBottomConsumptionOpened: Boolean = false


    private val _linearLayoutLive = MutableLiveData<LinearLayout>().apply {
        value = null
    }
    val linearLayoutLive: LiveData<LinearLayout> = _linearLayoutLive

    fun addLinearLayoutForLiveData(linearLayout: LinearLayout){
        _linearLayoutLive.value = linearLayout
    }

    val routeObjects: MutableList<Map<String, Any>> = mutableListOf()

    fun addRouteInLinearLayout (route: ConstraintLayout?){
        _linearLayoutLive.value?.addView(route)
        Log.d("My_tag", "child linear layout: " + linearLayoutLive.value?.childCount)
    }

    fun findLiveMapByViewId (viewId: Int): Map<String, Any>? {
        var liveMap: Map<String, Any>? = null
        for(i in routeObjects){
            if(i["view_id"] == viewId){
                liveMap = i
                break
            }
        }
        return liveMap
    }

    fun findLiveMapByRouteId (routeId: Int): Map<String, Any>? {
        var liveMap: Map<String, Any>? = null
        for(i in routeObjects){
            if(i["route_id"] == routeId){
                liveMap = i
                break
            }
        }
        return liveMap
    }

    fun deleteRouteInLinearLayout (routeObject: View){
        _linearLayoutLive.value?.removeView(routeObject)
    }

    fun deleteSharedPreferencesOfRoute (sharedPreferences: SharedPreferences){
        mainSharedPreferences.deleteSharedPreferences(sharedPreferences)
    }
    fun deleteRouteObjectInCell (routeObjectIndex: Int){

    }

    fun createMapLiveOfRoute (map: Map<String, *>): MutableLiveData<Map<String, *>> {
        val mutableMapLiveData = MutableLiveData<Map<String, *>>().apply {
            value = map
        }
        return mutableMapLiveData
    }

    fun addRoutsObject(sharedPreferences: SharedPreferences,routeId: Int, routeObject: View, map: MutableLiveData<Map<String, *>>){
        val finalMap = mapOf(
            "shared_preferences" to sharedPreferences,
            "route_id" to routeId,
            "route_object" to routeObject,
            "map" to map)
        //routeObjects.plus(finalMap)
        routeObjects.add(finalMap)
    }

    private val _buttonSampleLive = MutableLiveData<Map<Int, List<Int>>>().apply {
        value = defaultButtonSample
    }
    val buttonSampleLive: LiveData<Map<Int, List<Int>>> = _buttonSampleLive


    private val _priceLive = MutableLiveData<Int>().apply {
        value = defaultPrice
    }
    val priceLive: LiveData<Int> = _priceLive

    private val _routeIndexLive = MutableLiveData<Int>().apply {
        value = defaultRouteIndex
    }
    val routeIndexLive: LiveData<Int> = _routeIndexLive

    private val _consumptionIndexLive = MutableLiveData<Int>().apply {
        value = defaultConsumptionIndex
    }
    val consumptionIndexLive: LiveData<Int> = _consumptionIndexLive

    private val _transferSumLive = MutableLiveData<Int>().apply {
        value = defaultTransferSum
    }
    val transferSumLive: LiveData<Int> = _transferSumLive

    private val _hotLive = MutableLiveData<Int>().apply {
        value = defaultHot
    }
    val hotLive: LiveData<Int> = _hotLive

    private val _coldLive = MutableLiveData<Int>().apply {
        value = defaultCold
    }
    val coldLive: LiveData<Int> = _coldLive

    private val _unusableHotLive = MutableLiveData<Int>().apply {
        value = defaultUnusableHot
    }
    val unusableHotLive: LiveData<Int> = _unusableHotLive

    private val _unusableColdLive = MutableLiveData<Int>().apply {
        value = defaultUnusableCold
    }
    val unusableColdLive: LiveData<Int> = _unusableColdLive

    private val _discountLive = MutableLiveData<Int>().apply {
        value = defaultDiscount
    }
    val discountLive: LiveData<Int> = _discountLive

    private val _discountCheckLive = MutableLiveData<Boolean>().apply {
        value = defaultDiscountCheck
    }
    val discountCheckLive: LiveData<Boolean> = _discountCheckLive

    private val _causeUnusableHotLive = MutableLiveData<String>().apply {
        value = defaultCauseUnusableHot
    }
    val causeUnusableHotLive: LiveData<String> = _causeUnusableHotLive

    private val _causeUnusableColdLive = MutableLiveData<String>().apply {
        value = defaultCauseUnusableCold
    }
    val causeUnusableColdLive: LiveData<String> = _causeUnusableColdLive


    private val _consumptionSumLive = MutableLiveData<Int>().apply {
        value = defaultConsumptionSum
    }
    val consumptionSumLive: LiveData<Int> = _consumptionSumLive

    private val _consumptionCauseLive = MutableLiveData<String>().apply {
        value = defaultConsumptionCause
    }
    val consumptionCauseLive: LiveData<String> = _consumptionCauseLive

    private val _consumptionIsProfitCheckLive = MutableLiveData<Boolean>().apply {
        value = defaultConsumptionIsProfitCheck
    }
    val consumptionIsProfitCheckLive: LiveData<Boolean> = _consumptionIsProfitCheckLive

    private val _bottomRouteOpenedLive = MutableLiveData<Boolean>().apply {
        value = defaultBottomRouteOpened
    }
    val bottomRouteOpenedLive: LiveData<Boolean> = _bottomRouteOpenedLive

    private val _bottomConsumptionOpenedLive = MutableLiveData<Boolean>().apply {
        value = defaultBottomConsumptionOpened
    }
    val bottomConsumptionOpenedLive: LiveData<Boolean> = _bottomConsumptionOpenedLive

    private val _locationLive = MutableLiveData<String>().apply {
        value = defaultLocation
    }
    val locationLive: LiveData<String> = _locationLive

    fun setIntValue(key: String, value: Int?): Boolean {
        /** available keys:
         * "price"
         * "discount"
         * "route_index"
         * "transfer_sum"
         * "hot"
         * "cold"
         * "unusable_hot"
         * "unusable_cold"
         * "consumption_sum"
         * "consumption_index"
         */
        return if (value == null){ false } else {
            when (key) {
                "price" -> _priceLive.value = value
                "discount" -> _discountLive.value = value
                "route_index" -> _routeIndexLive.value = value
                "transfer_sum" -> _transferSumLive.value = value
                "hot" -> _hotLive.value = value
                "cold" -> _coldLive.value = value
                "unusable_hot" -> _unusableHotLive.value = value
                "unusable_cold" -> _unusableColdLive.value = value
                "consumption_sum" -> _consumptionSumLive.value = value
                "consumption_index" -> _consumptionIndexLive.value = value
            }
            true
        }
    }
    fun setBooleanValue (key: String, value: Boolean?): Boolean {
        return if (value == null){ false } else {
            when (key) {
                /** available keys:
                 * "discount_check"
                 * "consumption_is_profit_check"
                 * "bottom_route_opened"
                 * "bottom_consumption_opened"
                 */
                "discount_check" -> _discountCheckLive.value = value
                "consumption_is_profit_check" -> _consumptionIsProfitCheckLive.value = value
                "bottom_route_opened" -> _bottomRouteOpenedLive.value = value
                "bottom_consumption_opened" -> _bottomConsumptionOpenedLive.value = value
            }
            true
        }
    }
    fun setStringValue (key: String, value: String?): Boolean {
        return if (value == null){ false } else {
            when (key) {
                /** available keys:
                 * "cause_unusable_hot"
                 * "cause_unusable_cold"
                 * "consumption_cause"
                 * "location"
                 */
                "cause_unusable_hot" -> _causeUnusableHotLive.value = value
                "cause_unusable_cold" -> _causeUnusableColdLive.value = value
                "consumption_cause" -> _consumptionCauseLive.value = value
                "location" -> _locationLive.value = value
            }
            true
        }
    }

    fun getIntValue (key: String) : Int {
        /** available keys:
         * "price"
         * "discount"
         * "route_index"
         * "transfer_sum"
         * "hot"
         * "cold"
         * "unusable_hot"
         * "unusable_cold"
         * "sum"
         * "consumption_sum"
         * "consumption_index"
         */
        val value: Int? = when (key) {
            "price" -> priceLive.value
            "discount" -> discountLive.value
            "route_index" -> routeIndexLive.value
            "transfer_sum" -> transferSumLive.value
            "hot" -> hotLive.value
            "cold" -> coldLive.value
            "unusable_hot" -> unusableHotLive.value
            "unusable_cold" -> unusableColdLive.value
            "sum" -> sumLive.value
            "consumption_sum" -> consumptionSumLive.value
            "consumption_index" -> consumptionIndexLive.value
            else -> { 0 }
        }
        return value ?: 0
    }
    fun getBooleanValue (key: String) : Boolean {
        val value: Boolean? = when (key) {
            /** available keys:
             * "discount_check"
             * "consumption_is_profit_check"
             * "bottom_route_opened"
             * "bottom_consumption_opened"
             * */
            "discount_check" -> discountCheckLive.value
            "consumption_is_profit_check" -> consumptionIsProfitCheckLive.value
            "bottom_route_opened" -> bottomRouteOpenedLive.value
            "bottom_consumption_opened" -> bottomConsumptionOpenedLive.value
            else -> { false }
        }
        return value ?: false
    }
    fun getStringValue (key: String) : String {
        val value: String? = when (key) {
            /** available keys:
             * "cause_unusable_hot"
             * "cause_unusable_cold"
             * "consumption_cause"
             */
            "cause_unusable_hot" -> causeUnusableHotLive.value
            "cause_unusable_cold" -> causeUnusableColdLive.value
            "consumption_cause" -> consumptionCauseLive.value
            else -> { "" }
        }
        return value ?: ""
    }

    fun setDefaultRoute(setDefaultIndex: Boolean = false){
        if (setDefaultIndex){
            setIntValue("route_index", defaultRouteIndex)
        }
        setIntValue("price", defaultPrice)
        setIntValue("discount", defaultDiscount)
        setIntValue("transfer_sum", defaultTransferSum)
        setIntValue("hot", defaultHot)
        setIntValue("cold", defaultCold)
        setIntValue("unusable_hot", defaultUnusableHot)
        setIntValue("unusable_cold", defaultUnusableCold)
        setBooleanValue("discount_check", defaultDiscountCheck)
        setStringValue("cause_unusable_hot", defaultCauseUnusableHot)
        setStringValue("cause_unusable_cold", defaultCauseUnusableCold)
    }

    fun setDefaultConsumption(setDefaultIndex: Boolean = false){
        if (setDefaultIndex){
            setIntValue("consumption_index", defaultConsumptionIndex)
        }
        setIntValue("consumption_sum", defaultConsumptionSum)
        setBooleanValue("consumption_is_profit_check", defaultConsumptionIsProfitCheck)
        setStringValue("consumption_cause", defaultConsumptionCause)

    }

    private val _sumLive = MediatorLiveData<Int>()
    init {
        _sumLive.addSource(_discountLive) { _sumLive.value = finalSum() }
        _sumLive.addSource(_discountCheckLive) { _sumLive.value = finalSum() }
        _sumLive.addSource(_coldLive) { _sumLive.value = finalSum() }
        _sumLive.addSource(_hotLive) { _sumLive.value = finalSum() }
        _sumLive.addSource(_priceLive) { _sumLive.value = finalSum() }
    }



    private fun finalSum(): Int{
        val sum: Int = if (discountCheckLive.value == false){
            priceLive.value!! * (hotLive.value!! + coldLive.value!!)
        } else{
            (priceLive.value!! - discountLive.value!!) * (hotLive.value!! + coldLive.value!!)
        }
        return sum
    }

    //val sumLive: MediatorLiveData<List<Any>> = _sumLive
    val sumLive: LiveData<Int> = _sumLive
}