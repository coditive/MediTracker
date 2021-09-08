package com.syrous.meditracker.view.addMedicine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.syrous.meditracker.data.MedicineDao
import com.syrous.meditracker.model.Medicine
import com.syrous.meditracker.usecase.impl.MedicineUseCaseImpl

class AddMedicineVM(
    private val medicineDao: MedicineDao
): ViewModel() {
    private var medicineUseCase = MedicineUseCaseImpl(medicineDao, viewModelScope)

    fun addMedicineToStorage(
        name: String, amount: Double, imageLocation: String,
        hours: Int, minutes: Int, directionToUse: String
    ) {
        val medicine = Medicine(
            medicineName =  name,
            amount = amount,
            imageLocation = imageLocation,
            hours = hours,
            minutes = minutes,
            directionToUse = directionToUse
        )
       medicineUseCase.addMedicineToStorage(medicine)
    }
}

class AddMedicineVMFactory(private val medicineDao: MedicineDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = AddMedicineVM(medicineDao) as T
}