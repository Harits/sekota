package id.sekota.feature.product.data

import id.sekota.core.model.IconKey
import id.sekota.core.model.ProductSlug
import id.sekota.core.model.ltext
import id.sekota.feature.product.domain.ProductDetail
import id.sekota.feature.product.domain.ProductFeature
import id.sekota.feature.product.domain.ProductFlowStep
import id.sekota.feature.product.domain.ProductProblem
import id.sekota.feature.product.domain.ProductRepository
import id.sekota.feature.product.domain.ProductScreen

class StaticProductRepository : ProductRepository {
    override fun load(slug: ProductSlug): ProductDetail? = ProductCatalog.all[slug]
}

object ProductCatalog {

    private fun problem(
        number: String,
        idTitle: String,
        enTitle: String,
        idText: String,
        enText: String,
    ) = ProductProblem(number, ltext(idTitle, enTitle), ltext(idText, enText))

    private fun feature(icon: IconKey, idTitle: String, enTitle: String, idText: String, enText: String) =
        ProductFeature(icon, ltext(idTitle, enTitle), ltext(idText, enText))

    private fun step(icon: IconKey, number: String, idTitle: String, enTitle: String, idText: String, enText: String) =
        ProductFlowStep(number, ltext(idTitle, enTitle), ltext(idText, enText), icon)

    private fun screen(idTitle: String, enTitle: String, idText: String, enText: String) =
        ProductScreen(ltext(idTitle, enTitle), ltext(idText, enText))

    private val demo = ltext("Jadwalkan Demo", "Schedule a Demo")
    private val breadcrumb = ltext("Platform", "Platform")
    private val featuresOverline = ltext("Fitur", "Features")
    private val screensOverline = ltext("Tampilan", "Screens")
    private val flowOverline = ltext("Alur kerja", "How it works")
    private val audienceOverline = ltext("Untuk siapa", "Who it's for")
    private val relatedOverline = ltext("Layanan terkait", "Related service")
    private val othersOverline = ltext("Platform lain", "Other platforms")
    private val othersTitle = ltext("Satu ekosistem, empat platform.", "One ecosystem, four platforms.")
    private val screensNote = ltext(
        "Tampilan diambil dari desain produk; data di dalamnya adalah contoh.",
        "Screens are taken from the product design; the data shown is sample data.",
    )
    private val audienceTitle = ltext(
        "Dipakai oleh tim yang perlu keputusan cepat dan terukur.",
        "Built for teams that need fast, measurable decisions.",
    )

    val all: Map<ProductSlug, ProductDetail> = listOf(
        ProductDetail(
            slug = ProductSlug.Clarivio,
            name = "Clarivio",
            serviceName = ltext("Analisis Strategis & Risiko", "Strategic & Risk Intelligence"),
            breadcrumb = breadcrumb,
            headline = ltext(
                "Satu dasbor untuk semua sinyal yang perlu dipantau pimpinan.",
                "One dashboard for every signal leadership needs to watch.",
            ),
            lead = ltext(
                "Clarivio menyatukan data yang perlu dipantau organisasi Anda ke dalam satu dasbor terintegrasi, lalu mengolahnya menjadi peta ancaman, peluang, dan rekomendasi langkah yang bisa langsung dibahas di meja pimpinan.",
                "Clarivio brings the data your organization needs to monitor into one integrated dashboard, then turns it into a map of threats, opportunities, and recommended steps ready for the leadership table.",
            ),
            demoCta = demo,
            featuresCta = ltext("Lihat Fitur", "View Features"),
            problemsOverline = ltext("Mengapa Clarivio", "Why Clarivio"),
            problemsTitle = ltext("Masalah yang dijawab Clarivio.", "The problems Clarivio solves."),
            problems = listOf(
                problem(
                    "01", "Data tersebar di banyak tempat", "Data scattered everywhere",
                    "Sumber yang relevan, dari pemberitaan, media sosial, sampai data internal, dihimpun sesuai kebutuhan dalam satu tampilan.",
                    "Relevant sources, from news and social media to internal data, are brought together in one view as needed.",
                ),
                problem(
                    "02", "Sinyal terlambat terbaca", "Signals read too late",
                    "Setiap isu diberi skor, tingkat urgensi, dan tingkat kepastian sehingga yang mendesak tidak tenggelam.",
                    "Every issue is scored for urgency and certainty, so what matters most never gets buried.",
                ),
                problem(
                    "03", "Temuan sulit ditindaklanjuti", "Findings hard to act on",
                    "Rekomendasi tindakan menerjemahkan temuan menjadi langkah bertahap, lengkap dengan penanggung jawab dan tenggat.",
                    "Actionable insights turn findings into step-by-step actions, complete with owners and deadlines.",
                ),
            ),
            featuresOverline = featuresOverline,
            featuresTitle = ltext("Yang bisa dilakukan dengan Clarivio.", "What you can do with Clarivio."),
            features = listOf(
                feature(
                    IconKey.Globe, "Pemantauan 360°", "360° monitoring",
                    "Semua data yang perlu dipantau dalam satu dasbor yang tampilannya disesuaikan dengan kebutuhan organisasi.",
                    "All the data you need to watch in one dashboard, tailored to your organization.",
                ),
                feature(
                    IconKey.Sparkle, "Analisis narasi", "Narrative intelligence",
                    "Ringkasan narasi yang sedang berkembang per tema, misalnya geopolitik atau regulasi, beserta waktu pembaruan terakhir.",
                    "Summaries of emerging narratives by theme, such as geopolitics or regulation, with the latest update time.",
                ),
                feature(
                    IconKey.Radar, "Peta ancaman & peluang", "Threat & opportunity map",
                    "Setiap isu dilengkapi sumber ancaman, dampak bisnis, tingkat urgensi, kepastian, dan skor 0–100.",
                    "Each issue comes with its source, business impact, urgency, certainty, and a 0–100 score.",
                ),
                feature(
                    IconKey.Link, "Rekomendasi tindakan", "Actionable insights",
                    "Langkah demi langkah dengan tingkat keyakinan, rekam keberhasilan rekomendasi serupa, dan tenggat.",
                    "Step-by-step recommendations with confidence level, track record of similar recommendations, and deadlines.",
                ),
                feature(
                    IconKey.FileText, "Telusur sumber narasi", "Narrative source tracing",
                    "Setiap temuan bisa ditelusuri ke berita dan unggahan yang menjadi dasarnya.",
                    "Trace every finding back to the news and posts behind it.",
                ),
                feature(
                    IconKey.Database, "Data internal sesuai kebutuhan", "Internal data as needed",
                    "Data internal organisasi ditambahkan oleh tim Sekota sebagai kustomisasi, sehingga gambaran yang dipantau benar-benar utuh.",
                    "Your internal data is added by the Sekota team as a customization, so the picture you monitor is complete.",
                ),
            ),
            screensOverline = screensOverline,
            screensTitle = ltext("Lihat Clarivio lebih dekat.", "Take a closer look at Clarivio."),
            screensNote = screensNote,
            screens = listOf(
                screen(
                    "Dasbor utama", "Main dashboard",
                    "Ringkasan peluang, ancaman, temuan, dan narasi terbaru dalam satu layar.",
                    "Opportunities, threats, insights, and the latest narratives on one screen.",
                ),
                screen(
                    "Detail ancaman", "Threat detail",
                    "Skor ancaman beserta daftar narasi yang menjadi sumbernya.",
                    "Threat score with the list of narratives behind it.",
                ),
            ),
            flowOverline = flowOverline,
            flowTitle = ltext("Empat langkah memakai Clarivio.", "Four steps to using Clarivio."),
            flow = listOf(
                step(IconKey.Database, "01", "Hubungkan data", "Connect data", "Tentukan sumber yang perlu dipantau bersama tim Sekota.", "Decide which sources to monitor with the Sekota team."),
                step(IconKey.Radar, "02", "Pantau sinyal", "Monitor signals", "Ancaman dan peluang terpetakan dan diberi skor secara berkala.", "Threats and opportunities are mapped and scored regularly."),
                step(IconKey.Link, "03", "Terima rekomendasi", "Get recommendations", "Temuan diterjemahkan menjadi langkah yang bisa dijalankan.", "Findings become steps you can act on."),
                step(IconKey.Target, "04", "Putuskan & evaluasi", "Decide & review", "Pimpinan memutuskan, hasilnya dipantau kembali di dasbor.", "Leadership decides, and results are tracked back in the dashboard."),
            ),
            audienceOverline = audienceOverline,
            audienceTitle = audienceTitle,
            audience = listOf(
                ltext("Pimpinan & direksi", "Leadership & board"),
                ltext("Sekretaris perusahaan & hubungan korporat", "Corporate secretary & corporate affairs"),
                ltext("Manajemen risiko", "Risk management"),
                ltext("Hubungan pemerintah & eksternal", "Government & external relations"),
            ),
            relatedOverline = relatedOverline,
            relatedTitle = ltext("Analisis Strategis & Risiko", "Strategic & Risk Intelligence"),
            relatedText = ltext(
                "Clarivio adalah alat utama layanan Analisis Strategis & Risiko dari Sekota. Tim kami mendampingi mulai dari pemetaan kebutuhan sampai platform dipakai sehari-hari.",
                "Clarivio is the core tool of Sekota's Strategic & Risk Intelligence service. Our team supports you from mapping your needs to using the platform every day.",
            ),
            otherPlatformsOverline = othersOverline,
            otherPlatformsTitle = othersTitle,
            ctaTitle = ltext(
                "Diskusikan kebutuhan Clarivio untuk organisasi Anda.",
                "Discuss what Clarivio can do for your organization.",
            ),
            ctaText = ltext(
                "Tinggalkan pesan singkat, tim kami akan menghubungi Anda untuk menjadwalkan demo.",
                "Leave a short message and our team will get in touch to schedule a demo.",
            ),
            ctaButton = demo,
        ),
        ProductDetail(
            slug = ProductSlug.Ascendio,
            name = "Ascendio",
            serviceName = ltext("Kesiapan & Kompetensi SDM", "Workforce Readiness & Competence"),
            breadcrumb = breadcrumb,
            headline = ltext(
                "Kompetensi tetap hidup setelah pelatihan selesai.",
                "Competence that stays alive after training ends.",
            ),
            lead = ltext(
                "Ascendio memantau dampak pelatihan lewat asesmen berkala, mingguan atau bulanan, sehingga tingkat pemahaman setiap personel terlihat di satu dasbor, lengkap dengan rekomendasi perbaikan.",
                "Ascendio tracks the impact of training through regular weekly or monthly assessments, so every person's level of understanding shows up in one dashboard, with recommendations for improvement.",
            ),
            demoCta = demo,
            featuresCta = ltext("Lihat Fitur", "View Features"),
            problemsOverline = ltext("Mengapa Ascendio", "Why Ascendio"),
            problemsTitle = ltext("Masalah yang dijawab Ascendio.", "The problems Ascendio solves."),
            problems = listOf(
                problem(
                    "01", "Dampak pelatihan jarang dipantau", "Training impact is rarely tracked",
                    "Setelah kelas selesai, asesmen berkala menjaga materi tetap diingat dan dijalankan.",
                    "After the class ends, regular assessments keep the material remembered and practiced.",
                ),
                problem(
                    "02", "Kesiapan tim sulit diukur", "Team readiness is hard to measure",
                    "Skor per personel, per topik, dan per indikator terkumpul otomatis di satu dasbor.",
                    "Scores per person, topic, and indicator are collected automatically in one dashboard.",
                ),
                problem(
                    "03", "Tindak lanjut tidak terarah", "Follow-up lacks direction",
                    "Rekomendasi personal menunjukkan area yang tertinggal dan langkah perbaikannya.",
                    "Personal recommendations show lagging areas and how to improve them.",
                ),
            ),
            featuresOverline = featuresOverline,
            featuresTitle = ltext("Yang bisa dilakukan dengan Ascendio.", "What you can do with Ascendio."),
            features = listOf(
                feature(
                    IconKey.Clipboard, "Kuis & jadwal berkala", "Quizzes & schedules",
                    "Susun asesmen dari bank soal atau langsung, lalu jadwalkan secara mingguan atau bulanan.",
                    "Build assessments from a question bank or directly, then schedule them weekly or monthly.",
                ),
                feature(
                    IconKey.BarChart, "Dasbor kesiapan", "Readiness dashboard",
                    "Skor rata-rata, waktu pengerjaan, tingkat partisipasi, dan tren skor per sesi.",
                    "Average score, completion time, participation rate, and score trend per session.",
                ),
                feature(
                    IconKey.Target, "Indikator capaian", "Lagging & leading indicators",
                    "Skor dan tingkat kelulusan per indikator untuk melihat area yang perlu diperkuat.",
                    "Scores and pass rates per indicator to see where to strengthen.",
                ),
                feature(
                    IconKey.Users, "Rekomendasi personal", "Personal recommendations",
                    "Setiap personel melihat indikator yang tertinggal beserta langkah perbaikan yang disarankan.",
                    "Each person sees lagging indicators and the suggested steps to improve.",
                ),
                feature(
                    IconKey.Folder, "Manajemen dokumen", "Document management",
                    "Materi pelatihan tersimpan rapi dan mudah diakses peserta.",
                    "Training materials stay organized and easy for participants to access.",
                ),
                feature(
                    IconKey.Trend, "Manajemen pengguna & topik", "User & topic management",
                    "Atur peserta, jabatan, dan topik pelatihan sesuai struktur organisasi.",
                    "Manage participants, roles, and training topics to match your organization.",
                ),
            ),
            screensOverline = screensOverline,
            screensTitle = ltext("Lihat Ascendio lebih dekat.", "Take a closer look at Ascendio."),
            screensNote = screensNote,
            screens = listOf(
                screen(
                    "Dasbor admin", "Admin dashboard",
                    "Kesiapan tim per jabatan, skor per indikator, dan partisipasi kuis.",
                    "Team readiness by role, scores per indicator, and quiz participation.",
                ),
                screen(
                    "Dasbor personel", "Participant dashboard",
                    "Progres sesi, tren skor, dan rekomendasi personal untuk setiap peserta.",
                    "Session progress, score trend, and personal recommendations.",
                ),
            ),
            flowOverline = flowOverline,
            flowTitle = ltext("Empat langkah memakai Ascendio.", "Four steps to using Ascendio."),
            flow = listOf(
                step(IconKey.Target, "01", "Tetapkan topik", "Set topics", "Pilih topik dan indikator kompetensi yang ingin dijaga.", "Choose the topics and competence indicators to maintain."),
                step(IconKey.Clipboard, "02", "Jadwalkan asesmen", "Schedule assessments", "Kuis berkala dikirim ke peserta sesuai jadwal.", "Regular quizzes go out to participants on schedule."),
                step(IconKey.BarChart, "03", "Pantau kesiapan", "Track readiness", "Skor dan tren terkumpul otomatis di dasbor.", "Scores and trends are collected automatically in the dashboard."),
                step(IconKey.Users, "04", "Tindak lanjuti", "Follow up", "Rekomendasi diarahkan ke personel dan area yang tertinggal.", "Recommendations target the people and areas that lag behind."),
            ),
            audienceOverline = audienceOverline,
            audienceTitle = audienceTitle,
            audience = listOf(
                ltext("SDM & pengembangan pembelajaran", "HR & learning development"),
                ltext("Badan usaha jasa pengamanan", "Security service companies"),
                ltext("Tim HSSE", "HSSE teams"),
                ltext("Penyelenggara pelatihan", "Training providers"),
            ),
            relatedOverline = relatedOverline,
            relatedTitle = ltext("Kesiapan & Kompetensi SDM", "Workforce Readiness & Competence"),
            relatedText = ltext(
                "Ascendio adalah alat utama layanan Kesiapan & Kompetensi SDM dari Sekota. Tim kami mendampingi mulai dari pemetaan kebutuhan sampai platform dipakai sehari-hari.",
                "Ascendio is the core tool of Sekota's Workforce Readiness & Competence service. Our team supports you from mapping your needs to using the platform every day.",
            ),
            otherPlatformsOverline = othersOverline,
            otherPlatformsTitle = othersTitle,
            ctaTitle = ltext(
                "Diskusikan kebutuhan Ascendio untuk organisasi Anda.",
                "Discuss what Ascendio can do for your organization.",
            ),
            ctaText = ltext(
                "Tinggalkan pesan singkat, tim kami akan menghubungi Anda untuk menjadwalkan demo.",
                "Leave a short message and our team will get in touch to schedule a demo.",
            ),
            ctaButton = demo,
        ),
        ProductDetail(
            slug = ProductSlug.Sociara,
            name = "Sociara",
            serviceName = ltext("Dampak Sosial & CSR", "Social Impact & CSR"),
            breadcrumb = breadcrumb,
            headline = ltext(
                "Kelola program CSR dari rencana sampai dampak yang terbukti.",
                "Manage CSR programs from plan to proven impact.",
            ),
            lead = ltext(
                "Sociara mengelola siklus program CSR, dari perencanaan, pelaksanaan, hingga pengukuran dampak, sekaligus memantau penerimaan sosial (social license to operate) di setiap wilayah operasi.",
                "Sociara manages the CSR program cycle, from planning and execution to impact measurement, while tracking your social license to operate (SLO) in every operating region.",
            ),
            demoCta = demo,
            featuresCta = ltext("Lihat Fitur", "View Features"),
            problemsOverline = ltext("Mengapa Sociara", "Why Sociara"),
            problemsTitle = ltext("Masalah yang dijawab Sociara.", "The problems Sociara solves."),
            problems = listOf(
                problem(
                    "01", "Program sulit dibuktikan dampaknya", "Program impact is hard to prove",
                    "KPI, keluaran, dan tren dampak tercatat per program sehingga hasilnya bisa dipertanggungjawabkan.",
                    "KPIs, outputs, and impact trends are recorded per program, so results are accountable.",
                ),
                problem(
                    "02", "Penerimaan sosial tidak terukur", "Social acceptance isn't measured",
                    "Skor legitimasi, kredibilitas, dan kepercayaan dipantau per wilayah operasi.",
                    "Legitimacy, credibility, and trust scores are tracked per operating region.",
                ),
                problem(
                    "03", "Perencanaan tidak selaras", "Planning isn't aligned",
                    "Setiap program dipetakan ke SDGs dan RPJMD sejak tahap perancangan.",
                    "Every program is mapped to the SDGs and regional development plans (RPJMD) from the design stage.",
                ),
            ),
            featuresOverline = featuresOverline,
            featuresTitle = ltext("Yang bisa dilakukan dengan Sociara.", "What you can do with Sociara."),
            features = listOf(
                feature(
                    IconKey.Globe, "Pemantauan penerimaan sosial", "SLO monitoring",
                    "Skor legitimasi, kredibilitas, dan kepercayaan per wilayah beserta trennya.",
                    "Legitimacy, credibility, and trust scores by region, with their trends.",
                ),
                feature(
                    IconKey.Trend, "Penyelarasan SDGs & RPJMD", "SDGs & RPJMD alignment",
                    "Lihat seberapa banyak program yang selaras dengan SDGs dan prioritas RPJMD daerah.",
                    "See how many programs align with the SDGs and regional RPJMD priorities.",
                ),
                feature(
                    IconKey.Clipboard, "Panduan perancangan program", "Program design wizard",
                    "Tujuh langkah terpandu: kebutuhan, tujuan & KPI, SDGs & RPJMD, pemangku kepentingan, anggaran, dampak sosial, lalu ajukan.",
                    "Seven guided steps: needs, objectives & KPIs, SDGs & RPJMD, stakeholders, budget, SLO impact, then submit.",
                ),
                feature(
                    IconKey.Sparkle, "Templat & saran AI", "Templates & AI suggestions",
                    "Mulai dari templat program siap pakai atau minta rekomendasi program dari AI.",
                    "Start from ready-made program templates or ask AI for program recommendations.",
                ),
                feature(
                    IconKey.Check, "Tinjauan manajemen", "Management review",
                    "Program yang diajukan ditinjau manajemen dengan panel ringkasan sebelum disetujui.",
                    "Submitted programs are reviewed by management with a summary panel before approval.",
                ),
                feature(
                    IconKey.Message, "Pelaksanaan & umpan balik", "Execution & feedback",
                    "Log aktivitas, isu & risiko, umpan balik komunitas, dan dokumen dalam satu tempat.",
                    "Activity logs, issues & risks, community feedback, and documents in one place.",
                ),
            ),
            screensOverline = screensOverline,
            screensTitle = ltext("Lihat Sociara lebih dekat.", "Take a closer look at Sociara."),
            screensNote = screensNote,
            screens = listOf(
                screen(
                    "Ringkasan CSR & penerimaan sosial", "CSR & SLO overview",
                    "Ringkasan portofolio CSR, pilar penerimaan sosial, dan keselarasan SDGs & RPJMD.",
                    "CSR portfolio, SLO pillars, and SDGs & RPJMD alignment at a glance.",
                ),
                screen(
                    "Portofolio program", "Program portfolio",
                    "Progres, anggaran, dan penerima manfaat setiap program.",
                    "Progress, budget, and beneficiaries for every program.",
                ),
                screen(
                    "Panduan perancangan program", "Program design wizard",
                    "Perancangan program terpandu dalam tujuh langkah.",
                    "Guided program design in seven steps.",
                ),
            ),
            flowOverline = flowOverline,
            flowTitle = ltext("Empat langkah memakai Sociara.", "Four steps to using Sociara."),
            flow = listOf(
                step(IconKey.Clipboard, "01", "Rancang", "Design", "Susun program dengan panduan, templat, atau saran AI.", "Build programs with the wizard, templates, or AI suggestions."),
                step(IconKey.Check, "02", "Setujui", "Approve", "Manajemen meninjau dan menyetujui program.", "Management reviews and approves the program."),
                step(IconKey.Message, "03", "Jalankan & catat", "Run & record", "Aktivitas, isu, dan umpan balik komunitas tercatat.", "Activities, issues, and community feedback are recorded."),
                step(IconKey.BarChart, "04", "Ukur dampak", "Measure impact", "KPI, dampak, dan skor penerimaan sosial dipantau berkala.", "KPIs, impact, and SLO scores are tracked regularly."),
            ),
            audienceOverline = audienceOverline,
            audienceTitle = audienceTitle,
            audience = listOf(
                ltext("Tim CSR & TJSL", "CSR teams"),
                ltext("Pemberdayaan masyarakat", "Community development"),
                ltext("Hubungan eksternal", "External relations"),
                ltext("BUMN & grup korporasi", "State-owned enterprises & corporate groups"),
            ),
            relatedOverline = relatedOverline,
            relatedTitle = ltext("Dampak Sosial & CSR", "Social Impact & CSR"),
            relatedText = ltext(
                "Sociara adalah alat utama layanan Dampak Sosial & CSR dari Sekota. Tim kami mendampingi mulai dari pemetaan kebutuhan sampai platform dipakai sehari-hari.",
                "Sociara is the core tool of Sekota's Social Impact & CSR service. Our team supports you from mapping your needs to using the platform every day.",
            ),
            otherPlatformsOverline = othersOverline,
            otherPlatformsTitle = othersTitle,
            ctaTitle = ltext(
                "Diskusikan kebutuhan Sociara untuk organisasi Anda.",
                "Discuss what Sociara can do for your organization.",
            ),
            ctaText = ltext(
                "Tinggalkan pesan singkat, tim kami akan menghubungi Anda untuk menjadwalkan demo.",
                "Leave a short message and our team will get in touch to schedule a demo.",
            ),
            ctaButton = demo,
        ),
        ProductDetail(
            slug = ProductSlug.Evidara,
            name = "Evidara",
            serviceName = ltext("Kinerja ESG", "ESG Performance"),
            breadcrumb = breadcrumb,
            headline = ltext(
                "Ukur kesiapan ESG, temukan celahnya, dan buktikan kemajuannya.",
                "Measure ESG readiness, find the gaps, and prove your progress.",
            ),
            lead = ltext(
                "Evidara membantu organisasi menilai kesiapan ESG, memantau kinerja HSSE, dan membandingkannya dengan perusahaan sejenis yang lebih dulu menjalankan ESG.",
                "Evidara helps organizations assess ESG readiness, monitor HSSE performance, and compare it with peer companies that started their ESG journey earlier.",
            ),
            demoCta = demo,
            featuresCta = ltext("Lihat Fitur", "View Features"),
            problemsOverline = ltext("Mengapa Evidara", "Why Evidara"),
            problemsTitle = ltext("Masalah yang dijawab Evidara.", "The problems Evidara solves."),
            problems = listOf(
                problem(
                    "01", "Kesiapan ESG belum jelas", "ESG readiness is unclear",
                    "Asesmen per standar menunjukkan posisi organisasi saat ini, lengkap dengan bukti pendukung.",
                    "Standard-by-standard assessments show where you stand today, with supporting evidence.",
                ),
                problem(
                    "02", "Celah tidak ditindaklanjuti", "Gaps don't get closed",
                    "Analisis celah terhubung langsung ke pelacakan tindakan korektif dan preventif.",
                    "Gap analysis links directly to corrective and preventive action tracking.",
                ),
                problem(
                    "03", "Tidak ada pembanding", "No point of comparison",
                    "Kinerja dibandingkan dengan perusahaan sejenis di sektor yang sama.",
                    "Performance is compared with peer companies in the same sector.",
                ),
            ),
            featuresOverline = featuresOverline,
            featuresTitle = ltext("Yang bisa dilakukan dengan Evidara.", "What you can do with Evidara."),
            features = listOf(
                feature(
                    IconKey.Shield, "Asesmen kesiapan ESG", "ESG readiness assessment",
                    "Asesmen per pertanyaan untuk standar seperti ISO 14001, ISO 45001, dan TCFD, dengan unggah bukti dan saran AI.",
                    "Question-by-question assessments for standards such as ISO 14001, ISO 45001, and TCFD, with evidence upload and AI suggestions.",
                ),
                feature(
                    IconKey.Search, "Analisis celah", "Gap analysis",
                    "Lihat tingkat kepatuhan penuh, parsial, dan celah yang perlu ditutup per kategori.",
                    "See full compliance, partial compliance, and gaps to close by category.",
                ),
                feature(
                    IconKey.Activity, "Pemantauan HSSE", "HSSE monitor",
                    "Masukkan data operasional secara manual, lewat Excel, atau API, lalu pantau per kategori.",
                    "Enter operational data manually, via Excel, or by API, and monitor it by category.",
                ),
                feature(
                    IconKey.BarChart, "Pembandingan kinerja", "Benchmarking",
                    "Bandingkan emisi, keselamatan, air, dan limbah dengan perusahaan sejenis.",
                    "Compare emissions, safety, water, and waste with peer companies.",
                ),
                feature(
                    IconKey.Check, "Pelacakan tindakan (CAPA)", "Action tracking (CAPA)",
                    "Catat dan pantau tindakan korektif dan preventif hingga selesai.",
                    "Record and track corrective and preventive actions to completion.",
                ),
                feature(
                    IconKey.FileText, "Kepatuhan & laporan", "Compliance & reports",
                    "Status kepatuhan, tenggat regulasi, serta laporan yang bisa dibuat dan dijadwalkan otomatis.",
                    "Compliance status, regulatory deadlines, and reports you can generate and schedule automatically.",
                ),
            ),
            screensOverline = screensOverline,
            screensTitle = ltext("Lihat Evidara lebih dekat.", "Take a closer look at Evidara."),
            screensNote = screensNote,
            screens = listOf(
                screen(
                    "Dasbor eksekutif", "Executive dashboard",
                    "Skor kesiapan ESG, keselamatan, jejak karbon, dan peringatan prediktif.",
                    "ESG readiness score, safety, carbon footprint, and predictive alerts.",
                ),
                screen(
                    "Asesmen kesiapan ESG", "ESG readiness assessment",
                    "Asesmen per klausul dengan bukti pendukung dan saran AI.",
                    "Clause-by-clause assessment with supporting evidence and AI suggestions.",
                ),
            ),
            flowOverline = flowOverline,
            flowTitle = ltext("Empat langkah memakai Evidara.", "Four steps to using Evidara."),
            flow = listOf(
                step(IconKey.Shield, "01", "Asesmen", "Assess", "Nilai kesiapan per standar yang relevan.", "Assess readiness against the relevant standards."),
                step(IconKey.Search, "02", "Temukan celah", "Find gaps", "Analisis celah menunjukkan prioritas perbaikan.", "Gap analysis shows improvement priorities."),
                step(IconKey.Check, "03", "Tindak lanjuti", "Act", "Celah diubah menjadi tindakan dan dipantau.", "Gaps become tracked actions."),
                step(IconKey.BarChart, "04", "Bandingkan & laporkan", "Compare & report", "Kemajuan dibandingkan dengan perusahaan sejenis dan dilaporkan.", "Progress is compared with peers and reported."),
            ),
            audienceOverline = audienceOverline,
            audienceTitle = audienceTitle,
            audience = listOf(
                ltext("Tim ESG & keberlanjutan", "ESG & sustainability teams"),
                ltext("HSSE", "HSSE"),
                ltext("Kepatuhan", "Compliance"),
                ltext("Energi, migas & pertambangan", "Energy, oil & gas, and mining"),
            ),
            relatedOverline = relatedOverline,
            relatedTitle = ltext("Kinerja ESG", "ESG Performance"),
            relatedText = ltext(
                "Evidara adalah alat utama layanan Kinerja ESG dari Sekota. Tim kami mendampingi mulai dari pemetaan kebutuhan sampai platform dipakai sehari-hari.",
                "Evidara is the core tool of Sekota's ESG Performance service. Our team supports you from mapping your needs to using the platform every day.",
            ),
            otherPlatformsOverline = othersOverline,
            otherPlatformsTitle = othersTitle,
            ctaTitle = ltext(
                "Diskusikan kebutuhan Evidara untuk organisasi Anda.",
                "Discuss what Evidara can do for your organization.",
            ),
            ctaText = ltext(
                "Tinggalkan pesan singkat, tim kami akan menghubungi Anda untuk menjadwalkan demo.",
                "Leave a short message and our team will get in touch to schedule a demo.",
            ),
            ctaButton = demo,
        ),
    ).associateBy { it.slug }
}
