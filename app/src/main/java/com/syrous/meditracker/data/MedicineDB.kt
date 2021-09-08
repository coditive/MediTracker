package com.syrous.meditracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.syrous.meditracker.model.Medicine

@Database(entities = [Medicine::class], version = 1)
abstract class MedicineDB: RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
}