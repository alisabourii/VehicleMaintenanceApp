package com.example.vehiclemaintenanceapp.ui.slideshow

import android.content.Context
import java.io.File

object CsvStore {
    private const val DEFAULT_FILE = "vehicles.csv"

    /** Bir satır ekler: ["Otomobil","Toyota","Corolla","2020","1.6"] */
    fun appendRow(context: Context, row: List<String>, fileName: String = DEFAULT_FILE) {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) file.createNewFile()
        // Basit CSV: virgülle birleştir, sonuna newline
        file.appendText(row.joinToString(",") + "\n", Charsets.UTF_8)
    }

    /** Tüm satırları okur (her satır List<String>) */
    fun readRows(context: Context, fileName: String = DEFAULT_FILE): List<List<String>> {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) return emptyList()
        return file.readLines(Charsets.UTF_8)
            .filter { it.isNotBlank() }
            .map { it.split(",") }
    }

    /** Dosyayı temizler */
    fun clear(context: Context, fileName: String = DEFAULT_FILE) {
        val file = File(context.filesDir, fileName)
        if (file.exists()) file.delete()
    }

    /** Debug için dosya yolunu verir */
    fun filePath(context: Context, fileName: String = DEFAULT_FILE): String =
        File(context.filesDir, fileName).absolutePath
}
