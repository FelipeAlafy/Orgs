package net.felipealafy.orgs

import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import android.util.Log
import kotlinx.coroutines.flow.update
import net.felipealafy.orgs.views.Views

class OrgsViewModel : ViewModel() {
    private val _defaultProduct = Product()
    private val _productsList = MutableStateFlow<List<Product>>(emptyList())
    private val _currentProduct = MutableStateFlow(_defaultProduct.copy())
    private val _nameTextInput = MutableStateFlow("")
    private val _urlTextInput = MutableStateFlow("")
    private val _descriptionTextInput = MutableStateFlow("")
    private val _valueTextInput = MutableStateFlow("")
    val productsList: StateFlow<List<Product>> = _productsList
    val nameTextInput: StateFlow<String> = _nameTextInput
    val urlTextInput: StateFlow<String> = _urlTextInput
    val descriptionTextInput: StateFlow<String> = _descriptionTextInput
    val valueTextInput: StateFlow<String> = _valueTextInput
    val currentProduct: StateFlow<Product> = _currentProduct

    fun addProduct() {
        val newProduct = getProduct()

        _productsList.update { list ->
            list + newProduct
        }
        _currentProduct.value = _defaultProduct.copy()
        cleanTextsInput()
    }

    private fun getProduct() =
        _currentProduct.value.copy(
            id = _productsList.value.size + 1
        ).apply {
            name = nameTextInput.value
            urlImage = urlTextInput.value
            descripton = descriptionTextInput.value
            value = valueTextInput.value
        }

    fun cleanTextsInput() {
        _nameTextInput.value = ""
        _urlTextInput.value = ""
        _descriptionTextInput.value = ""
        _valueTextInput.value = ""
    }

    val onNameEditFieldTextChanged: (String) -> Unit = { newText ->
        _nameTextInput.update {
            newText
        }
    }

    val onImageEditFieldTextChanged: (String) -> Unit = { newText ->
        _urlTextInput.update {
            newText
        }
    }

    val onDescriptionEditFieldTextChanged: (String) -> Unit = { newText ->
        _descriptionTextInput.update {
            newText
        }
    }

    val onValueEditFieldTextChanged: (String) -> Unit = { newText ->
        _valueTextInput.update {
            newText
        }
    }

    fun editProduct(product: Product) {
        _currentProduct.update { product.copy() }
        _nameTextInput.update { product.name }
        _urlTextInput.update { product.urlImage }
        _descriptionTextInput.update { product.descripton }
        _valueTextInput.update { product.value }
    }

    fun removeProduct(product: Product) {
        _productsList.update { list ->
            list - product
        }
    }

    val swipeActions: (Product, SwipeToDismissBoxValue, navController: NavHostController) -> Boolean = { product, swipeAction, navController ->
        when (swipeAction) {
            SwipeToDismissBoxValue.StartToEnd -> {
                editProduct(product)
                removeProduct(product)
                navController.navigate(Views.Register.name)
                true
            }
            SwipeToDismissBoxValue.EndToStart -> {
                removeProduct(product)
                true
            }
            SwipeToDismissBoxValue.Settled -> {
                false
            }
        }
    }

    fun selectProduct(product: Product) {
        _currentProduct.update {
            product
        }
    }
}