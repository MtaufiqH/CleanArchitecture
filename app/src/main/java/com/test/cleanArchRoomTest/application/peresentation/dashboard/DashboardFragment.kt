package com.test.cleanArchRoomTest.application.peresentation.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.cleanArchRoomTest.R
import com.test.cleanArchRoomTest.utils.ext.addTo
import com.test.cleanArchRoomTest.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment() {

     private val dashboardViewModel: DashboardViewModel by viewModels()

    private lateinit var binding: FragmentDashboardBinding

    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardViewModel.bound()
        observeData()
    }

    private fun observeData() {
        dashboardViewModel.charsList.observe(viewLifecycleOwner, Observer {
            binding.textDashboard.text = it.info?.next
        })
    }

    override fun onResume() {
        super.onResume()
        dashboardViewModel.showErrorGettingChars.observe()
            .subscribe {
                context?.let { it1 ->
                    AlertDialog.Builder(it1)
                    /* .setTitle(getString(string.error_title))
                     .setMessage(getString(string.restaurant_list_error_message))
                     .setNeutralButton(getString(string.ok)) { dialog, _ -> dialog.dismiss() }*/
                }
            }.addTo(disposables)
    }

    override fun onPause() {
        disposables.clear()
        super.onPause()
    }

    override fun onDestroy() {
        dashboardViewModel.unbound()
        super.onDestroy()
    }
}