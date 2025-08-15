package net.felipealafy.orgs.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import net.felipealafy.orgs.OrgsViewModel
import net.felipealafy.orgs.Product
import net.felipealafy.orgs.R
import net.felipealafy.orgs.ui.theme.DarkGray
import net.felipealafy.orgs.ui.theme.Green
import net.felipealafy.orgs.ui.theme.LightBlue
import net.felipealafy.orgs.ui.theme.LightGray
import net.felipealafy.orgs.ui.theme.OrgsTypography
import net.felipealafy.orgs.ui.theme.Red
import net.felipealafy.orgs.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsView(
    navController: NavHostController = rememberNavController(),
    viewModel: OrgsViewModel,
) {
    val productsList by viewModel.productsList.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Views.valueOf(
        backStackEntry?.destination?.route ?: Views.Products.name
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Green,
                    titleContentColor = White
                ),
                title = {
                    Text(stringResource(R.string.app_name))
                },
                navigationIcon = {
                    if (currentScreen == Views.Products) return@TopAppBar
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_arrow),
                            tint = White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (currentScreen != Views.Products) return@Scaffold
            AddFloatingActionButton(onClick = {
                navController.navigate(Views.Register.name)
            })
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Views.Products.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable (route = Views.Products.name) {
                LazyColumn(
                    modifier = Modifier.padding(8.dp)
                ) {
                    items(productsList) { product ->
                        SwipeActionsForProductCard(product, swipeActions = {
                            swipeAction -> viewModel.swipeActions(
                                product,
                                swipeAction,
                                navController
                            )
                        })
                    }
                }
            }
            composable(route = Views.Register.name) {
                RegisterView(
                    navController= navController,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun SwipeActionsForProductCard(product: Product, swipeActions: (SwipeToDismissBoxValue) -> Boolean) {

    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = swipeActions
    )

    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        modifier = Modifier.fillMaxSize(),
        backgroundContent = {
            when (swipeToDismissBoxState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(R.string.edit_button),
                        modifier = Modifier
                            .fillMaxSize()
                            .background(LightBlue)
                            .wrapContentSize(Alignment.CenterStart)
                            .padding(12.dp),
                        tint = White
                    )
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.edit_button),
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Red)
                            .wrapContentSize(Alignment.CenterEnd)
                            .padding(12.dp),
                        tint = White
                    )
                }
                SwipeToDismissBoxValue.Settled -> {}
            }
        }
    ) {
        ProductCard(product)
    }
}

@Composable
fun ProductCard(product: Product) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(150.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Row {
            ProductView(product)
            Column (
                Modifier.padding(8.dp)
            ) {
                ProductName(product)
                ProductDescription(product)
                ProductValue(product)
            }
        }
    }
}

@Composable
fun ProductView(product: Product) {
    AsyncImage(
        model = product.urlImage,
        contentDescription = stringResource(R.string.product_image),
        modifier = Modifier.size(150.dp)
    )
}

@Composable
fun ProductName(product: Product) {
    Text(
        text = product.name,
        style = OrgsTypography.bodyLarge,
        color = DarkGray,
        modifier = Modifier.padding(top = 14.dp)
    )
}

@Composable
fun ProductDescription(product: Product) {
    Text(
        text = product.descripton,
        style = OrgsTypography.labelSmall,
        color = LightGray,
        modifier = Modifier.padding(top = 6.dp)
    )
}

@Composable
fun ProductValue(product: Product) {
    Text(
        text = "${stringResource(R.string.currency_unit)} ${product.value}",
        color = Green,
        style = OrgsTypography.bodyMedium,
        modifier = Modifier.padding(top = 20.dp)
    )
}

@Composable
fun AddFloatingActionButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = Green,
        contentColor = White,
        shape = CircleShape
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_button),
        )
    }
}
