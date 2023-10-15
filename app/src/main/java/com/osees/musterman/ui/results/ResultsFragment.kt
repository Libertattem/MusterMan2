package com.osees.musterman.ui.results

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.osees.musterman.databinding.FragmentResultsBinding

class ResultsFragment : Fragment() {

    private lateinit var resultsViewModel: ResultsViewModel
    private var _binding: FragmentResultsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resultsViewModel =
            ViewModelProvider(this).get(ResultsViewModel::class.java)

        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        val root: View = binding.root



        val textViewCash = binding.resultsCash
        val textViewNonCash = binding.resultsNonCash
        val textViewCashFlows = binding.resultsCashFlows
        val textViewMeters = binding.resultsMeters
        val textViewHot = binding.resultsHot
        val textViewCold = binding.resultsCold
        val textViewAverageTime = binding.resultsAverageTime
        val textViewTotalTime= binding.resultsTotalTime
        val textViewGiveGet = binding.resultsGiveGet
        val textViewProfit = binding.resultsProfit
        val textViewLoss = binding.resultsLoss
        val textViewMoney = binding.resultsMoney
        val textViewProcessedRouts = binding.resultsProcessedRouts
        val textViewSalary= binding.resultsSalary
        val textViewPetrol = binding.resultsPetrol
        val textViewTotal= binding.resultsTotal
        val textViewUnusableHot= binding.resultsUnusableHot
        val textViewUnusableCold= binding.resultsUnusableCold
        val textViewUnusableMeters= binding.resultsUnusableMeters

        val mainSharedPrefs = activity?.getSharedPreferences("main", Context.MODE_PRIVATE)

        if (mainSharedPrefs != null) {
            val hot = mainSharedPrefs.getInt("hot", 0)
            val cold = mainSharedPrefs.getInt("cold", 0)
            val sum = mainSharedPrefs.getInt("sum", 0)
            val transferSum = mainSharedPrefs.getInt("transfer_sum", 0)
            val unusableHot = mainSharedPrefs.getInt("unusable_hot", 0)
            val unusableCold = mainSharedPrefs.getInt("unusable_cold", 0)
            val processedRoutes = mainSharedPrefs.getInt("processed_routes", 0)
            val totalTime = mainSharedPrefs.getString("total_time", "00:00")
            val profit = mainSharedPrefs.getInt("profit", 0)
            val loss = mainSharedPrefs.getInt("loss", 0)
            val salary = mainSharedPrefs.getInt("salary", 0)
            val petrol = mainSharedPrefs.getInt("petrol", 0)

            textViewCash.text = "${sum - transferSum} р."
            textViewNonCash.text = "$transferSum р."
            textViewCashFlows.text = "${profit - loss} р."
            textViewMeters.text = "${hot + cold + unusableHot + unusableCold} шт."
            textViewHot.text = "${hot + unusableHot} шт."
            textViewCold.text = "${cold + unusableCold} шт."
            textViewAverageTime.text = totalTime
            textViewTotalTime.text = totalTime
            textViewUnusableMeters.text = "${unusableCold + unusableHot} шт."
            textViewUnusableHot.text = "$unusableHot шт."
            textViewUnusableCold.text = "$unusableCold шт."
            textViewGiveGet.text = "${salary*(unusableHot+unusableCold+hot+cold) + petrol - transferSum - loss + profit} р."
            textViewProfit.text = "$profit р."
            textViewLoss.text = "$loss р."
            textViewMoney.text = "$sum р."
            textViewProcessedRouts.text = "$processedRoutes шт."
            textViewSalary.text = "${salary*(unusableHot+unusableCold+hot+cold)} р."
            textViewPetrol.text = "$petrol р."
            textViewTotal.text = "${sum - salary*(unusableHot+unusableCold+hot+cold) - petrol} р."

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}