package net.felipealafy.orgs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val descripton: String = "",
    val value: String = "",
    val urlImage: String = ""
)