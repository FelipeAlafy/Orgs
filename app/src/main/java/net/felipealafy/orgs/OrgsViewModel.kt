package net.felipealafy.orgs

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.felipealafy.orgs.database.ProductDao
import java.util.UUID

class OrgsViewModel(private val dao: ProductDao) : ViewModel() {
    private val _defaultProduct = Product()
    private val _currentProduct = MutableStateFlow(_defaultProduct.copy())
    private val _nameTextInput = MutableStateFlow("")
    private val _urlTextInput = MutableStateFlow("")
    private val _descriptionTextInput = MutableStateFlow("")
    private val _valueTextInput = MutableStateFlow("")
    private val _navigationEvent = Channel<NavigationEvent>()
    private var editingProduct: Product? = null
    val nameTextInput: StateFlow<String> = _nameTextInput
    val urlTextInput: StateFlow<String> = _urlTextInput
    val descriptionTextInput: StateFlow<String> = _descriptionTextInput
    val valueTextInput: StateFlow<String> = _valueTextInput
    val currentProduct: StateFlow<Product> = _currentProduct
    val navigationEvent = _navigationEvent.receiveAsFlow()
    val productsList: StateFlow<List<Product>> = dao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    fun saveProduct() {
        val productToSave = getProduct()

        viewModelScope.launch {
            if (editingProduct != null) {
                dao.update(productToSave)
            } else {
                dao.insert(productToSave)
            }
        }

        clearSaving()
    }

    private fun getProduct() =
        Product(
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
        viewModelScope.launch {
            dao.delete(product)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
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