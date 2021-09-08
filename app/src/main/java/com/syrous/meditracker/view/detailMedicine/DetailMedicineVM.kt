package com.syrous.meditracker.view.detailMedicine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.syrous.meditracker.data.MedicineDao
import com.syrous.meditracker.usecase.impl.MedicineUseCaseImpl

class DetailMedicineVM(
    private val medicineDao: MedicineDao
): ViewModel(){
    private val medicineUseCase = MedicineUseCaseImpl(medicineDao, viewModelScope)

    val medicineDetailFlow = medicineUseCase.medicineDetailFlow

    fun getMedicineDetails(id: Long){
        medicineUseCase.getMedicineForId(id)
    }
}

class DetailMedicineVMFactory(private val medicineDao: MedicineDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = DetailMedicineVM(medicineDao) as T

}