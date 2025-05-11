package com.ghina0096.assessment2_mobpro.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ghina0096.assessment2_mobpro.model.Resep
import kotlinx.coroutines.flow.Flow

@Dao
interface ResepDao {

    @Insert
    suspend fun insert(resep: Resep)

    @Update
    suspend fun update(resep: Resep)

    @Query("SELECT * FROM resep ORDER BY tanggal DESC")
    fun getResep(): Flow<List<Resep>>

    @Query("SELECT * FROM resep WHERE id = :id")
    suspend fun getResepById(id: Long): Resep?

    @Query("DELETE FROM resep WHERE id = :id")
    suspend fun deleteById(id: Long)
}
