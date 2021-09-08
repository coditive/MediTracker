package com.syrous.meditracker.usecase.impl

import android.util.Log
import com.syrous.meditracker.data.MedicineDao
import com.syrous.meditracker.model.Medicine
import com.syrous.meditracker.usecase.usecaseinterface.MedicineUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MedicineUseCaseImpl(
    private val medicineDao: MedicineDao,
    private val coroutineScope: CoroutineScope
): MedicineUseCase {

    private val _medicineDetailFlow = MutableStateFlow<Medicine?>(null)
    override val medicineDetailFlow: StateFlow<Medicine?>
        get() = _medicineDetailFlow

    override val medicineList: StateFlow<List<Medicine>>
        get() = medicineDao.getAllMedicinesFlow()
            .stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    override val expensiveMedicine: StateFlow<Medicine?>
        get() = medicineDao.getExpensiveMedicine()
            .stateIn(coroutineScope, SharingStarted.Lazily, null)

    private val _totalExpenseFlow = MutableStateFlow(0.0)
    override val totalExpenseFlow: StateFlow<Double>
        get() = _totalExpenseFlow

    private val _averageMedicineCostFlow = MutableStateFlow<Double?>(null)
    override val averageMedicineCostFlow: StateFlow<Double?>
        get() = _averageMedicineCostFlow

    override fun addMedicineToStorage(medicine: Medicine) {
        coroutineScope.launch(Dispatchers.IO) {
            medicineDao.insertMedicine(medicine)
        }
    }

    override fun getTotalMedicalExpense() {
        coroutineScope.launch {
            var amount = 0.0
            val medicineList = medicineDao.getAllMedicines()
            for(medicine in medicineList){
                amount += medicine.amount
            }
            _totalExpenseFlow.value = amount
        }
    }

    override fun getMedicineForId(id: Long) {
        coroutineScope.launch {
            val medicine = medicineDao.getMedicineDetails(id)
            Log.d("usecase", "medicine - $medicine")
            _medicineDetailFlow.emit(medicine)
        }
    }

    override fun deleteMedicineFromStorage(id: Long) {
        coroutineScope.launch {
            Log.d("usecase", "medicine selected to delete - $id")
            medicineDao.getMedicineDetails(id)
        }
    }

    override fun getAverageMedicineCost() {
        coroutineScope.launch(Dispatchers.IO) {
            var amount = 0.0
            val medicineList = medicineDao.getAllMedicines()
            for(medicine in medicineList){
                amount += medicine.amount
            }

            _averageMedicineCostFlow.value = amount.div(medicineList.size)
        }
    }
}