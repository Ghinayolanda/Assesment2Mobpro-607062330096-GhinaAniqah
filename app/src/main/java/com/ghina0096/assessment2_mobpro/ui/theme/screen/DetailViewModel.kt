package com.ghina0096.assessment2_mobpro.ui.theme.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghina0096.assessment2_mobpro.database.ResepDao
import com.ghina0096.assessment2_mobpro.model.Resep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel(private val dao: ResepDao): ViewModel() {

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    fun insert(namaMasakan: String, isiResep: String) {
        val resep = Resep(
            tanggal = formatter.format(Date()),
            namaMasakan = namaMasakan,
            resep = isiResep
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(resep)
        }
    }

    suspend fun getResep(id: Long): Resep? {
        return dao.getResepById(id)
    }

    fun  update(id: Long, namaMasakan: String, isiResep: String) {
        val resep =Resep(
            id = id,
            tanggal = formatter.format(Date()),
            namaMasakan = namaMasakan,
            resep = isiResep
        )

        viewModelScope.launch (Dispatchers.IO){
            dao.update(resep)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch (Dispatchers.IO){
            dao.deleteById(id)
        }
    }
}