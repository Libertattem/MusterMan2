package com.osees.musterman.ui.route

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

class RouteViewModel : ViewModel() {

    private val buttonSample: Map<Int, List<Int>> = mapOf(
        //Arg1 - горячие, Arg2 - холодные, Arg3 - горячие н/г,Arg4 - холодные н/г
        0 to listOf(2, 0, 0, 0),
        1 to listOf(0, 2, 0, 0),
        2 to listOf(1, 1, 0, 0),
        3 to listOf(0, 1, 1, 0),
        4 to listOf(1, 0, 0, 1),
        5 to listOf(2, 2, 0, 0))

    private val mainPrice: Int = 600
    private val discount: Int = 50
    private val routeIndex: Int = 1
    private val hot: Int = 0
    private val cold: Int = 0
    private val unusableHot: Int = 0
    private val unusableCold: Int = 0
    private val discountCheck: Boolean = false
    private val meterVerification: ArrayList<String> = arrayListOf("0", "1", "2", "3", "4", "5", "6")

    private val _buttonSampleLive = MutableLiveData<Map<Int, List<Int>>>().apply {
        value = buttonSample
    }
    val buttonSampleLive: MutableLiveData<Map<Int, List<Int>>> = _buttonSampleLive

    private val _mainPriceLive = MutableLiveData<Int>().apply {
        value = mainPrice
    }
    val mainPriceLive: MutableLiveData<Int> = _mainPriceLive

    private val _routeIndexLive = MutableLiveData<Int>().apply {
        value = routeIndex
    }
    val routeIndexLive: MutableLiveData<Int> = _routeIndexLive

    private val _hotLive = MutableLiveData<Int>().apply {
        value = hot
    }
    val hotLive: MutableLiveData<Int> = _hotLive

    private val _coldLive = MutableLiveData<Int>().apply {
        value = cold
    }
    val coldLive: MutableLiveData<Int> = _coldLive

    private val _unusableHotLive = MutableLiveData<Int>().apply {
        value = unusableHot
    }
    val unusableHotLive: MutableLiveData<Int> = _unusableHotLive

    private val _unusableColdLive = MutableLiveData<Int>().apply {
        value = unusableHot
    }
    val unusableColdLive: MutableLiveData<Int> = _unusableColdLive

    private val _discountLive = MutableLiveData<Int>().apply {
        value = discount
    }
    val discountLive: MutableLiveData<Int> = _discountLive

    private val _discountCheckLive = MutableLiveData<Boolean>().apply {
        value = discountCheck
    }
    val discountCheckLive: MutableLiveData<Boolean> = _discountCheckLive

    private val _meterVerificationLive = MutableLiveData<ArrayList<String>>().apply {
        value = meterVerification
    }
    val meterVerificationLive: LiveData<ArrayList<String>> = _meterVerificationLive


    private val _sumLive = MediatorLiveData<Int>()
    init {
        _sumLive.addSource(_discountLive, Observer {_sumLive.value = finalSum()})
        _sumLive.addSource(_discountCheckLive, Observer {_sumLive.value = finalSum()})
        _sumLive.addSource(_coldLive, Observer {_sumLive.value = finalSum()})
        _sumLive.addSource(_hotLive, Observer {_sumLive.value = finalSum()})
        _sumLive.addSource(_mainPriceLive, Observer {_sumLive.value = finalSum()})
    }

    private fun combine (discount: Int, discountCheck: Boolean, cold: Int, hot: Int, price: Int): List<Any>{
        return listOf<Any>(discount, discountCheck, cold, hot, price)
    }

    private fun finalSum(): Int{
        val sum: Int = if (discountCheckLive.value == false){
            mainPriceLive.value!! * (hotLive.value!! + coldLive.value!!)
        } else{
            (mainPriceLive.value!! - discountLive.value!!) * (hotLive.value!! + coldLive.value!!)
        }
        return sum
    }

    //val sumLive: MediatorLiveData<List<Any>> = _sumLive
    val sumLive: MediatorLiveData<Int> = _sumLive
}