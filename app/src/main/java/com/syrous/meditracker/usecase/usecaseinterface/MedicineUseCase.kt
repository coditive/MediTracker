package com.syrous.meditracker.usecase.usecaseinterface

import com.syrous.meditracker.model.Medicine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface MedicineUseCase {

    val medicineList: StateFlow<List<Medicine>>
    val expensiveMedicine: StateFlow<Medicine?>
    val totalExpenseFlow: StateFlow<Double>
    val medicineDetailFlow: StateFlow<Medicine?>
    val averageMedicineCostFlow: StateFlow<Double?>
    fun addMedicineToStorage(medicine: Medicine)
    fun getTotalMedicalExpense()
    fun getMedicineForId(id: Long)
    fun deleteMedicineFromStorage(id: Long)
    fun getAverageMedicineCost()
}