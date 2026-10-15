package com.sekota.features.reader.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ReadingMode {
    DAY,
    NIGHT,
    SEPIA
}

@Serializable
data class ReaderChapter(
    val id: String,
    val label: String,
    val title: String,
    val subtitle: String,
    val page: Int
)

@Serializable
data class BookReadingProgress(
    val bookId: String,
    val chapterIndex: Int = 0,
    val percent: Int = 0,
    val isBookmarked: Boolean = false,
    val fontSizeSp: Int = 17,
    val readingMode: ReadingMode = ReadingMode.DAY,
    val lastReadTimestamp: Long = 0L
)

object ReaderDefaults {
    val CANONICAL_CHAPTERS = listOf(
        ReaderChapter(
            id = "pendahuluan",
            label = "Pendahuluan",
            title = "Sinyal yang Terlewat",
            subtitle = "Mengapa organisasi yang cerdas tetap bisa tersandung pada hal yang terlihat jelas.",
            page = 1
        ),
        ReaderChapter(
            id = "bab-1",
            label = "Bab 1",
            title = "The Attention Economy",
            subtitle = "Mengapa perhatian pemimpin kini menjadi aset paling langka dalam organisasi.",
            page = 18
        ),
        ReaderChapter(
            id = "bab-2",
            label = "Bab 2",
            title = "Mindful Tech Integration",
            subtitle = "Menetapkan batas teknologi yang bertahan tanpa rasa bersalah.",
            page = 62
        ),
        ReaderChapter(
            id = "bab-3",
            label = "Bab 3",
            title = "Deep Work Protocols",
            subtitle = "Langkah praktis untuk masuk ke kondisi fokus mendalam sesuai kebutuhan.",
            page = 104
        ),
        ReaderChapter(
            id = "bab-4",
            label = "Bab 4",
            title = "Cognitive Recovery",
            subtitle = "Memulihkan energi mental setelah penggunaan digital yang intens.",
            page = 146
        ),
        ReaderChapter(
            id = "bab-5",
            label = "Bab 5",
            title = "Membangun Radar Organisasi",
            subtitle = "Mengubah kewaspadaan individu menjadi sistem deteksi dini organisasi.",
            page = 182
        ),
        ReaderChapter(
            id = "penutup",
            label = "Penutup",
            title = "Pemimpin yang Waspada",
            subtitle = "Kebiasaan kecil yang menjaga radar tetap menyala.",
            page = 226
        )
    )

    const val TOTAL_PAGES = 240

    val SAMPLE_PARAGRAPHS = listOf(
        "Setiap hari, seorang eksekutif menerima ratusan sinyal: laporan dashboard, notifikasi pesan, ringkasan rapat, hingga pemberitaan media. Ironisnya, semakin banyak informasi yang tersedia, semakin besar peluang sinyal paling penting justru tenggelam di antara kebisingan.",
        "Fenomena ini bukan soal kurangnya kecerdasan. Riset perilaku menunjukkan bahwa otak manusia secara alami memprioritaskan informasi yang familiar dan mengonfirmasi keyakinan yang sudah ada. Di level kepemimpinan, bias ini diperkuat oleh lingkaran informasi yang semakin sempit dan tekanan untuk memutuskan dengan cepat.",
        "Bab ini mengajak Anda memetakan ke mana perhatian Anda benar-benar mengalir. Dengan memahami pola konsumsi informasi pribadi dan tim, Anda dapat mulai membedakan antara data yang sekadar ramai dan sinyal yang benar-benar menentukan arah organisasi."
    )

    const val SAMPLE_QUOTE =
        "“Pemimpin jarang gagal karena kekurangan data. Mereka gagal karena tidak sempat melihat apa yang sudah ada di depan mata.”"
}
