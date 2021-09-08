package com.syrous.meditracker.view.detailMedicine

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.syrous.meditracker.MediTrackerApplication
import com.syrous.meditracker.R
import com.syrous.meditracker.databinding.FragmentMedicineDetailsBinding
import kotlinx.coroutines.flow.collect

class FragmentDetailMedicine: Fragment() {

    private lateinit var binding: FragmentMedicineDetailsBinding
    private val viewModel by viewModels<DetailMedicineVM>{
        (requireActivity().application as MediTrackerApplication)
            .appComponent
            .detailMedicineVMFactory
    }
    private val args by navArgs<FragmentDetailMedicineArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedicineDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMedicineDetails(args.medicineId)
        lifecycleScope.launchWhenStarted {
            viewModel.medicineDetailFlow.collect {
                if(it != null) {
                    binding.medicineDetails.apply {
                        title.text = it.medicineName
                        amount.text = resources.getString(R.string.text_expense_rupees) + it.amount
                        time.text = "${it.hours} : ${it.minutes}"
                        directionOfUse.text = it.directionToUse
                        createdAt.text = it.createdAtDateFormat
                        Glide.with(requireContext()).load(it.imageLocation).into(medicineImageView)
                    }
                }
            }
        }
    }
}