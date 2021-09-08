package com.syrous.meditracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.syrous.meditracker.model.Medicine
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMedicine(medicine: Medicine)

    @Query("SELECT * FROM Medicine")
    fun getAllMedicinesFlow(): Flow<List<Medicine>>

    @Query("SELECT * FROM Medicine")
    suspend fun getAllMedicines(): List<Medicine>

    @Query("DELETE FROM MEDICINE WHERE id = :id")
    suspend fun deleteMedicine(id: Long)

    @Query("SELECT * FROM medicine where amount = (select max(amount) from medicine)")
    fun getExpensiveMedicine(): Flow<Medicine>

    @Query("SELECT * FROM medicine where id = :id")
    suspend fun getMedicineDetails(id: Long): Medicine
}