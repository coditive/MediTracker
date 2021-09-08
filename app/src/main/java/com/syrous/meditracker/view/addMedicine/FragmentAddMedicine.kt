package com.syrous.meditracker.view.addMedicine

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.syrous.meditracker.MediTrackerApplication
import com.syrous.meditracker.databinding.FragmentAddMedicineBinding
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FragmentAddMedicine: Fragment() {

    private lateinit var binding: FragmentAddMedicineBinding
    private var isImageCaptured = false
    private val REQUEST_IMAGE_CAPTURE = 1001
    private val viewModel by viewModels<AddMedicineVM>{
        (requireActivity().application as MediTrackerApplication)
            .appComponent
            .addMedicineVMFactory
    }
    private var imageLocation = ""
    private var hours = 0
    private var minutes = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMedicineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveMedicine.setOnClickListener {
            if(isImageCaptured) {
                val name = binding.addTransactionLayout.etName.text.toString()
                val amount = binding.addTransactionLayout.etAmount.text.toString()
                val directionToUse = binding.addTransactionLayout.etDirectionOfUse.text.toString()
                viewModel.addMedicineToStorage(name, amount.toDouble(), imageLocation,
                    hours, minutes, directionToUse)
                findNavController().popBackStack()
            }
        }

        binding.addTransactionLayout.clickOverLay.setOnClickListener{
            Log.d("FragmentAdd", "medication Time Clicked!!!")
            getMedicationTime()
        }

        binding.takeMedicinePictureBtn.setOnClickListener {
            captureMedicineImage()
        }
    }

    private fun getMedicationTime() {
        Log.d("FragmentAdd", "medication Time Clicked!!!")
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(10)
            .setTitleText("Select Medication Time")
            .build()

        timePicker.addOnPositiveButtonClickListener {
            binding.addTransactionLayout.etTime.setText("${timePicker.hour} : ${timePicker.minute}")
            hours = timePicker.hour
            minutes = timePicker.minute
        }

        timePicker.show(requireActivity().supportFragmentManager, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) setPic()
    }

    private fun setPic () {
        isImageCaptured = true
        Glide.with(this).load(imageLocation).into(binding.addTransactionLayout.medicationImage)
        binding.addTransactionLayout.medicationImage.visibility = View.VISIBLE
        binding.takeMedicinePictureBtn.visibility = View.GONE
        binding.btnSaveMedicine.visibility = View.VISIBLE
    }


    private fun captureMedicineImage() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            captureIntent ->
            captureIntent.resolveActivity(requireActivity().packageManager)?.also {
                val photoFile: File? = try {
                    createFile()
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "Unable to create image file",
                        Toast.LENGTH_SHORT
                    ).show()
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.syrous.meditracker.fileprovider",
                        it
                    )
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(captureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createFile(): File? {
        val timestamp: String = SimpleDateFormat("yyyMMdd_HHmmss", Locale.ENGLISH ).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timestamp}_",
            ".jpg",
            storageDir
        ).apply {
            val uri = FileProvider.getUriForFile(requireContext(), "com.syrous.meditracker.fileprovider", this)
            imageLocation = uri.toString()
        }
    }
}