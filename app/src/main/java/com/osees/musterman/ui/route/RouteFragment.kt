package com.osees.musterman.ui.route

import android.R
import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
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
        buttonRouteAdd.setOnClickListener{
            //Toast.makeText(activity, "hay", Toast.LENGTH_SHORT).show()
            dialog?.setContentView(bottomViewRoot)
            dialog?.show()
            //val bottomSheet = BottomSheetBehavior.from(binding.nestedScroll)
            //bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
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

        val buttonsSample: List<Button> = listOf(bottomView.buttonSample1, bottomView.buttonSample2, bottomView.buttonSample3,
            bottomView.buttonSample4, bottomView.buttonSample5, bottomView.buttonSample6)

        val textInputEditPrice: TextInputEditText = bottomView.textInputEditPrice
        val textInputEditRoute: TextInputEditText = bottomView.textInputEditRoute
        val textInputEditDiscount: TextInputEditText = bottomView.textInputEditDiscount
        val checkBoxDiscount: CheckBox = bottomView.checkBoxDiscount
        val spinnerHot: Spinner = bottomView.spinnerHot
        val spinnerCold: Spinner = bottomView.spinnerCold
        val spinnerUnusableHot: Spinner = bottomView.spinnerUnusableHot
        val spinnerUnusableCold: Spinner = bottomView.spinnerUnusableCold


        routeViewModel.buttonSampleLive.observe(viewLifecycleOwner) {
            for (i in buttonsSample) {
                val spannableText: CharSequence = spannableTextForButtonSample(it.getValue(buttonsSample.indexOf(i)))
                i.text = spannableText
                //Log.d("my_tag", "spannableTextForButtonSample: $spannableText")
            }
        }
        routeViewModel.mainPriceLive.observe(viewLifecycleOwner) {
            textInputEditPrice.setText(it.toString())
        }
        routeViewModel.routeIndexLive.observe(viewLifecycleOwner) {
            textInputEditRoute.setText(it.toString())
        }
        routeViewModel.discountLive.observe(viewLifecycleOwner) {
            textInputEditDiscount.setText(it.toString())
        }
        routeViewModel.discountCheckLive.observe(viewLifecycleOwner) {
            checkBoxDiscount.isChecked = it
        }


        routeViewModel.meterVerificationLive.observe(viewLifecycleOwner) {
            val adapter = ArrayAdapter(activity!!, R.layout.simple_spinner_item, it)
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            spinnerHot.adapter = adapter
            spinnerCold.adapter = adapter
            spinnerUnusableHot.adapter = adapter
            spinnerUnusableCold.adapter = adapter
        }


        spinnerHot.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                routeViewModel.hotLive.value = spinnerHot.getItemAtPosition(position).toString().toInt()
            }
        }

        spinnerCold.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                routeViewModel.coldLive.value = spinnerCold.getItemAtPosition(position).toString().toInt()
            }
        }

        spinnerUnusableHot.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                routeViewModel.unusableHotLive.value = spinnerUnusableHot.getItemAtPosition(position).toString().toInt()
            }
        }

        spinnerUnusableCold.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                routeViewModel.unusableColdLive.value = spinnerUnusableCold.getItemAtPosition(position).toString().toInt()
            }
        }

        bottomView.checkBoxDiscount.setOnCheckedChangeListener { _, isChecked ->
            //Toast.makeText(activity, "checked", Toast.LENGTH_LONG).show()
            routeViewModel.discountCheckLive.setValue(isChecked)
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
                //Log.d(TAG, "onKey: " + price + ": " + isNumber(price))
                // if the event is a key down event on the enter button
                if ((event.action == KeyEvent.ACTION_DOWN || keyCode == KeyEvent.KEYCODE_BACK ||
                            keyCode == KeyEvent.FLAG_EDITOR_ACTION) && isNumber(price))
                {
                    routeViewModel.mainPriceLive.value = price.toInt()
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
                val route = bottomView.textInputEditDiscount.text.toString()
                // if the event is a key down event on the enter button
                if ((event.action == KeyEvent.ACTION_DOWN || keyCode == KeyEvent.KEYCODE_BACK ||
                            keyCode == KeyEvent.FLAG_EDITOR_ACTION) && isNumber(route))
                {
                    routeViewModel.routeIndexLive.value = route.toInt()
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
                    routeViewModel.discountLive.value = discount.toInt()
                    // clear focus and hide cursor from edit text
                    textInputEditPrice.clearFocus()
                    textInputEditPrice.isCursorVisible = false
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
                Log.d(ContentValues.TAG, "onCreateView: hot: $hot")
                Log.d(ContentValues.TAG, "onCreateView: cold: $cold")
                Log.d(ContentValues.TAG, "onCreateView: unusableHot: $unusableHot")
                Log.d(ContentValues.TAG, "onCreateView: unusableCold: $unusableCold")

                if (hot != null) {
                    spinnerHot.setSelection(hot)
                    routeViewModel.hotLive.value = hot
                }
                if (cold != null) {
                    spinnerCold.setSelection(cold)
                    routeViewModel.coldLive.value = cold
                }
                if (unusableHot != null) {
                    spinnerUnusableHot.setSelection(unusableHot)
                    routeViewModel.unusableHotLive.value = unusableHot
                }
                if (unusableCold != null) {
                    spinnerUnusableCold.setSelection(unusableCold)
                    routeViewModel.unusableColdLive.value = unusableCold
                }

            }
        }

        routeViewModel.sumLive.observe(viewLifecycleOwner) {
            bottomView.buttonHandle.text = "Обработать: " + it.toString() + "р"
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}