package com.osees.musterman.ui.route

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.PopupMenu
import android.widget.Toast
import com.osees.musterman.MainSharedPreferences
import com.osees.musterman.R
import com.osees.musterman.databinding.ConsumptionItemBinding
import com.osees.musterman.databinding.RouteItemBinding
import java.time.format.DateTimeFormatter
import java.util.StringJoiner

class RouteCreatorObjects (private val root: ViewGroup, private val context: Context, private val routeViewModel: RouteViewModel) {

    private val mainSharedPrefs = MainSharedPreferences(context)

    @SuppressLint("SetTextI18n")
    fun createRouteObject(routeSharedPreferences: SharedPreferences){
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

        val map: Map<String, Any> = routeSharedPreferences.all as Map<String, Any>
        val inflater = LayoutInflater.from(context)
        val routeObject = RouteItemBinding.inflate(inflater)
        //routeObject.root.id = map["route_index"]!!

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

        routeObject.textViewRouteIndex.text = "№ $routeIndex"

        routeObject.textViewSum.text = "${sum}р."
        routeObject.textViewTransferSum.text = "${transferSum}р."

        routeObject.textViewHot.text = "${hot}г"
        routeObject.textViewCold.text = "${cold}х"
        routeObject.textViewUnusable.text = "${unusableCold + unusableHot}н/г"

        routeObject.textViewElapsedTime.text = "+${elapsedTime/60}\nмин."
        routeObject.textViewCurrentTime.text = currentTime

        if (discountCheck){
            routeObject.textViewDiscount.text = "п/у"
        }
        else{
            routeObject.textViewDiscount.text = "-"
        }

        val params = ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT)
        params.setMargins(2, 5, 2, 5)
        routeObject.root.layoutParams = params
        //routeObject.root.id = View.generateViewId()

        val menu = PopupMenu(context, routeObject.root)
        val routeEditMenu: Array<String>? = context.resources?.getStringArray(R.array.route_edit_menu)

        if (routeEditMenu != null) {
            for (item in routeEditMenu){
                menu.menu.add(item)
            }
        }

        menu.setOnMenuItemClickListener { item ->
            //val newToast = Toast.makeText(context, item.toString(), Toast.LENGTH_SHORT)
            when (item){
                menu.menu.getItem(0) -> {
                    routeViewModel.setIntValue("route_index", routeIndex)
                    routeViewModel.setIntValue("discount", discount)
                    routeViewModel.setBooleanValue("discount_check", discountCheck)
                    routeViewModel.setIntValue("hot", hot)
                    routeViewModel.setIntValue("cold", cold)
                    routeViewModel.setIntValue("unusable_hot", unusableHot)
                    routeViewModel.setIntValue("unusable_cold", unusableCold)
                    routeViewModel.setStringValue("cause_unusable_hot", causeUnusableHot)
                    routeViewModel.setStringValue("cause_unusable_cold", causeUnusableCold)
                    routeViewModel.setBooleanValue("bottom_route_opened", true)
                }
                menu.menu.getItem(1)-> {
                    mainSharedPrefs.deleteSharedPreferences(routeSharedPreferences)
                    redrawRouteObjects()
                    routeViewModel.setIntValue("route_index", mainSharedPrefs.getLastRouteIndex() + 1)
                }
            }
            true
        }

        routeObject.root.setOnLongClickListener {
            menu.show()
            true
        }

        root.addView(routeObject.root)
    }

    fun createConsumptionObject(consumptionSharedPreferences: SharedPreferences){
        /**EXAMPLE MAP<String, Int> for route object:
         *    mapOf(
         *       "consumption_index"    to 1,
         *       "current_time"         to "22:10",
         *       "consumption_sum"      to 50,
         *       "consumption_is_profit_check"  to true,
         *       "consumption_cause"    to "Чаевые"
         *    )
         */

        val map: Map<String, Any> = consumptionSharedPreferences.all as Map<String, Any>
        val inflater = LayoutInflater.from(context)
        val consumptionObject = ConsumptionItemBinding.inflate(inflater)

        val consumptionIndex = map["consumption_index"] as Int
        val currentTime = map["current_time"] as String
        val sum = map["consumption_sum"] as Int
        val isProfit = map["consumption_is_profit_check"] as Boolean
        val cause = map["consumption_cause"] as String

        if (isProfit){
            consumptionObject.root.setBackgroundResource(R.drawable.consumption_item_profit_style)
            consumptionObject.dividerConsumption1.setBackgroundColor(2861581)
            consumptionObject.dividerConsumption2.setBackgroundColor(2861581)
            consumptionObject.textViewSum.text = "+${sum}р."
        }
        else{
            consumptionObject.root.setBackgroundResource(R.drawable.consumption_item_loss_style)
            consumptionObject.dividerConsumption1.setBackgroundColor(14751768)
            consumptionObject.dividerConsumption2.setBackgroundColor(14751768)
            consumptionObject.textViewSum.text = "-${sum}р."
        }

        consumptionObject.textViewCurrentTime.text = currentTime
        consumptionObject.textViewCause.text = cause


        val params = ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT)
        params.setMargins(2, 5, 2, 5)
        consumptionObject.root.layoutParams = params

        val menu = PopupMenu(context, consumptionObject.root)
        val consumptionEditMenu: Array<String>? = context.resources?.getStringArray(R.array.route_edit_menu)

        if (consumptionEditMenu != null) {
            for (item in consumptionEditMenu){
                menu.menu.add(item)
            }
        }

        menu.setOnMenuItemClickListener { item ->
            //val newToast = Toast.makeText(context, item.toString(), Toast.LENGTH_SHORT)
            when (item){
                menu.menu.getItem(0) -> {
                    routeViewModel.setIntValue("consumption_index", consumptionIndex)
                    routeViewModel.setIntValue("consumption_sum", sum)
                    routeViewModel.setStringValue("consumption_cause", cause)
                    routeViewModel.setBooleanValue("consumption_is_profit_check", isProfit)
                    routeViewModel.setBooleanValue("bottom_consumption_opened", true)
                }
                menu.menu.getItem(1)-> {
                    mainSharedPrefs.deleteSharedPreferences(consumptionSharedPreferences)
                    redrawRouteObjects()
                    routeViewModel.setIntValue("consumption_index", mainSharedPrefs.getLastConsumptionIndex() + 1)
                }
            }
            true
        }
        consumptionObject.root.setOnLongClickListener {
            menu.show()
            true
        }
        root.addView(consumptionObject.root)
    }

    fun redrawRouteObjects(){
        val routeSharedPreferences = mainSharedPrefs.findRouteSharedPrefs("route")
        val consumptionSharedPreferences = mainSharedPrefs.findRouteSharedPrefs("consumption")
        root.removeAllViewsInLayout()

        if (routeSharedPreferences.isNotEmpty()) {
            for (i in routeSharedPreferences){
                createRouteObject(i)
            }
        }
        if (consumptionSharedPreferences.isNotEmpty()) {
            for (i in consumptionSharedPreferences){
                createConsumptionObject(i)
            }
        }
    }
}
