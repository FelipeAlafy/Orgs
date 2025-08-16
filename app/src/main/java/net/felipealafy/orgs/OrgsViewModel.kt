package net.felipealafy.orgs

import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class OrgsViewModel : ViewModel() {
    private val _defaultProduct = Product()
    private val _productsList = MutableStateFlow<List<Product>>(emptyList())
    private val _currentProduct = MutableStateFlow(_defaultProduct.copy())
    private val _nameTextInput = MutableStateFlow("")
    private val _urlTextInput = MutableStateFlow("")
    private val _descriptionTextInput = MutableStateFlow("")
    private val _valueTextInput = MutableStateFlow("")
    private val _navigationEvent = Channel<NavigationEvent>()
    private var editingProduct: Product? = null
    val productsList: StateFlow<List<Product>> = _productsList
    val nameTextInput: StateFlow<String> = _nameTextInput
    val urlTextInput: StateFlow<String> = _urlTextInput
    val descriptionTextInput: StateFlow<String> = _descriptionTextInput
    val valueTextInput: StateFlow<String> = _valueTextInput
    val currentProduct: StateFlow<Product> = _currentProduct
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun saveProduct() {
        val productToSave = getProduct()

        if (editingProduct != null) {
            _productsList.update { currentList ->
                currentList.map { productInList ->
                    if (productInList.id == productToSave.id) {
                        productToSave
                    } else {
                        productInList
                    }
                }
            }
        } else {
            _productsList.update { currentList ->
                currentList + productToSave
            }
        }

        clearSaving()
    }

    private fun getProduct() =
        _currentProduct.value.copy(
            id = editingProduct?.id ?: UUID.randomUUID().toString(),
            name = nameTextInput.value,
            urlImage = urlTextInput.value,
            descripton = descriptionTextInput.value,
            value = valueTextInput.value
        )

    private fun clearSaving() {
        _currentProduct.value = _defaultProduct.copy()
        cleanTextsInput()
        editingProduct = null
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

    fun startEditMode(product: Product) {
        editingProduct = product
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

    val swipeActions: (Product, SwipeToDismissBoxValue) -> Boolean = { product, swipeAction ->
        when (swipeAction) {
            SwipeToDismissBoxValue.StartToEnd -> {
                startEditMode(product)
                viewModelScope.launch {
                    _navigationEvent.send(NavigationEvent.NavigateToRegister)
                }
                false
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

sealed class NavigationEvent {
    data object NavigateToRegister : NavigationEvent()
    data object NavigateUp : NavigationEvent()
}