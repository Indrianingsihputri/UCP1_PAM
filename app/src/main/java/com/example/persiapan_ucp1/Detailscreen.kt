package com.example.persiapan_ucp1

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Halaman kedua: menampilkan ringkasan perjalanan dari data yang dikirim halaman pertama.
 *
 * Data diterima sebagai parameter biasa (hasil parsing argumen navigasi),
 * sehingga composable ini mudah diuji dan dipakai ulang.
 *
 * @param onBack callback untuk kembali ke halaman formulir
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    origin: String,
    departure: String,
    arrival: String,
    transport: String,
    onBack: () -> Unit
) {
    // Scaffold menyediakan kerangka halaman Material 3 (app bar + konten)
    Scaffold(
        // ==================== APP BAR + TOMBOL KEMBALI (ATAS) ====================
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail Perjalanan") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        // innerPadding wajib dipakai agar konten tidak tertutup app bar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ==================== JUDUL ====================
            Text("Ringkasan Perjalanan", style = MaterialTheme.typography.headlineSmall)

            // ==================== KARTU RINGKASAN ====================
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Tiap baris: label kecil di atas, nilai di bawahnya
                    SummaryRow("Kota Asal", origin)
                    HorizontalDivider()
                    SummaryRow("Tanggal Berangkat", departure)
                    HorizontalDivider()
                    SummaryRow("Tanggal Tiba", arrival)
                    HorizontalDivider()
                    SummaryRow("Transportasi", transport)
                }
            }

            // Spacer berbobot mendorong tombol ke bagian bawah layar
            Spacer(Modifier.weight(1f))

            // ==================== TOMBOL KEMBALI (BAWAH) ====================
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Kembali")
            }
        }
    }
}

/**
 * Satu baris ringkasan: label (abu-abu, kecil) dan nilainya (tebal, besar).
 */
@Composable
private fun SummaryRow(label: String, value: String) {
    Column {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(value, style = MaterialTheme.typography.titleMedium)
    }
}