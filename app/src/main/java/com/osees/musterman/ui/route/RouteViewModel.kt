package com.osees.musterman.ui.route

import androidx.lifecycle.*

class RouteViewModel : ViewModel() {

    private val buttonSample: Map<Int, List<Int>> = mapOf(
        //Arg1 - горячие, Arg2 - холодные, Arg3 - горячие н/г,Arg4 - холодные н/г
        0 to listOf(2, 0, 0, 0),
        1 to listOf(0, 2, 0, 0),
        2 to listOf(1, 1, 0, 0),
        3 to listOf(0, 1, 1, 0),
        4 to listOf(1, 0, 0, 1),
        5 to listOf(2, 2, 0, 0))

    private val price: Int = 600
    private val discount: Int = 50
    private val routeIndex: Int = 1
    private val transferSum: Int = 0
    private val hot: Int = 0
    private val cold: Int = 0
    private val unusableHot: Int = 0
    private val unusableCold: Int = 0

    private val discountCheck: Boolean = false

    private val causeUnusableHot: String = ""
    private val causeUnusableCold: String = ""


    private val _buttonSampleLive = MutableLiveData<Map<Int, List<Int>>>().apply {
        value = buttonSample
    }
    val buttonSampleLive: LiveData<Map<Int, List<Int>>> = _buttonSampleLive

    private val _priceLive = MutableLiveData<Int>().apply {
        value = price
    }
    val priceLive: LiveData<Int> = _priceLive

    private val _routeIndexLive = MutableLiveData<Int>().apply {
        value = routeIndex
    }
    val routeIndexLive: LiveData<Int> = _routeIndexLive

    private val _transferSumLive = MutableLiveData<Int>().apply {
        value = transferSum
    }
    val transferSumLive: LiveData<Int> = _transferSumLive

    private val _hotLive = MutableLiveData<Int>().apply {
        value = hot
    }
    val hotLive: LiveData<Int> = _hotLive

    private val _coldLive = MutableLiveData<Int>().apply {
        value = cold
    }
    val coldLive: LiveData<Int> = _coldLive

    private val _unusableHotLive = MutableLiveData<Int>().apply {
        value = unusableHot
    }
    val unusableHotLive: LiveData<Int> = _unusableHotLive

    private val _unusableColdLive = MutableLiveData<Int>().apply {
        value = unusableCold
    }
    val unusableColdLive: LiveData<Int> = _unusableColdLive

    private val _discountLive = MutableLiveData<Int>().apply {
        value = discount
    }
    val discountLive: LiveData<Int> = _discountLive

    private val _discountCheckLive = MutableLiveData<Boolean>().apply {
        value = discountCheck
    }
    val discountCheckLive: LiveData<Boolean> = _discountCheckLive

    private val _causeUnusableHotLive = MutableLiveData<String>().apply {
        value = causeUnusableHot
    }
    val causeUnusableHotLive: LiveData<String> = _causeUnusableHotLive

    private val _causeUnusableColdLive = MutableLiveData<String>().apply {
        value = causeUnusableCold
    }
    val causeUnusableColdLive: LiveData<String> = _causeUnusableColdLive

    fun editHot(hot: Int){
        _hotLive.value = hot
    }

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
            }
            true
        }
    }
    fun setBooleanValue (key: String, value: Boolean?): Boolean {
        return if (value == null){ false } else {
            when (key) {
                /** available keys:
                 * "discount_check"
                 */
                "discount_check" -> _discountCheckLive.value = value
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
                 */
                "cause_unusable_hot" -> _causeUnusableHotLive.value = value
                "cause_unusable_cold" -> _causeUnusableColdLive.value = value
            }
            true
        }
    }

    fun getIntValue (key: String) : Int {
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
            else -> { 0 }
        }
        return value ?: 0
    }
    fun getBooleanValue (key: String) : Boolean {
        val value: Boolean? = when (key) {
            /** available keys:
             * "discount_check"*/
            "discount_check" -> discountCheckLive.value
            else -> { false }
        }
        return value ?: false
    }
    fun getStringValue (key: String) : String {
        val value: String? = when (key) {
            /** available keys:
             * "cause_unusable_hot"
             * "cause_unusable_cold"
             */
            "cause_unusable_hot" -> _causeUnusableHotLive.value
            "cause_unusable_cold" -> _causeUnusableColdLive.value
            else -> { "" }
        }
        return value ?: ""
    }



    private val _sumLive = MediatorLiveData<Int>()
    init {
        _sumLive.addSource(_discountLive, Observer {_sumLive.value = finalSum()})
        _sumLive.addSource(_discountCheckLive, Observer {_sumLive.value = finalSum()})
        _sumLive.addSource(_coldLive, Observer {_sumLive.value = finalSum()})
        _sumLive.addSource(_hotLive, Observer {_sumLive.value = finalSum()})
        _sumLive.addSource(_priceLive, Observer {_sumLive.value = finalSum()})
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