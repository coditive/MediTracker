package com.syrous.meditracker.view.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.syrous.meditracker.data.MedicineDao
import com.syrous.meditracker.usecase.impl.MedicineUseCaseImpl
import com.syrous.meditracker.usecase.usecaseinterface.MedicineUseCase

class DashboardVM(
    private val medicineDao: MedicineDao
): ViewModel() {
    private val medicineUseCase: MedicineUseCase = MedicineUseCaseImpl(medicineDao, viewModelScope)
    val averageMedicineCostFlow = medicineUseCase.averageMedicineCostFlow
    val expensiveMedicineFlow = medicineUseCase.expensiveMedicine
    val medicineListFlow = medicineUseCase.medicineList
    val totalMedicalExpense = medicineUseCase.totalExpenseFlow
    fun getTotalMedicalExpense() {
        medicineUseCase.getTotalMedicalExpense()
    }

    fun deleteMedicine(id: Long) {
        medicineUseCase.deleteMedicineFromStorage(id)
    }

    fun getAverageMedicineExpense() {
        medicineUseCase.getAverageMedicineCost()
    }
}

class DashBoardViewModelFactory(private val medicineDao: MedicineDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = DashboardVM(medicineDao) as T

}
