package com.ghina0096.assessment2_mobpro.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ghina0096.assessment2_mobpro.database.ResepDb
import com.ghina0096.assessment2_mobpro.ui.theme.screen.DetailViewModel
import com.ghina0096.assessment2_mobpro.ui.theme.screen.MainViewModel


class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val  dao = ResepDb.getInstance(context).dao
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dao) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}