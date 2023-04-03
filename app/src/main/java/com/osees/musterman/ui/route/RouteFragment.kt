package com.osees.musterman.ui.route

import com.osees.musterman.R
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.osees.musterman.MainSharedPreferences
import com.osees.musterman.databinding.EditMenuBinding
import com.osees.musterman.databinding.FragmentRouteBinding

class RouteFragment : Fragment() {

    private lateinit var routeViewModel: RouteViewModel
    private var _binding: FragmentRouteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
        val bottomView: EditMenuBinding = EditMenuBinding.inflate(inflater, container,false)
        val bottomViewRoot: View = bottomView.root

        val buttonRouteAdd: Button = binding.buttonRouteAdd
        val causes: Array<String>? = activity?.resources?.getStringArray(R.array.causes_unusable)
        val meterCount: Array<String>? = activity?.resources?.getStringArray(R.array.meter_count)

        buttonRouteAdd.setOnClickListener{
            //Toast.makeText(activity, "hay", Toast.LENGTH_SHORT).show()
            dialog?.setContentView(bottomViewRoot)
            dialog?.show()
        }

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

        val buttonsSample: List<Button> = listOf(bottomView.buttonSample1, bottomView.buttonSample2,
            bottomView.buttonSample4, bottomView.buttonSample3, bottomView.buttonSample5, bottomView.buttonSample6)

        val textInputEditPrice: TextInputEditText = bottomView.textInputEditPrice
        val textInputEditRoute: TextInputEditText = bottomView.textInputEditRoute
        val textInputEditDiscount: TextInputEditText = bottomView.textInputEditDiscount
        val textInputEditTransferSum: TextInputEditText = bottomView.textInputEditTransfer

        val checkBoxDiscount: CheckBox = bottomView.checkBoxDiscount
        val checkBoxTransfer: CheckBox = bottomView.checkBoxTransfer

        val buttonHot: Button = bottomView.buttonHot
        val buttonUnusableHot: Button = bottomView.buttonUnusableHot
        val buttonCauseUnusableHot: Button = bottomView.buttonCauseUnusableHot

        val buttonCold: Button = bottomView.buttonCold
        val buttonUnusableCold: Button = bottomView.buttonUnusableCold
        val buttonCauseUnusableCold: Button = bottomView.buttonCauseUnusableCold

        val buttonHandle: Button = bottomView.buttonHandle

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
                //liveDataInt?.value = item.toString().toInt()
                //liveDataString?.value = item.toString()
                true
            }
            button.setOnClickListener {
                menu.show()
            }
        }

        setPopupMenuForButton(buttonHot, "hot", null, meterCount)
        setPopupMenuForButton(buttonUnusableHot, "unusable_hot", null, meterCount)
        setPopupMenuForButton(buttonCauseUnusableHot, null, "causeUnusable_hot_live", causes)


        setPopupMenuForButton(buttonCold, "cold", null, meterCount)
        setPopupMenuForButton(buttonUnusableCold, "unusable_cold", null, meterCount)
        setPopupMenuForButton(buttonCauseUnusableCold, null, "causeUnusable_cold_live", causes)

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
                routeViewModel.setStringValue("cause_unusable_hot", causes?.get(2))
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
                routeViewModel.setStringValue("cause_unusable_cold", causes?.get(2))
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

        fun isNumber (string: String): Boolean {
            return try {
                string.toInt()
                true
            }
            catch (e: NumberFormatException){
                false
            }
        }

        textInputEditPrice.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                val price = bottomView.textInputEditPrice.text.toString()
                if ((event.action == KeyEvent.ACTION_DOWN || keyCode == KeyEvent.KEYCODE_BACK ||
                            keyCode == KeyEvent.FLAG_EDITOR_ACTION) && isNumber(price))
                {
                    routeViewModel.setIntValue("price", price.toInt())
                    // clear focus and hide cursor from edit text
                    textInputEditPrice.clearFocus()
                    textInputEditPrice.isCursorVisible = false
                    return true
                }
                return false
            }
        })

        textInputEditRoute.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                val route = bottomView.textInputEditRoute.text.toString()
                // if the event is a key down event on the enter button
                if ((event.action == KeyEvent.ACTION_DOWN || keyCode == KeyEvent.KEYCODE_BACK ||
                            keyCode == KeyEvent.FLAG_EDITOR_ACTION) && isNumber(route))
                {
                    routeViewModel.setIntValue("route_index", route.toInt())
                    // clear focus and hide cursor from edit text
                    textInputEditPrice.clearFocus()
                    textInputEditPrice.isCursorVisible = false
                    return true
                }
                return false
            }
        })

        textInputEditDiscount.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                val discount = bottomView.textInputEditDiscount.text.toString()
                //Log.d(TAG, "onKey: " + discount.toString() + ": " + discount.isNullOrBlank())
                // if the event is a key down event on the enter button
                if ((event.action == KeyEvent.ACTION_DOWN ||
                            keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.FLAG_EDITOR_ACTION) &&
                    isNumber(discount))
                {
                    routeViewModel.setIntValue("discount", discount.toInt())
                    // clear focus and hide cursor from edit text
                    textInputEditPrice.clearFocus()
                    textInputEditPrice.isCursorVisible = false
                    return true
                }
                return false
            }
        })

        textInputEditTransferSum.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                val transferSum = bottomView.textInputEditTransfer.text.toString()
                //Log.d(TAG, "onKey: " + discount.toString() + ": " + discount.isNullOrBlank())
                // if the event is a key down event on the enter button
                if ((event.action == KeyEvent.ACTION_DOWN ||
                            keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.FLAG_EDITOR_ACTION) &&
                    isNumber(transferSum))
                {
                    routeViewModel.setIntValue("transfer_sum", transferSum.toInt())
                    // clear focus and hide cursor from edit text
                    textInputEditTransferSum.clearFocus()
                    textInputEditTransferSum.isCursorVisible = false
                    return true
                }
                return false
            }
        })

        for (index in buttonsSample.indices){
            val item = buttonsSample[index]

            item.setOnClickListener{

                val mapOfButtonSample: Map<Int, List<Int>>? = routeViewModel.buttonSampleLive.value

                val hot: Int? = mapOfButtonSample?.get(index)?.get(0)
                val cold: Int? = mapOfButtonSample?.get(index)?.get(1)
                val unusableHot: Int? = mapOfButtonSample?.get(index)?.get(2)
                val unusableCold: Int? = mapOfButtonSample?.get(index)?.get(3)
                //Log.d(ContentValues.TAG, "onCreateView: hot: $hot")

                //routeViewModel.hotLive.value = hot
                routeViewModel.setIntValue("hot", hot)
                routeViewModel.setIntValue("cold", cold)
                routeViewModel.setIntValue("unusable_hot", unusableHot)
                routeViewModel.setIntValue("unusable_cold", unusableCold)
            }
        }



        val mainSharedPreferences = activity?.let { MainSharedPreferences(it) }
        val routeCreatorObjects = activity?.let { RouteCreatorObjects(binding.linearLayoutScroll, it) }
        //mainSharedPreferences?.deleteSharedPreferences(null)

        if (mainSharedPreferences != null) {
            val routeSharedPreferences = mainSharedPreferences.findRouteSharedPrefs()
            if (routeSharedPreferences != null){
                for (i in routeSharedPreferences){
                    routeCreatorObjects?.createRouteObject(i.all as Map<String, Int>)
                }
                routeViewModel.setIntValue("route_index", routeSharedPreferences.size.plus(1))
            }
        }

        buttonHandle.setOnClickListener {
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
            val discountSum1: Int = if (discountCheck){ (hot + cold) * discount } else { 0 }

            val map: Map <String, Int> = mapOf(
                "route_index"   to routeIndex,
                "average_time"  to 1200,
                "price"         to price,
                "sum"           to sum,
                "transfer_sum"  to transferSum,
                "discount"      to discountSum1,
                "hot"           to hot,
                "cold"          to cold,
                "unusable_hot"  to unusableHot,
                "unusable_cold" to unusableCold
            )
            mainSharedPreferences?.routeSharedPrefEditor(map)
            routeCreatorObjects?.createRouteObject(map)
            routeViewModel.setIntValue("route_index", routeIndex + 1 )
        }

        routeViewModel.sumLive.observe(viewLifecycleOwner) {
            bottomView.buttonHandle.text = "Обработать: " + it.toString() + "р"
            checkBoxTransfer.setOnCheckedChangeListener { _, isChecked ->
                //Toast.makeText(activity, "checked", Toast.LENGTH_LONG).show()
                if (isChecked) {
                    routeViewModel.setIntValue("transfer_sum", it)
                }
                else {
                    routeViewModel.setIntValue("transfer_sum", 0)
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}