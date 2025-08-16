package net.felipealafy.orgs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class Product(
    val id: String = "",
    val name: String = "",
    val descripton: String = "",
    val value: String = "",
    val urlImage: String = ""
)