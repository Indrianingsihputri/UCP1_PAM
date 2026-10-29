package com.example.persiapan_ucp1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// Daftar pilihan transportasi yang tersedia
private val transportOptions = listOf("Bus", "Ship", "Train", "Plane")

/**
 * Halaman utama: profil pengguna + formulir perencanaan perjalanan.
 *
 * @param onSubmit callback yang dipanggil HANYA jika seluruh data valid.
 *                 Halaman ini tidak tahu cara berpindah halaman; itu urusan navigasi.
 */
@Composable
fun HomeScreen(onSubmit: (origin: String, departure: String, arrival: String, transport: String) -> Unit) {

    // ==================== STATE ====================
    // mutableStateOf: saat nilai berubah, UI otomatis di-recompose.
    // rememberSaveable: nilai tetap ada walau layar diputar / activity dibuat ulang.
    var origin by rememberSaveable { mutableStateOf("") }
    var departure by rememberSaveable { mutableStateOf("") }
    var arrival by rememberSaveable { mutableStateOf("") }
    var transport by rememberSaveable { mutableStateOf("") }

    // Penanda apakah tombol Submit sudah pernah ditekan.
    // Pesan error baru muncul setelah submit, bukan sejak awal.
    var submitted by rememberSaveable { mutableStateOf(false) }

    // Kolom utama; bisa di-scroll agar aman di layar kecil
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ==================== BAGIAN PROFIL ====================
        ProfileHeader(name = "Budi Santoso", location = "Yogyakarta, Indonesia")

        HorizontalDivider()

        // ==================== JUDUL FORMULIR ====================
        Text("Plan Your Adventures", style = MaterialTheme.typography.headlineSmall)

        // ==================== INPUT: ORIGIN ====================
        OutlinedTextField(
            value = origin,
            onValueChange = { origin = it },   // update state setiap pengguna mengetik
            label = { Text("Origin") },
            singleLine = true,
            isError = submitted && origin.isBlank(),   // merah jika kosong setelah submit
            supportingText = {
                if (submitted && origin.isBlank()) Text("Kota asal wajib diisi")
            },
            modifier = Modifier.fillMaxWidth()
        )

        // ==================== INPUT: TANGGAL BERANGKAT ====================
        DatePickerField(
            label = "Departure",
            value = departure,
            isError = submitted && departure.isBlank(),
            errorText = "Tanggal keberangkatan wajib diisi",
            onDateSelected = { departure = it }
        )

        // ==================== INPUT: TANGGAL KEDATANGAN ====================
        DatePickerField(
            label = "Arrival",
            value = arrival,
            isError = submitted && arrival.isBlank(),
            errorText = "Tanggal kedatangan wajib diisi",
            onDateSelected = { arrival = it }
        )

        // ==================== INPUT: TRANSPORTASI (PILIH SATU) ====================
        Text("Choose Transportation", style = MaterialTheme.typography.titleMedium)

        // selectableGroup + RadioButton memastikan hanya satu opsi yang terpilih
        Column(Modifier.selectableGroup()) {
            transportOptions.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        // Seluruh baris bisa diklik, bukan hanya lingkaran radio
                        .selectable(
                            selected = transport == option,
                            onClick = { transport = option },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // onClick = null karena klik sudah ditangani oleh Row di atas
                    RadioButton(selected = transport == option, onClick = null)
                    Spacer(Modifier.width(12.dp))
                    Text(option)
                }
            }
        }

        // Pesan error jika belum ada transportasi yang dipilih
        if (submitted && transport.isBlank()) {
            Text(
                "Pilih salah satu jenis transportasi",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // ==================== TOMBOL SUBMIT + VALIDASI ====================
        Button(
            onClick = {
                submitted = true // aktifkan tampilan pesan error

                // Validasi sederhana: semua field harus terisi
                val isValid = origin.isNotBlank() && departure.isNotBlank() &&
                        arrival.isNotBlank() && transport.isNotBlank()

                // Hanya lanjut ke halaman berikutnya jika data lengkap
                if (isValid) onSubmit(origin.trim(), departure, arrival, transport)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}

/**
 * Bagian atas halaman: foto profil, nama pengguna, dan lokasi tempat tinggal.
 */
@Composable
private fun ProfileHeader(name: String, location: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        // Foto profil berbentuk lingkaran.
        // Ganti Icon ini dengan Image(painterResource(R.drawable.xxx)) jika punya foto.
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(72.dp)
        ) {
            Icon(
                Icons.Filled.Person,
                contentDescription = "Foto profil",
                modifier = Modifier.padding(16.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(Modifier.width(16.dp))

        // Nama dan lokasi di sebelah kanan foto
        Column {
            Text(name, style = MaterialTheme.typography.titleLarge)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.LocationOn,
                    contentDescription = null, // dekoratif, tidak perlu dibacakan
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Field tanggal yang dapat dipakai ulang (reusable composable).
 * Menampilkan kolom teks read-only; ikon kalender membuka dialog pemilih tanggal.
 *
 * @param onDateSelected dipanggil dengan tanggal yang sudah diformat setelah pengguna menekan OK
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerField(
    label: String,
    value: String,
    isError: Boolean,
    errorText: String,
    onDateSelected: (String) -> Unit
) {
    // State lokal: apakah dialog sedang tampil
    var showPicker by remember { mutableStateOf(false) }
    // State bawaan Material 3 yang menyimpan tanggal terpilih
    val pickerState = rememberDatePickerState()

    // Kolom teks: pengguna tidak mengetik, hanya memilih lewat dialog
    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        placeholder = { Text("Pilih tanggal") },
        trailingIcon = {
            IconButton(onClick = { showPicker = true }) {
                Icon(Icons.Filled.DateRange, contentDescription = "Pilih tanggal $label")
            }
        },
        isError = isError,
        supportingText = { if (isError) Text(errorText) },
        modifier = Modifier.fillMaxWidth()
    )

    // Dialog kalender, hanya dibuat saat showPicker = true
    if (showPicker) {
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    // Ubah milidetik menjadi teks tanggal lalu kirim ke pemanggil
                    pickerState.selectedDateMillis?.let { onDateSelected(formatDate(it)) }
                    showPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) { Text("Batal") }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }
}

/**
 * Mengubah milidetik (hasil DatePicker) menjadi teks, contoh: "29 Okt 2026".
 * Zona waktu UTC dipakai karena DatePicker Material 3 mengembalikan waktu UTC;
 * tanpa ini tanggal bisa bergeser sehari.
 */
private fun formatDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}