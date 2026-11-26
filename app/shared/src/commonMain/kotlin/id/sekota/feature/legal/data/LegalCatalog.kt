package id.sekota.feature.legal.data

import id.sekota.core.model.LText
import id.sekota.core.model.ltext
import id.sekota.feature.legal.domain.LegalBlock
import id.sekota.feature.legal.domain.LegalDoc
import id.sekota.feature.legal.domain.LegalDocType
import id.sekota.feature.legal.domain.LegalLink
import id.sekota.feature.legal.domain.LegalRepository
import id.sekota.feature.legal.domain.LegalSection
import id.sekota.feature.legal.domain.LegalSpan

class StaticLegalRepository : LegalRepository {
    override fun privacy(): LegalDoc = LegalCatalog.privacy
    override fun terms(): LegalDoc = LegalCatalog.terms
}

private fun t(id: String, en: String): LText = ltext(id, en)

private fun span(
    id: String,
    en: String,
    link: LegalLink? = null,
    bold: Boolean = false,
) = LegalSpan(t(id, en), link = link, bold = bold)

private fun p(vararg spans: LegalSpan) = LegalBlock.Paragraph(spans.toList())

private fun ul(vararg items: LegalSpan) = LegalBlock.Bullets(items.map { listOf(it) })

private fun section(id: String, idTitle: String, enTitle: String, vararg blocks: LegalBlock) =
    LegalSection(id = id, title = t(idTitle, enTitle), blocks = blocks.toList())

object LegalCatalog {

    val privacy = LegalDoc(
        type = LegalDocType.Privacy,
        overline = t("Legal", "Legal"),
        title = t("Kebijakan Privasi", "Privacy Policy"),
        lead = t(
            "Kebijakan ini menjelaskan data apa yang kami kumpulkan saat Anda mengunjungi situs Sekota atau menghubungi kami, untuk apa data itu digunakan, dan apa hak Anda atas data tersebut.",
            "This policy explains what data we collect when you visit the Sekota website or contact us, what we use it for, and your rights over that data.",
        ),
        updated = t(
            "Terakhir diperbarui: 25 November 2026",
            "Last updated: 25 November 2026",
        ),
        tocTitle = t("Daftar isi", "Contents"),
        otherLabel = t("Baca juga Syarat & Ketentuan", "Also read the Terms & Conditions"),
        otherType = LegalDocType.Terms,
        sections = listOf(
            section(
                "tentang", "Tentang kebijakan ini", "About this policy",
                p(
                    span(
                        "Kebijakan Privasi ini berlaku untuk situs resmi PT Sekota Sinergi Indonesia (“Sekota” atau “kami”), bagian dari eBdesk Group, termasuk seluruh halaman dalam bahasa Indonesia dan bahasa Inggris.",
                        "This Privacy Policy applies to the official website of PT Sekota Sinergi Indonesia (“Sekota” or “we”), part of eBdesk Group, including all pages in Indonesian and English.",
                    ),
                ),
                p(
                    span(
                        "Kami memproses data pribadi dengan mengacu pada Undang-Undang Nomor 27 Tahun 2022 tentang Pelindungan Data Pribadi dan peraturan lain yang berlaku di Indonesia.",
                        "We process personal data with reference to Indonesian Law No. 27 of 2022 on Personal Data Protection and other applicable Indonesian regulations.",
                    ),
                ),
            ),
            section(
                "data", "Data yang kami kumpulkan", "Data we collect",
                p(
                    span("Data yang Anda berikan. ", "Data you provide. ", bold = true),
                    span(
                        "Saat mengisi formulir Hubungi Kami, Anda memberikan nama, alamat email, topik, dan isi pesan. Jika Anda mengirim email langsung ke kami, kami menerima alamat email dan isi korespondensi tersebut.",
                        "When you fill in the Contact Us form, you provide your name, email address, topic, and message. If you email us directly, we receive your email address and the content of that correspondence.",
                    ),
                ),
                p(
                    span("Data teknis. ", "Technical data. ", bold = true),
                    span(
                        "Seperti situs pada umumnya, server dan penyedia hosting kami dapat mencatat data teknis standar, misalnya alamat IP, jenis peramban, halaman yang dikunjungi, dan waktu akses. Data ini dipakai untuk menjaga keamanan dan kinerja situs.",
                        "Like most websites, our servers and hosting provider may log standard technical data, such as IP address, browser type, pages visited, and time of access. We use this data to keep the site secure and performing well.",
                    ),
                ),
                p(
                    span("Cookie. ", "Cookies. ", bold = true),
                    span(
                        "Saat ini situs tidak menggunakan cookie untuk pelacakan atau iklan. Jika hal ini berubah, kebijakan ini akan kami perbarui terlebih dahulu.",
                        "The site currently does not use cookies for tracking or advertising. If this changes, we will update this policy first.",
                    ),
                ),
            ),
            section(
                "penggunaan", "Untuk apa data digunakan", "How we use data",
                p(span("Kami menggunakan data Anda untuk:", "We use your data to:")),
                ul(
                    span("membalas pertanyaan atau pesan yang Anda kirim;", "respond to questions or messages you send;"),
                    span("menjadwalkan diskusi, konsultasi, atau demo platform yang Anda minta;", "schedule discussions, consultations, or platform demos you request;"),
                    span("menindaklanjuti minat kerja sama, misalnya Program Mitra Awal;", "follow up on partnership interest, such as the Early Partner Program;"),
                    span("menjaga keamanan, mencegah penyalahgunaan, dan meningkatkan kinerja situs.", "keep the site secure, prevent misuse, and improve performance."),
                ),
                p(
                    span(
                        "Kami tidak menjual data pribadi Anda dan tidak menggunakannya untuk iklan pihak ketiga.",
                        "We do not sell your personal data and do not use it for third-party advertising.",
                    ),
                ),
            ),
            section(
                "dasar", "Dasar pemrosesan", "Basis for processing",
                p(
                    span(
                        "Kami memproses data yang Anda kirim melalui formulir atau email berdasarkan persetujuan Anda, yang Anda berikan saat mengirim pesan, serta untuk menindaklanjuti permintaan yang Anda ajukan sebelum adanya kerja sama. Data teknis diproses untuk kepentingan yang sah dalam menjaga keamanan dan operasional situs.",
                        "We process data you send through the form or by email based on your consent, given when you send the message, and to act on requests you make before any engagement. Technical data is processed for our legitimate interest in keeping the site secure and running.",
                    ),
                ),
            ),
            section(
                "berbagi", "Berbagi data", "Sharing data",
                p(span("Data Anda hanya dibagikan sejauh diperlukan, kepada:", "Your data is shared only as far as necessary, with:")),
                ul(
                    span(
                        "perusahaan dalam eBdesk Group, apabila permintaan Anda berkaitan dengan teknologi atau layanan yang disediakan bersama;",
                        "companies within eBdesk Group, where your request relates to technology or services provided jointly;",
                    ),
                    span(
                        "penyedia layanan yang membantu kami mengoperasikan situs dan komunikasi, seperti hosting dan email, yang terikat kewajiban menjaga kerahasiaan;",
                        "service providers that help us run the site and communications, such as hosting and email, who are bound by confidentiality obligations;",
                    ),
                    span("pihak berwenang, apabila diwajibkan oleh hukum.", "authorities, where required by law."),
                ),
            ),
            section(
                "simpan", "Penyimpanan dan keamanan", "Retention and security",
                p(
                    span(
                        "Kami menyimpan data pribadi selama diperlukan untuk tujuan di atas, atau selama diwajibkan oleh peraturan. Setelah itu, data akan dihapus atau dianonimkan.",
                        "We keep personal data for as long as needed for the purposes above, or as long as required by regulation. After that, the data is deleted or anonymized.",
                    ),
                ),
                p(
                    span(
                        "Kami menerapkan langkah teknis dan organisasi yang wajar untuk melindungi data dari akses, perubahan, atau pengungkapan yang tidak sah. Namun, tidak ada metode pengiriman atau penyimpanan data di internet yang sepenuhnya bebas risiko.",
                        "We apply reasonable technical and organizational measures to protect data from unauthorized access, alteration, or disclosure. However, no method of transmitting or storing data over the internet is completely risk-free.",
                    ),
                ),
            ),
            section(
                "hak", "Hak Anda", "Your rights",
                p(span("Sesuai peraturan pelindungan data pribadi, Anda berhak untuk:", "Under personal data protection regulations, you have the right to:")),
                ul(
                    span("mengetahui dan mengakses data pribadi Anda yang kami simpan;", "know about and access the personal data we hold about you;"),
                    span("memperbarui atau memperbaiki data yang tidak akurat;", "update or correct inaccurate data;"),
                    span("meminta penghapusan data;", "request deletion of your data;"),
                    span("menarik persetujuan yang pernah Anda berikan;", "withdraw consent you have given;"),
                    span("mengajukan keberatan atas pemrosesan data tertentu.", "object to certain processing of your data."),
                ),
                p(
                    span("Untuk menggunakan hak tersebut, kirim permintaan ke ", "To exercise these rights, send a request to "),
                    span("info@sekota.id", "info@sekota.id", link = LegalLink.Email),
                    span(
                        ". Kami dapat meminta informasi tambahan untuk memastikan permintaan berasal dari pemilik data.",
                        ". We may ask for additional information to confirm the request comes from the data owner.",
                    ),
                ),
            ),
            section(
                "pihak-ketiga", "Layanan dan tautan pihak ketiga", "Third-party services and links",
                p(
                    span(
                        "Situs ini memuat huruf dari Google Fonts, sehingga peramban Anda terhubung ke server Google saat halaman dibuka. Situs ini juga dapat memuat tautan ke situs lain. Kebijakan privasi pihak ketiga tersebut berlaku untuk layanan mereka, dan kami menyarankan Anda membacanya.",
                        "This site loads fonts from Google Fonts, so your browser connects to Google's servers when a page opens. The site may also contain links to other websites. Those third parties' privacy policies apply to their services, and we encourage you to read them.",
                    ),
                ),
            ),
            section(
                "perubahan", "Perubahan kebijakan", "Changes to this policy",
                p(
                    span(
                        "Kami dapat memperbarui kebijakan ini dari waktu ke waktu. Versi terbaru selalu tersedia di halaman ini, dengan tanggal pembaruan terakhir di bagian atas.",
                        "We may update this policy from time to time. The latest version is always available on this page, with the last updated date shown at the top.",
                    ),
                ),
            ),
            section(
                "kontak", "Hubungi kami", "Contact us",
                p(
                    span(
                        "Untuk pertanyaan tentang kebijakan ini atau data pribadi Anda, hubungi PT Sekota Sinergi Indonesia melalui ",
                        "For questions about this policy or your personal data, contact PT Sekota Sinergi Indonesia at ",
                    ),
                    span("info@sekota.id", "info@sekota.id", link = LegalLink.Email),
                    span(" atau halaman ", " or through the "),
                    span("Hubungi Kami", "Contact Us", link = LegalLink.Contact),
                    span(".", "."),
                ),
            ),
        ),
    )

    val terms = LegalDoc(
        type = LegalDocType.Terms,
        overline = t("Legal", "Legal"),
        title = t("Syarat & Ketentuan", "Terms & Conditions"),
        lead = t(
            "Syarat dan ketentuan ini mengatur penggunaan situs Sekota. Dengan mengakses atau menggunakan situs ini, Anda dianggap telah membaca dan menyetujuinya.",
            "These terms and conditions govern your use of the Sekota website. By accessing or using this site, you are deemed to have read and agreed to them.",
        ),
        updated = t(
            "Terakhir diperbarui: 25 November 2026",
            "Last updated: 25 November 2026",
        ),
        tocTitle = t("Daftar isi", "Contents"),
        otherLabel = t("Baca juga Kebijakan Privasi", "Also read the Privacy Policy"),
        otherType = LegalDocType.Privacy,
        sections = listOf(
            section(
                "penerimaan", "Penerimaan syarat", "Acceptance of terms",
                p(
                    span(
                        "Situs ini dikelola oleh PT Sekota Sinergi Indonesia (“Sekota” atau “kami”), bagian dari eBdesk Group. Jika Anda tidak menyetujui syarat dan ketentuan ini, mohon untuk tidak menggunakan situs ini.",
                        "This site is operated by PT Sekota Sinergi Indonesia (“Sekota” or “we”), part of eBdesk Group. If you do not agree to these terms and conditions, please do not use this site.",
                    ),
                ),
            ),
            section(
                "situs", "Tentang situs", "About the site",
                p(
                    span(
                        "Situs ini berisi informasi umum tentang Sekota, layanan kami, platform Clarivio, Ascendio, Sociara, dan Evidara, serta buku-buku thought leadership yang kami terbitkan.",
                        "This site contains general information about Sekota, our services, the Clarivio, Ascendio, Sociara, and Evidara platforms, and the thought leadership books we publish.",
                    ),
                ),
            ),
            section(
                "informasi", "Informasi bukan nasihat", "Information, not advice",
                p(
                    span(
                        "Konten di situs ini bersifat umum dan disediakan untuk tujuan informasi. Konten tersebut bukan nasihat profesional, hukum, keuangan, atau bisnis untuk situasi tertentu.",
                        "Content on this site is general and provided for information purposes. It is not professional, legal, financial, or business advice for any specific situation.",
                    ),
                ),
                p(
                    span(
                        "Setiap kerja sama, layanan, atau penggunaan platform diatur dalam perjanjian tertulis yang terpisah antara Sekota dan klien.",
                        "Any engagement, service, or platform use is governed by a separate written agreement between Sekota and the client.",
                    ),
                ),
            ),
            section(
                "platform", "Produk dan platform", "Products and platforms",
                p(
                    span(
                        "Deskripsi fitur dan tampilan platform di situs ini dapat berubah seiring pengembangan produk. Data yang terlihat pada tangkapan layar adalah data contoh. Ketersediaan fitur untuk setiap klien mengikuti ruang lingkup yang disepakati dalam perjanjian.",
                        "Feature descriptions and platform screens on this site may change as products develop. Data shown in screenshots is sample data. Feature availability for each client follows the scope agreed in the contract.",
                    ),
                ),
            ),
            section(
                "hki", "Hak kekayaan intelektual", "Intellectual property",
                p(
                    span(
                        "Seluruh konten di situs ini, termasuk teks, desain, grafik, logo, nama Sekota, Clarivio, Ascendio, Sociara, dan Evidara, serta isi dan sampul buku, adalah milik Sekota atau pemiliknya masing-masing dan dilindungi oleh peraturan hak kekayaan intelektual. Nama dan logo eBdesk adalah milik eBdesk Group.",
                        "All content on this site, including text, design, graphics, logos, the names Sekota, Clarivio, Ascendio, Sociara, and Evidara, and book content and covers, belongs to Sekota or their respective owners and is protected by intellectual property laws. The eBdesk name and logo belong to eBdesk Group.",
                    ),
                ),
                p(
                    span(
                        "Anda boleh membaca, mengunduh, atau mencetak konten untuk keperluan pribadi dan non-komersial. Penggunaan lain, termasuk menyalin, mengubah, atau menyebarluaskan untuk tujuan komersial, memerlukan izin tertulis dari kami.",
                        "You may read, download, or print content for personal, non-commercial use. Any other use, including copying, modifying, or distributing for commercial purposes, requires our written permission.",
                    ),
                ),
            ),
            section(
                "larangan", "Penggunaan yang dilarang", "Prohibited use",
                p(span("Saat menggunakan situs ini, Anda tidak boleh:", "When using this site, you must not:")),
                ul(
                    span(
                        "mengganggu, merusak, atau mencoba mendapatkan akses tidak sah ke situs atau sistem kami;",
                        "disrupt, damage, or attempt to gain unauthorized access to the site or our systems;",
                    ),
                    span(
                        "mengirim spam, konten yang melanggar hukum, atau konten yang menyesatkan melalui formulir;",
                        "send spam, unlawful content, or misleading content through the form;",
                    ),
                    span("menyamar sebagai orang atau organisasi lain;", "impersonate another person or organization;"),
                    span("mengambil konten secara otomatis dalam jumlah besar tanpa izin.", "scrape content in bulk without permission."),
                ),
            ),
            section(
                "tautan", "Tautan ke situs lain", "Links to other sites",
                p(
                    span(
                        "Situs ini dapat memuat tautan ke situs pihak ketiga. Kami tidak mengendalikan dan tidak bertanggung jawab atas isi, kebijakan, atau praktik situs-situs tersebut.",
                        "This site may contain links to third-party websites. We do not control and are not responsible for the content, policies, or practices of those sites.",
                    ),
                ),
            ),
            section(
                "tanggung-jawab", "Batasan tanggung jawab", "Limitation of liability",
                p(
                    span(
                        "Kami berupaya menjaga informasi di situs ini tetap akurat dan terbaru, tetapi situs disediakan “sebagaimana adanya” tanpa jaminan apa pun. Sejauh diizinkan hukum, Sekota tidak bertanggung jawab atas kerugian yang timbul dari penggunaan situs ini atau ketergantungan pada informasi di dalamnya.",
                        "We strive to keep the information on this site accurate and up to date, but the site is provided “as is” without any warranty. To the extent permitted by law, Sekota is not liable for any loss arising from use of this site or reliance on its information.",
                    ),
                ),
            ),
            section(
                "privasi", "Privasi", "Privacy",
                p(
                    span(
                        "Penggunaan data pribadi Anda diatur dalam ",
                        "The use of your personal data is governed by our ",
                    ),
                    span("Kebijakan Privasi", "Privacy Policy", link = LegalLink.Privacy),
                    span(" kami.", "."),
                ),
            ),
            section(
                "perubahan", "Perubahan syarat", "Changes to these terms",
                p(
                    span(
                        "Kami dapat mengubah syarat dan ketentuan ini sewaktu-waktu. Versi terbaru berlaku sejak dipublikasikan di halaman ini.",
                        "We may change these terms and conditions at any time. The latest version applies from the moment it is published on this page.",
                    ),
                ),
            ),
            section(
                "hukum", "Hukum yang berlaku", "Governing law",
                p(
                    span(
                        "Syarat dan ketentuan ini tunduk pada hukum Republik Indonesia.",
                        "These terms and conditions are governed by the laws of the Republic of Indonesia.",
                    ),
                ),
            ),
            section(
                "kontak", "Hubungi kami", "Contact us",
                p(
                    span(
                        "Pertanyaan tentang syarat dan ketentuan ini dapat dikirim ke ",
                        "Questions about these terms and conditions can be sent to ",
                    ),
                    span("info@sekota.id", "info@sekota.id", link = LegalLink.Email),
                    span(" atau melalui halaman ", " or through the "),
                    span("Hubungi Kami", "Contact Us", link = LegalLink.Contact),
                    span(".", "."),
                ),
            ),
        ),
    )
}
