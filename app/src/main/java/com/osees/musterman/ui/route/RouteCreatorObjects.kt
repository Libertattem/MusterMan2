package com.osees.musterman.ui.route

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color.BLACK
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.osees.musterman.R
import com.osees.musterman.databinding.RouteItemBinding

class RouteCreatorObjects (private val root: ViewGroup, private val context: Context) {

    @SuppressLint("SetTextI18n")
    fun createRouteObject(map: Map<String, Int>){
        /**EXAMPLE MAP<String, Int> for route object:
        *    mapOf(
        *       "route_index"   to 1,
        *       "average_time"  to 1200,
        *       "price"         to 600,
        *       "sum"           to 1200,
        *       "transfer_sum"  to 600,
        *       "discount"      to 0,
        *       "hot"           to 2,
        *       "cold"          to 1,
        *       "unusable_hot"  to 1,
        *       "unusable_cold" to 0
        *    )
        */
        val inflater = LayoutInflater.from(context)
        val routeObject = RouteItemBinding.inflate(inflater)
        //routeObject.root.id = map["route_index"]!!

        routeObject.textViewRouteIndex.text = "№ ${map["route_index"].toString()}"

        routeObject.textViewSum.text = "${map["sum"].toString()}р."
        routeObject.textViewTransferSum.text = "${map["transfer_sum"].toString()}р."

        routeObject.textViewHot.text = "${map["hot"].toString()}г"
        routeObject.textViewCold.text = "${map["cold"].toString()}х"
        routeObject.textViewUnusable.text = "${(map["unusable_hot"]?.plus(map["unusable_cold"]!!)).toString()}н/г"

        routeObject.textViewAverageTime.text = "${(map["average_time"]?.div(60)).toString()}мин."

        if (map["discount"]!! > 0){
            routeObject.textViewDiscount.text = "п/у"
        }
        else{
            routeObject.textViewDiscount.text = "без скидок"
        }

        val params = ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT)
        params.setMargins(2, 5, 2, 5)
        routeObject.root.layoutParams = params
        routeObject.root.id = View.generateViewId()

        root.addView(routeObject.root)

        Log.d("My_test", "routeObject.root count: " + routeObject.root.id.toString())

        /*val mainConstraintLayout = ConstraintLayout(context)
        //mainConstraintLayout.orientation = LinearLayout.HORIZONTAL
        val mainParam = mainConstraintLayout.layoutParams as ViewGroup.MarginLayoutParams
        mainParam.setMargins(2, 8, 2, 0)
        mainParam.width = MATCH_PARENT
        mainParam.height = WRAP_CONTENT
        mainConstraintLayout.layoutParams = mainParam
        mainConstraintLayout.setBackgroundResource(R.drawable.route_object_style)
        ConstraintSet.Constraint
        mainConstraintLayout.setConstraintSet()

        val indexTextView= TextView(context)
        indexTextView.height = MATCH_PARENT
        indexTextView.width = MATCH_PARENT
        indexTextView.layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        indexTextView.text = map["route_index"].toString()
        indexTextView.setTextColor(BLACK)
        root.addView(mainLinearLayout)

         */

    }
}
