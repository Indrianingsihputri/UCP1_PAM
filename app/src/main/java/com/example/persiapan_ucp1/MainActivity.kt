package com.example.persiapan_ucp1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.persiapan_ucp1.ui.theme.Persiapan_UCP1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Persiapan_UCP1Theme {
                // Scaffold memberi innerPadding agar konten tidak tertutup status bar / navigation bar
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Greeting diganti dengan navigasi antar halaman
                    // (HomeScreen -> DetailScreen). Padding diteruskan lewat Modifier.
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Persiapan_UCP1Theme {
        Greeting("Android")
    }
}