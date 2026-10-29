package com.example.persiapan_ucp1

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.persiapan_ucp1.DetailScreen
import com.example.persiapan_ucp1.HomeScreen

/**
 * Kumpulan route (alamat halaman) agar tidak ada string yang tertulis berulang.
 */
object Routes {
    // Route halaman pertama (formulir)
    const val HOME = "home"

    // Route halaman kedua (detail). Bagian {…} adalah placeholder argumen
    const val DETAIL = "detail/{origin}/{departure}/{arrival}/{transport}"

    /**
     * Membuat route lengkap beserta nilai data yang dikirim.
     * Uri.encode dipakai agar spasi / karakter khusus (mis. "29 Okt 2026")
     * aman dimasukkan ke dalam route.
     */
    fun detail(origin: String, departure: String, arrival: String, transport: String) =
        "detail/${Uri.encode(origin)}/${Uri.encode(departure)}/${Uri.encode(arrival)}/${Uri.encode(transport)}"
}

/**
 * Mengatur grafik navigasi: daftar halaman dan cara berpindah antar halaman.
 */
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    // NavController menyimpan back stack dan menjalankan perintah navigate / popBackStack
    val navController = rememberNavController()

    // NavHost = wadah yang menampilkan halaman sesuai route aktif
    NavHost(navController = navController, startDestination = Routes.HOME) {

        // ---------- Halaman 1: Formulir ----------
        composable(Routes.HOME) {
            HomeScreen(
                // Dipanggil HomeScreen saat data sudah valid; kirim data lewat route
                onSubmit = { origin, departure, arrival, transport ->
                    navController.navigate(Routes.detail(origin, departure, arrival, transport))
                }
            )
        }

        // ---------- Halaman 2: Detail Perjalanan ----------
        composable(
            route = Routes.DETAIL,
            // Deklarasi tipe tiap argumen yang diterima halaman ini
            arguments = listOf(
                navArgument("origin") { type = NavType.StringType },
                navArgument("departure") { type = NavType.StringType },
                navArgument("arrival") { type = NavType.StringType },
                navArgument("transport") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Parsing data: ambil nilai argumen dari entri back stack
            val args = backStackEntry.arguments
            DetailScreen(
                origin = args?.getString("origin").orEmpty(),
                departure = args?.getString("departure").orEmpty(),
                arrival = args?.getString("arrival").orEmpty(),
                transport = args?.getString("transport").orEmpty(),
                // Kembali ke halaman sebelumnya (formulir)
                onBack = { navController.popBackStack() }
            )
        }
    }
}