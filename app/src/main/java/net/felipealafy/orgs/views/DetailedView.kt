package net.felipealafy.orgs.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import net.felipealafy.orgs.OrgsViewModel
import net.felipealafy.orgs.Product
import net.felipealafy.orgs.R
import net.felipealafy.orgs.ui.theme.DarkGray
import net.felipealafy.orgs.ui.theme.Green
import net.felipealafy.orgs.ui.theme.OrgsTypography

@Composable
fun DetailedView(viewModel: OrgsViewModel) {
    val product by viewModel.currentProduct.collectAsState()

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp)
    ) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                LargerImageViewer(product)
                Price(product, Modifier.align(Alignment.BottomStart).padding(8.dp).shadow(8.dp))
            }
            Column (
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 16.dp)
            ) {
                Title(product)
                Description(product)
            }
        }
    }
}

@Composable
fun LargerImageViewer(product: Product) {
    AsyncImage(
        model = product.urlImage,
        contentDescription = stringResource(R.string.product_image),
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun Price(product: Product, modifier: Modifier) {
    Text(
        text = "${stringResource(R.string.currency_unit)} ${product.value}",
        style = OrgsTypography.headlineLarge,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        color = Green
    )
}

@Composable
fun Title(product: Product) {
    Text(
        text = product.name,
        style = OrgsTypography.headlineLarge,
        color = DarkGray,
        modifier = Modifier.padding(top = 24.dp, start = 10.dp, end = 10.dp)
    )
}

@Composable
fun Description(product: Product) {
    Text(
        text = product.descripton,
        style = OrgsTypography.bodyMedium.copy(
            textAlign = TextAlign.Justify
        ),
        modifier = Modifier.padding(top = 24.dp, start = 10.dp, end = 10.dp)
    )
}