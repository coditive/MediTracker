package com.syrous.meditracker.view.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.syrous.meditracker.MediTrackerApplication
import com.syrous.meditracker.R
import com.syrous.meditracker.databinding.FragmentDashboardBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FragmentDashboard: Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private val medicineAdapter = MedicineAdapter(ClickHandler())
    private val viewModel by viewModels<DashboardVM> {
        (requireActivity().application as MediTrackerApplication)
            .appComponent
            .dashboardVMFactory
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        binding.medicineRv.apply {
            adapter = medicineAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.btnAddTransaction.setOnClickListener {
            findNavController().navigate(FragmentDashboardDirections
                .actionFragmentDashboardToFragmentAddMedicine())
        }
        setUpUI()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAverageMedicineExpense()
    }

    @SuppressLint("SetTextI18n")
    private fun setUpUI() {
        binding.apply {
            highestMedicineNameView.totalTitle.text = "Expensive Medicine Name"
            highestMedicineExpenseView.totalTitle.text = "Average Medicine Cost"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpViewModel() {
        viewModel.medicineListFlow
            .onEach(medicineAdapter::submitList)
            .launchIn(lifecycleScope)

        viewModel.getTotalMedicalExpense()

        lifecycleScope.launchWhenStarted {
            viewModel.expensiveMedicineFlow.collect {
                if (it != null) {
                    binding.highestMedicineNameView.total.text = it.medicineName
                } else {
                    binding.highestMedicineNameView.total.text = ""
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.averageMedicineCostFlow.collect {
                if (it != null && !it.isNaN()) {
                    binding.highestMedicineExpenseView.total.text =
                        resources.getString(R.string.text_expense_rupees) + it
                } else {
                    binding.highestMedicineExpenseView.total.text =
                        resources.getString(R.string.text_expense_rupees) + "0"
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.totalMedicalExpense.collect {
                binding.totalMedicalExpenseView.totalExpense.text =
                    resources.getString(R.string.text_expense_rupees) + it
            }
        }
    }

    inner class ClickHandler {
        fun onClick(id: Long) {
          findNavController().navigate(FragmentDashboardDirections
              .actionFragmentDashboardToFragmentDetailMedicine(id))
        }

        fun deleteItem(id: Long) {
            Log.d("FragmentDash", "medicine selected to delete - $id")
            viewModel.deleteMedicine(id)
        }
    }
}