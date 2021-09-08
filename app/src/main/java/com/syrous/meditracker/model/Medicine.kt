package com.syrous.meditracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.DateFormat


@Entity
data class Medicine(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val medicineName: String,
    val amount: Double,
    val hours: Int,
    val minutes: Int,
    val imageLocation: String,
    val directionToUse: String,
    val createdAt: Long = System.currentTimeMillis()
): Serializable {
    val createdAtDateFormat: String
        get() = DateFormat.getDateInstance()
            .format(createdAt)
}