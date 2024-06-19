package com.dicoding.capstone.ui.tabLayout.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.capstone.data.theme.SettingPreference
import kotlinx.coroutines.launch

class ProfileViewModel(private val pref: SettingPreference) : ViewModel() {
//    fun getTheme() = pref.getThemeSetting().asLiveData()
//
//    fun saveTheme(isDark: Boolean) {
//        viewModelScope.launch {
//            pref.saveThemeSetting(isDark)
//        }
//    }
//
//    class Setting(private val pref: SettingPreference) : ViewModelProvider.NewInstanceFactory() {
//        override fun <Theme : ViewModel> create(modelClass: Class<Theme>): Theme = ProfileViewModel(pref) as Theme
//    }
}