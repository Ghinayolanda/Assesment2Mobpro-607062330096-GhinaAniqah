package com.ghina0096.assessment2_mobpro.ui.theme.screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghina0096.assessment2_mobpro.database.ResepDao
import com.ghina0096.assessment2_mobpro.model.Resep
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(dao: ResepDao) : ViewModel() {
    val data: StateFlow<List<Resep>> = dao.getResep().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
}