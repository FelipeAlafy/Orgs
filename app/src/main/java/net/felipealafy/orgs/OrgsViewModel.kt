package net.felipealafy.orgs

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

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
}