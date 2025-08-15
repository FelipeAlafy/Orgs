package net.felipealafy.orgs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class Product(val id: Int = 0) {
    var name by mutableStateOf("")
    var descripton by mutableStateOf("")
    var value by mutableStateOf("")
    var urlImage by mutableStateOf("")
}