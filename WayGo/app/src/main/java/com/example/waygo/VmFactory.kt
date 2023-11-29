package com.example.waygo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.waygo.datainject.Injection
import com.example.waygo.view.main.MainVM

class VmFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MainVM::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return MainVM(Injection.provideRepository(context)) as T
            }
//            modelClass.isAssignableFrom(FoodViewModel::class.java) -> {
//                @Suppress("UNCHECKED_CAST")
//                return FoodViewModel(Injection.provideFoodRepository(context)) as T
//            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
//class VmFactory (private val context: Context) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return MainViewModel(Injection.provideRepository(context)) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//
//
//
//}
