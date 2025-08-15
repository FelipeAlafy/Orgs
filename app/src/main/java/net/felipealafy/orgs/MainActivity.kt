package net.felipealafy.orgs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import net.felipealafy.orgs.ui.theme.OrgsTheme
import net.felipealafy.orgs.views.ProductsView

class MainActivity : ComponentActivity() {
    private val viewModel: OrgsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            OrgsTheme {
                ProductsView(
                    viewModel= viewModel,
                    navController = navController
                )
            }
        }
    }
}