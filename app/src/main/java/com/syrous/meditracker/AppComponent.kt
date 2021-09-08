package com.syrous.meditracker

import android.content.Context
import androidx.room.Room
import com.syrous.meditracker.data.MedicineDB
import com.syrous.meditracker.data.MedicineDao
import com.syrous.meditracker.view.addMedicine.AddMedicineVMFactory
import com.syrous.meditracker.view.dashboard.DashBoardViewModelFactory
import com.syrous.meditracker.view.detailMedicine.DetailMedicineVMFactory

class AppComponent(context: Context) {

    private val db = Room.databaseBuilder(
        context,
        MedicineDB::class.java,
        "medicine-expense-db"
    ).build()

    fun getMedicineDao(): MedicineDao = db.medicineDao()

    val dashboardVMFactory: DashBoardViewModelFactory = DashBoardViewModelFactory(
        medicineDao = db.medicineDao()
    )

    val detailMedicineVMFactory: DetailMedicineVMFactory = DetailMedicineVMFactory(
        medicineDao = db.medicineDao()
    )

    val addMedicineVMFactory = AddMedicineVMFactory(medicineDao = db.medicineDao())

}