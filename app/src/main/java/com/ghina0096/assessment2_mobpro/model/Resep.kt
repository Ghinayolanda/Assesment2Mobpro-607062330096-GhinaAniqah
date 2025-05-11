package com.ghina0096.assessment2_mobpro.model


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "resep")
data class Resep(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val namaMasakan: String,
    val resep: String,
    val tanggal: String
)
