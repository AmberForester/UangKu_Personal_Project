package com.example.uangku.feature.export_excel.domain

enum class ExportPeriodOption(
    val title: String,
    val description: String
) {
    CURRENT_FINANCIAL_PERIOD(
        title = "Periode Berjalan",
        description = "Sesuai siklus periode keuangan aktif saat ini"
    ),
    THIS_MONTH(
        title = "Bulan Ini",
        description = "Mulai dari tanggal 1 hingga akhir bulan kalender"
    ),
    ALL_TIME(
        title = "Semua Riwayat",
        description = "Seluruh catatan transaksi yang tersimpan di UangKu"
    )
}
