package net.felipealafy.orgs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.felipealafy.orgs.database.ProductDao

class OrgsViewModelFactory(private val dao: ProductDao) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrgsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrgsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}