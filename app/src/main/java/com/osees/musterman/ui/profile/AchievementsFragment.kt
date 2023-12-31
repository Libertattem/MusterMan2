package com.osees.musterman.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.osees.musterman.R

class AchievementsFragment : Fragment() {

    companion object {
        fun newInstance() = AchievementsFragment()
    }

    private lateinit var viewModel: AchievementsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_achievements, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AchievementsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}