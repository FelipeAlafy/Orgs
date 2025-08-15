package net.felipealafy.orgs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import net.felipealafy.orgs.ui.theme.OrgsTheme
import net.felipealafy.orgs.views.ProductsView

class MainActivity : ComponentActivity() {
    val viewModel: OrgsViewModel = OrgsViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OrgsTheme {
                ProductsView(viewModel = viewModel)
            }
        }
    }
}