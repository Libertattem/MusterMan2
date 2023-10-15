package com.osees.musterman.ui.route

import android.content.Context
import android.content.SharedPreferences
import com.osees.musterman.R
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.osees.musterman.MainSharedPreferences
import com.osees.musterman.databinding.EditMenuBinding
import com.osees.musterman.databinding.FragmentRouteBinding
import com.osees.musterman.databinding.RouteConsumptionEditorBinding
import com.osees.musterman.putMap
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList

class RouteFragment : Fragment() {

    private lateinit var routeViewModel: RouteViewModel
    private var _binding: FragmentRouteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        routeViewModel =
            ViewModelProvider(this)[RouteViewModel::class.java]

        _binding = FragmentRouteBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val dialog = activity?.let { BottomSheetDialog(it) }

        val buttonRouteAdd: Button = binding.buttonRouteAdd
        val bottomRoute: EditMenuBinding = EditMenuBinding.inflate(inflater, container,false)
        val bottomRouteRoot: View = bottomRoute.root

        buttonRouteAdd.setOnClickListener{
            //Toast.makeText(activity, "hay", Toast.LENGTH_SHORT).show()
            routeViewModel.setBooleanValue("bottom_route_opened", true)
        }

        val buttonConsumptionAdd: Button = binding.buttonConsumptionAdd
        val bottomConsumption: RouteConsumptionEditorBinding =
            RouteConsumptionEditorBinding.inflate(inflater, container,false)
        val bottomConsumptionRoot: View = bottomConsumption.root

        buttonConsumptionAdd.setOnClickListener{
            //dialog?.setContentView(bottomConsumptionRoot)
            routeViewModel.setBooleanValue("bottom_consumption_opened", true)
        }

        val unusableCauses: Array<String>? = activity?.resources?.getStringArray(R.array.causes_unusable)
        val meterCount: Array<String>? = activity?.resources?.getStringArray(R.array.meter_count)
        val consumptionCauses: Array<String>? = activity?.resources?.getStringArray(R.array.consumption_causes)
        val locations: Array<String>? = activity?.resources?.getStringArray(R.array.locations_for_price)
        val locationPrice: IntArray? = activity?.resources?.getIntArray(R.array.locations_price)
        val locationDiscount: IntArray? = activity?.resources?.getIntArray(R.array.locations_discount)


        fun spannableTextForButtonSample(list: List<Int>): CharSequence {
            val spannable: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            val spannableTextList: MutableList<SpannableString> = mutableListOf()

            fun spannedItem (item: Int, index: Int): SpannableString {
                val span: SpannableString
                when (index){
                    0 -> {span = SpannableString( item.toString() + "г")
                        span.setSpan( ForegroundColorSpan(Color.RED), 0,2, spannable)
                    }
                    1 -> {span = SpannableString( item.toString() + "х")
                        span.setSpan( ForegroundColorSpan(Color.BLUE), 0,2, spannable)
                    }
                    2 -> {span = SpannableString(item.toString() + "г н/г")
                        span.setSpan( ForegroundColorSpan(Color.RED), 0,2, spannable)
                        span.setSpan( ForegroundColorSpan(Color.GRAY), 3,6, spannable)
                    }
                    3 -> {span = SpannableString(item.toString() + "х н/г")
                        span.setSpan( ForegroundColorSpan(Color.BLUE), 0,2, spannable)
                        span.setSpan( ForegroundColorSpan(Color.GRAY), 3,6, spannable)
                    }
                    else -> span = SpannableString("")
                }
                return span
            }

            for(index in list.indices){
                val i = list[index]
                if( i != 0 ){
                    spannableTextList.add(spannedItem(i, index))
                }
            }

            val spannableText = when(spannableTextList.size){
                1 -> {return spannableTextList[0]}
                2 -> {return TextUtils.concat(spannableTextList[0], ", ", spannableTextList[1])}
                3 -> {return TextUtils.concat(spannableTextList[0], ", ", spannableTextList[1],
                    ", ", spannableTextList[2])}
                4 -> {return TextUtils.concat(spannableTextList[0], ", ", spannableTextList[1],
                    ", ",spannableTextList[2], ", ", spannableTextList[3])}
                else -> {""}
            }
            return spannableText
        }

        val buttonsSample: List<Button> = listOf(bottomRoute.buttonSample1, bottomRoute.buttonSample2,
            bottomRoute.buttonSample4, bottomRoute.buttonSample3, bottomRoute.buttonSample5, bottomRoute.buttonSample6)

        val textInputEditPrice: TextInputEditText = bottomRoute.textInputEditPrice
        val textInputEditRoute: TextInputEditText = bottomRoute.textInputEditRoute
        val textInputEditDiscount: TextInputEditText = bottomRoute.textInputEditDiscount
        val textInputEditTransferSum: TextInputEditText = bottomRoute.textInputEditTransfer

        val checkBoxDiscount: CheckBox = bottomRoute.checkBoxDiscount
        val checkBoxTransfer: CheckBox = bottomRoute.checkBoxTransfer

        val buttonHot: Button = bottomRoute.buttonHot
        val buttonUnusableHot: Button = bottomRoute.buttonUnusableHot
        val buttonCauseUnusableHot: Button = bottomRoute.buttonCauseUnusableHot

        val buttonCold: Button = bottomRoute.buttonCold
        val buttonUnusableCold: Button = bottomRoute.buttonUnusableCold
        val buttonCauseUnusableCold: Button = bottomRoute.buttonCauseUnusableCold

        val buttonRouteHandle: Button = bottomRoute.buttonHandle

        val textEditConsumptionSum: EditText = bottomConsumption.consumptionTextInputSum
        val buttonConsumptionCause: Button = bottomConsumption.consumptionButtonCause
        val checkBoxConsumptionProfit: CheckBox = bottomConsumption.consumptionCheckBoxProfit
        val checkBoxConsumptionLoss: CheckBox = bottomConsumption.consumptionCheckBoxLoss
        val checkBoxHideRouts: CheckBox = binding.hideRoute
        val checkBoxHideConsumption: CheckBox = binding.hideConsumption
        val checkBoxHideCompleted: CheckBox = binding.hideCompleted
        val spinnerSortBy: Spinner = binding.sortBy
        val buttonConsumptionHandle: Button = bottomConsumption.consumptionButtonHandle
        val buttonLocation: Button = bottomRoute.buttonLocation

        fun setPopupMenuForButton (button: Button, keyInt: String?, keyString: String?, menuItems: Array<String>?){
            val menu = PopupMenu(activity, button)
            if (menuItems != null) {
                for (item in menuItems){
                    menu.menu.add(item)
                }
            }
            menu.setOnMenuItemClickListener { item ->
                if (keyInt != null) {
                    routeViewModel.setIntValue(keyInt, item.toString().toInt())
                }
                if (keyString != null) {
                    routeViewModel.setStringValue(keyString, item.toString())
                }
                true
            }
            button.setOnClickListener {
                menu.show()
            }
        }

        setPopupMenuForButton(buttonHot, "hot", null, meterCount)
        setPopupMenuForButton(buttonUnusableHot, "unusable_hot", null, meterCount)
        setPopupMenuForButton(buttonCauseUnusableHot, null, "cause_unusable_hot", unusableCauses)


        setPopupMenuForButton(buttonCold, "cold", null, meterCount)
        setPopupMenuForButton(buttonUnusableCold, "unusable_cold", null, meterCount)
        setPopupMenuForButton(buttonCauseUnusableCold, null, "cause_unusable_cold", unusableCauses)

        setPopupMenuForButton(buttonConsumptionCause, null, "consumption_cause", consumptionCauses)
        setPopupMenuForButton(buttonLocation,null, "location", locations)

        routeViewModel.buttonSampleLive.observe(viewLifecycleOwner) {
            for (i in buttonsSample) {
                val spannableText: CharSequence = spannableTextForButtonSample(it.getValue(buttonsSample.indexOf(i)))
                i.text = spannableText
                //Log.d("my_tag", "spannableTextForButtonSample: $spannableText")
            }
        }
        routeViewModel.priceLive.observe(viewLifecycleOwner) {
            textInputEditPrice.setText(it.toString())
        }
        routeViewModel.routeIndexLive.observe(viewLifecycleOwner) {
            textInputEditRoute.setText(it.toString())
        }
        routeViewModel.discountLive.observe(viewLifecycleOwner) {
            textInputEditDiscount.setText(it.toString())
        }
        routeViewModel.transferSumLive.observe(viewLifecycleOwner) {
            textInputEditTransferSum.setText(it.toString())
            if(it != 0 ){
                checkBoxTransfer.isChecked = true
            }
        }
        routeViewModel.discountCheckLive.observe(viewLifecycleOwner) {
            checkBoxDiscount.isChecked = it
        }
        routeViewModel.hotLive.observe(viewLifecycleOwner) {
            buttonHot.text = it.toString()
        }
        routeViewModel.unusableHotLive.observe(viewLifecycleOwner) {
            buttonUnusableHot.text = "Н/Г: $it"
            if (it == 0){
                routeViewModel.setStringValue("cause_unusable_hot", "")
            }
            else {
                routeViewModel.setStringValue("cause_unusable_hot", unusableCauses?.get(2))
            }
        }
        routeViewModel.causeUnusableHotLive.observe(viewLifecycleOwner) {
            if (it == ""){
                buttonCauseUnusableHot.visibility = View.INVISIBLE
            }
            else {
                buttonCauseUnusableHot.visibility = View.VISIBLE
            }
            buttonCauseUnusableHot.text = it
        }
        routeViewModel.coldLive.observe(viewLifecycleOwner) {
            buttonCold.text = it.toString()
        }
        routeViewModel.unusableColdLive.observe(viewLifecycleOwner) {
            buttonUnusableCold.text = "Н/Г: $it"
            if (it == 0){
                routeViewModel.setStringValue("cause_unusable_cold", "")
            }
            else {
                routeViewModel.setStringValue("cause_unusable_cold", unusableCauses?.get(2))
            }
        }
        routeViewModel.causeUnusableColdLive.observe(viewLifecycleOwner) {
            if (it == ""){
                buttonCauseUnusableCold.visibility = View.INVISIBLE
            }
            else {
                buttonCauseUnusableCold.visibility = View.VISIBLE
            }
            buttonCauseUnusableCold.text = it
        }
        routeViewModel.consumptionSumLive.observe(viewLifecycleOwner){
            textEditConsumptionSum.setText(it.toString())
        }
        routeViewModel.consumptionCauseLive.observe(viewLifecycleOwner){
            buttonConsumptionCause.text = it
        }
        routeViewModel.consumptionIsProfitCheckLive.observe(viewLifecycleOwner){
            checkBoxConsumptionProfit.isChecked = it
            checkBoxConsumptionLoss.isChecked = !it
        }
        routeViewModel.bottomRouteOpenedLive.observe(viewLifecycleOwner){
            if (it){
                dialog?.setContentView(bottomRouteRoot)
                dialog?.show()
            }
        }
        routeViewModel.bottomConsumptionOpenedLive.observe(viewLifecycleOwner){
            if (it){
                dialog?.setContentView(bottomConsumptionRoot)
                dialog?.show()
            }
        }
        routeViewModel.locationLive.observe(viewLifecycleOwner){
            if (locations?.indexOf(it) != null && it != "Другое") {
                routeViewModel.setIntValue("price", locationPrice?.get(locations.indexOf(it)))
                routeViewModel.setIntValue("discount", locationDiscount?.get(locations.indexOf(it)))
            }
            buttonLocation.text = it
        }
        /*routeViewModel.unusableCausesLive.observe(viewLifecycleOwner){
            val adapter = ArrayAdapter(activity!!, R.layout.simple_spinner_item, it)
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            spinnerCauseUnusableHot.adapter = adapter
            spinnerCauseUnusableCold.adapter = adapter
        }*/
        /*spinnerCold.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                routeViewModel.coldLive.value = id.toInt()
            }
        }*/



        checkBoxDiscount.setOnCheckedChangeListener { _, isChecked ->
            //Toast.makeText(activity, "checked", Toast.LENGTH_LONG).show()
            routeViewModel.setBooleanValue("discount_check", isChecked)
        }
        checkBoxConsumptionProfit.setOnCheckedChangeListener { _, isChecked ->
            routeViewModel.setBooleanValue("consumption_is_profit_check", isChecked)
        }
        checkBoxConsumptionLoss.setOnCheckedChangeListener { _, isChecked ->
            routeViewModel.setBooleanValue("consumption_is_profit_check", !isChecked)
        }
        checkBoxTransfer.setOnCheckedChangeListener { _, isChecked ->
            //Toast.makeText(activity, "checked", Toast.LENGTH_LONG).show()
            if (isChecked && routeViewModel.getIntValue("transfer_sum") == 0) {
                routeViewModel.setIntValue("transfer_sum", routeViewModel.getIntValue("sum"))
            }
            else if (!isChecked){
                routeViewModel.setIntValue("transfer_sum", 0)
            }
        }

        fun isNumber (string: String): Boolean {
            return try {
                string.toInt()
                true
            }
            catch (e: NumberFormatException){
                false
            }
        }

        /*textEditConsumptionSum.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN)
                { // clear focus and hide cursor from edit text
                    textInputEditTransferSum.clearFocus()
                    textInputEditTransferSum.isCursorVisible = false
                    return true
                }
                return false
            }
        })*/
        fun saveRouteTextInput(){
            val price = textInputEditPrice.text.toString()
            val routeIndex = textInputEditRoute.text.toString()
            val discount =  textInputEditDiscount.text.toString()
            val transferSum = textInputEditTransferSum.text.toString()

            if (isNumber(price)){routeViewModel.setIntValue("price", price.toInt())}
            if (isNumber(routeIndex)){routeViewModel.setIntValue("route_index", routeIndex.toInt())}
            if (isNumber(discount)){routeViewModel.setIntValue("discount", discount.toInt())}
            if (isNumber(transferSum)){routeViewModel.setIntValue("transfer_sum", transferSum.toInt())}
        }

        fun saveConsumptionTextInput(){
            val sum = textEditConsumptionSum.text.toString()
            if(isNumber(sum)){routeViewModel.setIntValue("consumption_sum", sum.toInt())}
        }

        activity?.let { activity ->
            KeyboardVisibilityEvent.setEventListener(activity, viewLifecycleOwner, KeyboardVisibilityEventListener{
                val openedRoute: Boolean = routeViewModel.getBooleanValue("bottom_route_opened")
                val openedConsumption: Boolean = routeViewModel.getBooleanValue("bottom_consumption_opened")

                if(openedRoute || openedConsumption) {
                    if (!it) {
                        val textInputFocus = dialog?.currentFocus as TextView
                        if (openedRoute) {
                            saveRouteTextInput()
                        } else if (openedConsumption) {
                            saveConsumptionTextInput()
                        }
                        textInputFocus.clearFocus()
                    }
                }
            })
        }

        dialog?.setOnDismissListener {
            if (routeViewModel.getBooleanValue("bottom_route_opened")){
                saveRouteTextInput()
                routeViewModel.setBooleanValue("bottom_route_opened", false)
            }
            else if (routeViewModel.getBooleanValue("bottom_consumption_opened")){
                saveConsumptionTextInput()
                routeViewModel.setBooleanValue("bottom_consumption_opened", false)
            }
        }

        /*textEditConsumptionSum.setOnEditorActionListener {
                textView, keyCode, keyEvent ->
            if (keyCode == KeyEvent.ACTION_DOWN) {
                // your code here
                return@setOnEditorActionListener true
            }
            false
        }*/

        for (index in buttonsSample.indices){
            val item = buttonsSample[index]

            item.setOnClickListener{

                val mapOfButtonSample: Map<Int, List<Int>>? = routeViewModel.buttonSampleLive.value

                val hot: Int? = mapOfButtonSample?.get(index)?.get(0)
                val cold: Int? = mapOfButtonSample?.get(index)?.get(1)
                val unusableHot: Int? = mapOfButtonSample?.get(index)?.get(2)
                val unusableCold: Int? = mapOfButtonSample?.get(index)?.get(3)

                //routeViewModel.hotLive.value = hot
                routeViewModel.setIntValue("hot", hot)
                routeViewModel.setIntValue("cold", cold)
                routeViewModel.setIntValue("unusable_hot", unusableHot)
                routeViewModel.setIntValue("unusable_cold", unusableCold)
            }
        }

        val mainSharedPreferences = activity?.let { MainSharedPreferences(it) }
        val routeCreatorObjects = activity?.let { RouteCreatorObjects(binding.linearLayoutScroll, it, routeViewModel) }
        //mainSharedPreferences?.deleteSharedPreferences(null)


        if (routeCreatorObjects != null) {
            if(routeViewModel.linearLayoutLive.value == null){
                routeViewModel.addLinearLayoutForLiveData(routeCreatorObjects.createLinearLayoutForLiveData())
            }
        }

        binding.linearLayoutScroll.addView(routeViewModel.linearLayoutLive.value)

        /*routeViewModel.linearLayoutLive.observe(viewLifecycleOwner){
            //binding.linearLayoutScroll.addChildrenForAccessibility(it.children.toList() as ArrayList<View>?)

            Log.d("My_tag", "add view")
            binding.linearLayoutScroll.removeAllViews()
            binding.linearLayoutScroll.addView(it)
        }*/




        fun setLastRouteIndex(){
            routeViewModel.setIntValue("route_index", mainSharedPreferences?.getLastRouteIndex()?.plus(1))
        }

        fun setLastConsumptionIndex(){
            routeViewModel.setIntValue("consumption_index", mainSharedPreferences?.getLastConsumptionIndex()?.plus(1))
        }

        if (mainSharedPreferences != null) {
            //routeCreatorObjects?.redrawRouteObjects()
            setLastRouteIndex()
        }

        fun createRouteCellInViewModel (sharedPreferences: SharedPreferences){
            val map: Map<String, *> = sharedPreferences.all
            val liveMap: MutableLiveData<Map<String, *>> = routeViewModel.createMapLiveOfRoute(map)
            liveMap.observe(viewLifecycleOwner){
                if (it != null){
                    sharedPreferences.edit().putMap(sharedPreferences, it)
                }
                else
                {
                    mainSharedPreferences?.deleteSharedPreferences(sharedPreferences)
                }
            }
            val routeObject: ConstraintLayout? = routeCreatorObjects?.createRouteObject1(liveMap, viewLifecycleOwner)
            routeObject?.id?.let { routeViewModel.addRoutsObject(sharedPreferences, liveMap.value?.get("route_index") as Int, routeObject, liveMap) }
        }


        buttonRouteHandle.setOnClickListener {
            val typeSharedPreferences = "route"
            val price: Int = routeViewModel.getIntValue("price")
            val discount: Int = routeViewModel.getIntValue("discount")
            val hot: Int = routeViewModel.getIntValue("hot")
            val cold: Int= routeViewModel.getIntValue("cold")
            val discountCheck: Boolean = routeViewModel.getBooleanValue("discount_check")
            val unusableHot: Int = routeViewModel.getIntValue("unusable_hot")
            val unusableCold: Int= routeViewModel.getIntValue("unusable_cold")
            val routeIndex: Int = routeViewModel.getIntValue("route_index")
            val sum: Int = routeViewModel.getIntValue("sum")
            val transferSum: Int = routeViewModel.getIntValue("transfer_sum")

            val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val currentTime: String = LocalDateTime.now().format(timeFormat).toString()

            val causeUnusableHot = routeViewModel.getStringValue("cause_unusable_hot")
            val causeUnusableCold = routeViewModel.getStringValue("cause_unusable_cold")

            val mapForRoute: Map <String, Any> = mapOf(
                "type_shared_prefs" to typeSharedPreferences,
                "route_index"   to routeIndex,
                "current_time"  to currentTime,
                "elapsed_time"  to 1200,
                "price"         to price,
                "sum"           to sum,
                "transfer_sum"  to transferSum,
                "discount_check" to discountCheck,
                "discount"      to discount,
                "hot"           to hot,
                "cold"          to cold,
                "unusable_hot"  to unusableHot,
                "unusable_cold" to unusableCold,
                "cause_unusable_hot"  to causeUnusableHot,
                "cause_unusable_cold" to causeUnusableCold
            )

            val (routeSharedPref: String, isEdit: Boolean) = mainSharedPreferences!!.routeSharedPrefEditor1(mapForRoute)
            val mapForRouteLiveData = routeViewModel.createMapLiveOfRoute(mapForRoute)


            if(isEdit){
                Log.d("My_tag", "is edit: " + isEdit)

            }
            else{
                Log.d("My_tag", "is edit: " + isEdit)
                val routeObject = routeCreatorObjects?.createRouteObject1(mapForRouteLiveData, viewLifecycleOwner)
                val id1 = routeObject?.id!!
                //val id1 = 1
                //routeViewModel.linearLayoutLive.value?.addView(routeObject)
                routeViewModel.addRouteInLinearLayout(routeObject)
                routeViewModel.addRoutsObject(routeSharedPref, id1, mapForRouteLiveData)
            }

            fun createMapForMainSharedPrefs(isEdit: Boolean = false, oldMap: Map <String, *>? = null): Map<String, Any> {
                val oldHot: Int
                val oldCold: Int
                val oldUnusableHot: Int
                val oldUnusableCold: Int
                val oldSum: Int
                val oldTransferSum: Int
                val oldPrice: Int

                if (oldMap != null){
                    oldHot = oldMap["hot"] as Int
                    oldCold = oldMap["cold"] as Int
                    oldUnusableHot= oldMap["unusable_hot"] as Int
                    oldUnusableCold = oldMap["unusable_cold"] as Int
                    oldSum = oldMap["sum"] as Int
                    oldTransferSum = oldMap["transfer_sum"] as Int
                    oldPrice = oldMap["price"] as Int
                }else {
                    oldHot = 0
                    oldCold = 0
                    oldUnusableHot= 0
                    oldUnusableCold = 0
                    oldSum = 0
                    oldTransferSum = 0
                    oldPrice = 0

                }

                val newHot = if (isEdit){hot - oldHot} else { hot }
                val newCold = if (isEdit){cold - oldCold} else { cold }
                val newUnusableHot = if (isEdit){unusableHot - oldUnusableHot} else { unusableHot }
                val newUnusableCold = if (isEdit){unusableCold - oldUnusableCold} else { unusableCold }
                val newSum = if (isEdit){sum - oldSum} else { sum }
                val newTransferSum = if (isEdit){transferSum - oldTransferSum} else { transferSum }
                val newMetersForPrice = if (isEdit){(hot + cold) - (oldHot + oldCold)} else { hot + cold }

                val map = mutableMapOf<String, Any>(
                    "hot" to newHot,
                    "cold"  to newCold,
                    "price_$oldPrice" to newMetersForPrice,
                    "sum"           to newSum,
                    "transfer_sum"  to newTransferSum,
                    "unusable_hot"  to newUnusableHot,
                    "unusable_cold" to newUnusableCold)

                if (!isEdit){
                    map["total_time"] = currentTime
                    map["processed_routes"] = 1
                }
                else {map["processed_routes"] = 0}
                if (oldPrice != price){map["price_$price"] = hot + cold}

                return map
            }

            val lastRouteIndexSharedPreferences = mainSharedPreferences?.getLastRouteIndex()

            if (lastRouteIndexSharedPreferences != 0){

                if (lastRouteIndexSharedPreferences!! >= routeIndex){
                    Log.d("My_test", "is redraw")
                    val oldRouteSharedPrefs = context?.getSharedPreferences(
                        "${typeSharedPreferences}_$routeIndex", Context.MODE_PRIVATE)?.all
                    mainSharedPreferences.putInMainSharedPrefs(createMapForMainSharedPrefs(true, oldRouteSharedPrefs))

                    mainSharedPreferences.routeSharedPrefEditor(mapForRoute)
                    //routeCreatorObjects?.redrawRouteObjects()
                }
                else{
                    val routeSharedPref = mainSharedPreferences.routeSharedPrefEditor(mapForRoute)
                    //routeCreatorObjects?.createRouteObject(routeSharedPref)
                    mainSharedPreferences.putInMainSharedPrefs(createMapForMainSharedPrefs())
                    Log.d("My_test", "is add new")
                }
            }
            else{
                val routeSharedPref = mainSharedPreferences.routeSharedPrefEditor(mapForRoute)
                //routeCreatorObjects?.createRouteObject(routeSharedPref)
                mainSharedPreferences.putInMainSharedPrefs(createMapForMainSharedPrefs())
                Log.d("My_test", "is add new")
            }
            setLastRouteIndex()
            routeViewModel.setDefaultRoute()
            dialog?.dismiss()
        }

        buttonConsumptionHandle.setOnClickListener {
            val typeSharedPreferences = "consumption"
            val consumptionIndex: Int = routeViewModel.getIntValue("consumption_index")
            val sum: Int = routeViewModel.getIntValue("consumption_sum")
            val cause: String = routeViewModel.getStringValue("consumption_cause")
            val isProfit: Boolean = routeViewModel.getBooleanValue("consumption_is_profit_check")

            val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val currentTime: String = LocalDateTime.now().format(timeFormat).toString()

            val map: Map <String, Any> = mapOf(
                "type_shared_prefs" to typeSharedPreferences,
                "consumption_index"   to consumptionIndex,
                "current_time"  to currentTime,
                "consumption_sum"  to sum,
                "consumption_cause"  to cause,
                "consumption_is_profit_check" to isProfit
            )

            fun createMapForMainSharedPrefs(isEdit: Boolean = false, oldMap: Map <String, *>? = null): Map<String, Any> {
                val oldSum: Int
                val oldProfitCheck: Boolean

                if (oldMap != null){
                    oldSum = oldMap["consumption_sum"] as Int
                    oldProfitCheck = oldMap["consumption_is_profit_check"] as Boolean
                }
                else{
                    oldSum = 0
                    oldProfitCheck = isProfit
                }


                val newProfit: Int = if (oldProfitCheck){ if (isEdit){sum - oldSum } else { sum } } else{ 0 }
                val newLoss: Int = if (!oldProfitCheck){ if (isEdit){sum - oldSum } else { sum } } else{ 0 }

                return mapOf<String, Any>(
                    "profit" to newProfit,
                    "loss"  to newLoss)
            }

            val lastConsumptionIndexSharedPrefs = mainSharedPreferences?.getLastConsumptionIndex()

            if (lastConsumptionIndexSharedPrefs != 0){
                if (lastConsumptionIndexSharedPrefs!! >= consumptionIndex){
                    Log.d("My_test", "is redraw")
                    val oldConsumptionSharedPrefs = context?.getSharedPreferences(
                        "${typeSharedPreferences}_$consumptionIndex", Context.MODE_PRIVATE)?.all
                    mainSharedPreferences.putInMainSharedPrefs(createMapForMainSharedPrefs(true, oldConsumptionSharedPrefs))
                    routeCreatorObjects?.redrawRouteObjects()
                }
                else{
                    val consumptionSharedPref = mainSharedPreferences.routeSharedPrefEditor(map)
                    mainSharedPreferences.putInMainSharedPrefs(createMapForMainSharedPrefs())
                    routeCreatorObjects?.createConsumptionObject(consumptionSharedPref)
                    Log.d("My_test", "is add new")
                }
            }
            else{
                val consumptionSharedPref = mainSharedPreferences.routeSharedPrefEditor(map)
                mainSharedPreferences.putInMainSharedPrefs(createMapForMainSharedPrefs())
                routeCreatorObjects?.createConsumptionObject(consumptionSharedPref)
                Log.d("My_test", "is add new")
            }
            setLastConsumptionIndex()
            routeViewModel.setDefaultConsumption()
            dialog?.dismiss()
        }

        routeViewModel.sumLive.observe(viewLifecycleOwner) {
            bottomRoute.buttonHandle.text = "Обработать: " + it.toString() + "р"
        }

        return root
    }

    override fun onDestroyView() {
        binding.linearLayoutScroll.removeAllViews()
        super.onDestroyView()
        _binding = null
    }
}