package net.felipealafy.orgs.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.felipealafy.orgs.R
import net.felipealafy.orgs.ui.theme.OrgsTypography


@Composable
fun RegisterView(navController: NavController) {
    val image = MutableStateFlow("")
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ImageViewer(image)
        TextInput(
            textInputContent = MutableStateFlow(""),
            label = R.string.product_name,
            onValueChange = {}
        )
        TextInput(
            textInputContent = image,
            label = R.string.product_url,
            onValueChange = {
                image.value = it
            }
        )
        TextInput(
            textInputContent = MutableStateFlow(""),
            label = R.string.product_description,
            onValueChange = {},
            singleLine = false
        )
        TextInput(
            textInputContent = MutableStateFlow(""),
            label = R.string.product_value,
            onValueChange = {}
        )
        Spacer(modifier = Modifier.weight(1f))
        SaveButton(onClick = {
            navController.navigateUp()
        })
    }
}

@Composable
fun ImageViewer(urlImage: StateFlow<String>) {
    val url by urlImage.collectAsState()
    if (url.isBlank()) {
        FallbackImageViewer()
        return
    }
    UrlImageViewer(url)
}

@Composable
fun UrlImageViewer(url: String) {
    AsyncImage(
        model = url,
        contentDescription = stringResource(R.string.product_image),
        modifier = Modifier.height(300.dp)
    )
}
@Composable
fun FallbackImageViewer() {
    val defaultImage = "https://cchrcambodia.org/media/no-image.png"
    AsyncImage(
        model = defaultImage,
        contentDescription = stringResource(R.string.product_image),
        modifier = Modifier.height(300.dp)
    )
}

@Composable
fun TextInput(textInputContent: StateFlow<String>, label: Int, onValueChange: (String) -> Unit, singleLine: Boolean = true) {
    val text by textInputContent.collectAsState()
    OutlinedTextField(
        value = text,
        singleLine = singleLine,
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
}

@Composable
fun SaveButton(onClick: () -> Unit) {
    Button (
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = net.felipealafy.orgs.ui.theme.Green,
            contentColor = net.felipealafy.orgs.ui.theme.White
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = stringResource(R.string.save_button),
            style = OrgsTypography.bodyLarge
        )
    }
}