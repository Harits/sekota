package id.sekota.feature.books.data

import id.sekota.core.model.BookSlug
import id.sekota.core.model.ltext
import id.sekota.feature.books.domain.BookContentItem
import id.sekota.feature.books.domain.BookDetail
import id.sekota.feature.books.domain.BooksPageContent
import id.sekota.feature.books.domain.BooksRepository
import id.sekota.feature.books.domain.SeriesContent
import id.sekota.feature.books.domain.SeriesLink

class StaticBooksRepository : BooksRepository {
    override fun load(): BooksPageContent = BooksCatalog.content
}

object BooksCatalog {

    private const val AUTHOR_ID = "Putu Aan J. · PT Sekota Sinergi Indonesia"
    private val author = ltext(AUTHOR_ID, AUTHOR_ID)
    private val audienceTitle = ltext("Untuk siapa", "Who it's for")
    private val contentsTitle = ltext("Isi buku", "Inside the book")
    private val contact = ltext("Hubungi Kami", "Contact Us")

    private fun item(idTitle: String, enTitle: String, idDetail: String, enDetail: String) =
        BookContentItem(ltext(idTitle, enTitle), ltext(idDetail, enDetail))

    val content = BooksPageContent(
        overline = ltext("Wawasan", "Insights"),
        titlePrefix = ltext("Cara berpikir kami, ", "Our way of thinking, "),
        titleGradient = ltext("dibukukan.", "in print."),
        heroLead = ltext(
            "Buku-buku thought leadership yang ditulis Putu Aan J. dan diterbitkan PT Sekota Sinergi Indonesia, tentang keputusan, kepatuhan, lahan, dan keberlanjutan.",
            "Thought leadership books written by Putu Aan J. and published by PT Sekota Sinergi Indonesia, on decisions, compliance, land, and sustainability. All books are written in Indonesian.",
        ),
        indexLabel = ltext("Indeks buku", "Book index"),
        bookCta = ltext("Diskusikan Gagasan Buku Ini", "Discuss the Ideas in This Book"),
        bookCtaHint = ltext(
            "Bahas relevansinya untuk organisasi Anda bersama tim Sekota.",
            "Discuss its relevance for your organization with the Sekota team.",
        ),
        books = listOf(
            BookDetail(
                slug = BookSlug.ForgettingEnterprise,
                author = author,
                title = "The Forgetting Enterprise",
                subtitle = ltext(
                    "Mengapa Aturan dan Training Tidak Bertahan dan Bagaimana Cara Mengendalikannya",
                    "Why Rules and Training Don't Stick, and How to Control It",
                ),
                quote = ltext(
                    "Pelatihan menyalakan percikan pemahaman. Sistemlah yang menjaga agar percikan itu tidak padam.",
                    "Training sparks understanding. Systems keep that spark from going out.",
                ),
                paragraphs = listOf(
                    ltext(
                        "Berapa banyak aturan di organisasi Anda yang hanya hidup di dokumen SOP?",
                        "How many rules in your organization live only in the SOP document?",
                    ),
                    ltext(
                        "Kita sering berasumsi bahwa orang yang sudah tahu pasti akan melakukan. Kenyataannya, di antara keduanya ada jurang yang diisi tekanan target, kelelahan, dan kebiasaan lama. Hasilnya adalah siklus yang akrab bagi setiap manajer HSSE, mutu, dan kepatuhan: train, forget, retrain, sampai sebuah insiden membuktikannya.",
                        "We often assume that people who know will do. In reality, there is a gap between the two, filled with target pressure, fatigue, and old habits. The result is a cycle every HSSE, quality, and compliance manager knows well: train, forget, retrain, until an incident proves the point.",
                    ),
                    ltext(
                        "The Forgetting Enterprise tidak menyuruh Anda berhenti melatih. Buku ini menempatkan pelatihan sebagai langkah awal, lalu menunjukkan cara merancang ulang lingkungan kerja lewat kendali visual, pembatas fisik, kunci prosedural, umpan balik, dan akuntabilitas.",
                        "The Forgetting Enterprise doesn't tell you to stop training. It treats training as the first step, then shows how to redesign the work environment through visual controls, physical barriers, procedural locks, feedback, and accountability.",
                    ),
                    ltext(
                        "Buku ini juga tidak memberi skor tunggal. Ia memberi peta, yaitu cara menunjuk persis lapisan kontrol mana yang hilang di proses paling kritis Anda.",
                        "Nor does it hand you a single score. It gives you a map: a way to pinpoint exactly which control layer is missing in your most critical processes.",
                    ),
                    ltext(
                        "Ditulis untuk penggerak di lapangan, dan dirancang untuk diteruskan ke meja pimpinan.",
                        "Written for the people who drive change on the ground, and designed to be passed on to leadership.",
                    ),
                ),
                audienceTitle = audienceTitle,
                audience = listOf(
                    ltext("Manajer HSSE, mutu, dan kepatuhan", "HSSE, quality, and compliance managers"),
                    ltext("Pelatihan, keamanan, dan operasional", "Training, security, and operations"),
                    ltext("Direksi sebagai pengambil keputusan", "The board, as decision makers"),
                ),
                contentsTitle = contentsTitle,
                contents = listOf(
                    item(
                        "Lima lapisan kontrol", "Five control layers",
                        "Dirangkai sebagai alat diagnosis.", "Brought together as a diagnostic tool.",
                    ),
                    item(
                        "Peta Kerapuhan Kontrol", "Control Fragility Map",
                        "Menunjuk lapisan yang hilang di proses kritis.", "Pinpoints the missing layer in critical processes.",
                    ),
                    item(
                        "Prinsip desain anti-bypass", "Anti-bypass design principles",
                        "Anggaran kontrol, satu-masuk-satu-keluar, tinjauan pensiun, dan kontrol bertingkat.",
                        "Control budgets, one-in-one-out, retirement reviews, and tiered controls.",
                    ),
                    item(
                        "Untuk pimpinan", "For leadership",
                        "Setiap bab ditutup C-Suite Key Takeaways, dilengkapi Ringkasan untuk Pimpinan.",
                        "Every chapter closes with C-Suite Key Takeaways, plus an Executive Summary.",
                    ),
                ),
            ),
            BookDetail(
                slug = BookSlug.BlindSpotRadar,
                author = author,
                title = "Blind Spot Radar",
                subtitle = ltext(
                    "Mengapa Pemimpin Cerdas Melewatkan Sinyal Besar",
                    "Why Smart Leaders Miss Big Signals",
                ),
                quote = ltext(
                    "Blind spot paling berbahaya bukan yang tidak bisa dilihat, melainkan yang bisa dilihat tetapi tidak mau dilihat.",
                    "The most dangerous blind spot isn't the one you can't see, but the one you can see and choose not to.",
                ),
                paragraphs = listOf(
                    ltext(
                        "Pernah keluar dari rapat dengan suara bulat, lalu beberapa bulan kemudian bertanya: mengapa tidak ada yang melihat itu datang?",
                        "Ever walked out of a unanimous meeting, only to ask a few months later: why did no one see that coming?",
                    ),
                    ltext(
                        "Setiap organisasi membayar Blind Spot Tax, yaitu biaya dari asumsi yang tidak pernah diuji dan sinyal yang tidak pernah sampai ke meja pengambil keputusan. Di Indonesia, pajak ini sering diperbesar oleh hal yang jarang dibicarakan: ewuh pakewuh, rasa sungkan yang membuat suara berbeda tertahan sebelum sempat terdengar.",
                        "Every organization pays a Blind Spot Tax: the cost of assumptions never tested and signals that never reach the decision-makers' table. In Indonesia, this tax is often magnified by something rarely discussed: ewuh pakewuh, the reluctance that holds back dissenting voices before they can be heard.",
                    ),
                    ltext(
                        "Blind Spot Radar tidak berhenti pada daftar bias kognitif. Buku ini membahas apa yang perlu dibangun: cara merancang keputusan, memberi ruang bagi perbedaan pendapat, menguji sistem saat krisis dan di ruang dewan, hingga memimpin bersama AI tanpa kehilangan penilaian manusia.",
                        "Blind Spot Radar goes beyond a list of cognitive biases. It covers what needs to be built: how to design decisions, make room for dissent, test systems in crises and in the boardroom, and lead alongside AI without losing human judgment.",
                    ),
                    ltext(
                        "Tesisnya menantang anggapan umum: pemimpin yang lebih baik bukan yang paling tahu, melainkan yang paling sadar akan batas pengetahuannya, dan membangun sistem untuk menutup batas itu.",
                        "Its thesis challenges a common assumption: better leaders aren't the ones who know the most, but the ones most aware of the limits of their knowledge, and who build systems to close those limits.",
                    ),
                ),
                audienceTitle = audienceTitle,
                audience = listOf(
                    ltext("Direktur dan komisaris", "Directors and commissioners"),
                    ltext(
                        "Manajer menengah yang melihat masalah tetapi sulit membawanya ke atas",
                        "Middle managers who see problems but struggle to raise them",
                    ),
                ),
                contentsTitle = contentsTitle,
                contents = listOf(
                    item(
                        "Part I · Memahami Blind Spot", "Part I · Understanding Blind Spots",
                        "Batas keahlian, intuisi, dan bias kelompok.",
                        "The limits of expertise, intuition, and group bias.",
                    ),
                    item(
                        "Part II · Membangun Sistem", "Part II · Building the System",
                        "Radar Mindset, ketidakpastian konstruktif, dan arsitektur keputusan.",
                        "Radar Mindset, constructive uncertainty, and decision architecture.",
                    ),
                    item(
                        "Part III · Menguji Sistem dalam Kondisi Ekstrem",
                        "Part III · Testing the System Under Extreme Conditions",
                        "Krisis, ruang dewan, tim proyek, dan era digital.",
                        "Crises, the boardroom, project teams, and the digital era.",
                    ),
                    item(
                        "Part IV · Membangun Masa Depan", "Part IV · Building the Future",
                        "Augmented Decision Maker, organisasi adaptif, dan Pemimpin Radar.",
                        "The Augmented Decision Maker, adaptive organizations, and the Radar Leader.",
                    ),
                ),
            ),
            BookDetail(
                slug = BookSlug.ManifestoEkuitasLahan,
                author = author,
                title = "Manifesto Ekuitas Lahan",
                subtitle = ltext(
                    "Manifesto dalam tiga bagian",
                    "A manifesto in three parts (Land Equity Manifesto)",
                ),
                quote = ltext(
                    "Lahan bukan objek yang dikelola. Ia ekosistem yang dinegosiasikan.",
                    "Land is not an object to be managed. It is an ecosystem to be negotiated.",
                ),
                paragraphs = listOf(
                    ltext(
                        "Tiga peta. Satu bidang lahan. Tiga versi kebenaran.",
                        "Three maps. One plot of land. Three versions of the truth.",
                    ),
                    ltext(
                        "Seorang Kepala Dinas menatap peta kadaster, peta konsesi perusahaan, dan peta partisipatif komunitas yang saling bertentangan. Untuk pertama kalinya ia bertanya: bukan bagaimana menyelesaikan sengketa ini, tetapi mengapa sengketa terus diproduksi.",
                        "A head of a regional agency looks at a cadastral map, a corporate concession map, and a community participatory map that contradict one another. For the first time, he asks not how to settle this dispute, but why disputes keep being produced.",
                    ),
                    ltext(
                        "Manifesto Ekuitas Lahan berangkat dari pertanyaan itu. Biaya terbesar dari lahan yang tidak bekerja justru yang jarang dihitung: investasi yang tidak pernah datang, kredit yang tidak bisa diakses, dan konflik yang diwariskan ke generasi berikutnya. Akar masalahnya bukan oknum, melainkan arsitektur.",
                        "Manifesto Ekuitas Lahan starts from that question. The biggest costs of idle land are the ones rarely counted: investment that never arrives, credit that can't be accessed, and conflict passed down to the next generation. The root of the problem isn't individuals, but architecture.",
                    ),
                    ltext(
                        "Buku ini ditulis untuk dua meja yang menghadapi krisis yang sama dari sisi berlawanan: pengambil kebijakan agraria, dan eksekutif korporasi yang sudah skeptis terhadap CSR. Ini bukan kajian untuk diarsipkan, tetapi kerangka dan alat diagnostik untuk dipakai.",
                        "The book is written for two tables facing the same crisis from opposite sides: agrarian policymakers, and corporate executives who have grown skeptical of CSR. It isn't a study to be archived, but a framework and diagnostic tools to be used.",
                    ),
                    ltext(
                        "Pertanyaannya sederhana: apakah cara lama masih layak dipertahankan?",
                        "The question is simple: is the old way still worth keeping?",
                    ),
                ),
                audienceTitle = audienceTitle,
                audience = listOf(
                    ltext(
                        "Kepala daerah, kepala dinas, dan pembuat kebijakan agraria",
                        "Regional leaders, agency heads, and agrarian policymakers",
                    ),
                    ltext(
                        "CEO, Head of ESG, Direktur Sustainability, dan fund manager",
                        "CEOs, Heads of ESG, Sustainability Directors, and fund managers",
                    ),
                    ltext(
                        "Bank Tanah, lembaga keuangan, dan akademisi kebijakan",
                        "Land banks, financial institutions, and policy academics",
                    ),
                ),
                contentsTitle = contentsTitle,
                contents = listOf(
                    item(
                        "Buku I · Mengapa Lahan Kita Tidur", "Book I · Why Our Land Lies Idle",
                        "Diagnosis.", "Diagnosis.",
                    ),
                    item(
                        "Buku II · Arsitektur Perubahan", "Book II · The Architecture of Change",
                        "Kerangka.", "Framework.",
                    ),
                    item(
                        "Buku III · Eksekusi dan Warisan", "Book III · Execution and Legacy",
                        "Implementasi.", "Implementation.",
                    ),
                    item(
                        "Surat untuk Penerus", "A Letter to Those Who Follow",
                        "Penutup.", "Closing.",
                    ),
                ),
            ),
            BookDetail(
                slug = BookSlug.ImpactDrivenCsr,
                author = author,
                title = "Impact-Driven CSR",
                subtitle = ltext(
                    "Dari Kepatuhan ke Dampak Nyata bagi Komunitas",
                    "From Compliance to Real Impact for Communities",
                ),
                quote = ltext(
                    "Kepatuhan adalah titik awal, bukan garis akhir.",
                    "Compliance is the starting point, not the finish line.",
                ),
                paragraphs = listOf(
                    ltext(
                        "Di Indonesia, izin pemerintah membuat perusahaan boleh beroperasi. Izin dari komunitaslah yang membuat operasi itu aman.",
                        "In Indonesia, a government permit means a company may operate. It's the community's consent that makes operating safe.",
                    ),
                    ltext(
                        "Banyak program CSR terlihat rapi di laporan tetapi tidak hidup di lapangan. Laporannya berhenti di jumlah penerima dan dana tersalurkan, padahal direksi, investor, dan komunitas menanyakan perubahan nyata.",
                        "Many CSR programs look tidy in reports but aren't alive on the ground. Reporting stops at the number of beneficiaries and funds disbursed, while the board, investors, and communities are asking about real change.",
                    ),
                    ltext(
                        "Impact-Driven CSR membekali praktisi dengan fondasi dan alat untuk menutup jarak itu: membaca posisi CSR perusahaan dengan jujur, menjaga lisensi sosial, mengukur dampak dari output hingga outcome, memetakan pemangku kepentingan, dan memilih model CSR yang tepat, dari tradisional ke strategis.",
                        "Impact-Driven CSR equips practitioners with the foundations and tools to close that gap: reading the company's CSR position honestly, protecting the social license, measuring impact from output to outcome, mapping stakeholders, and choosing the right CSR model, from traditional to strategic.",
                    ),
                ),
                audienceTitle = audienceTitle,
                audience = listOf(
                    ltext("Manajer dan staf CSR, ComDev, dan TJSL", "CSR, community development, and TJSL managers and staff"),
                ),
                contentsTitle = contentsTitle,
                contents = listOf(
                    item(
                        "Landasan", "Foundations",
                        "Evolusi CSR, regulasi Indonesia, Carroll, dan ISO 26000.",
                        "The evolution of CSR, Indonesian regulation, Carroll, and ISO 26000.",
                    ),
                    item(
                        "Konteks Indonesia", "Indonesian context",
                        "Social License to Operate, Triple Bottom Line, dan Logframe.",
                        "Social License to Operate, Triple Bottom Line, and Logframe.",
                    ),
                    item(
                        "Implementasi", "Implementation",
                        "Pemangku kepentingan, ekosistem mitra dan UMKM, dari model tradisional ke strategis.",
                        "Stakeholders, partner and MSME ecosystems, from traditional to strategic models.",
                    ),
                ),
            ),
            BookDetail(
                slug = BookSlug.AccountableEsg,
                author = author,
                title = "Accountable ESG",
                subtitle = ltext(
                    "Dari Laporan Keberlanjutan ke Kinerja yang Teruji",
                    "From Sustainability Reports to Tested Performance",
                ),
                quote = ltext("Disclosure bukan kinerja.", "Disclosure is not performance."),
                paragraphs = listOf(
                    ltext(
                        "ESG perusahaan Anda sudah dinilai, diminta atau tidak. Oleh investor, lembaga rating, calon mitra, dan semakin sering oleh publik.",
                        "Your company's ESG is already being assessed, whether you asked or not. By investors, rating agencies, prospective partners, and increasingly by the public.",
                    ),
                    ltext(
                        "Kesalahan yang paling umum adalah menyamakan kualitas pengungkapan dengan kualitas kinerja. Keduanya berbeda. Ketika jaraknya melebar, laporan yang paling rapi pun bisa berbalik menjadi tuduhan greenwashing.",
                        "The most common mistake is equating the quality of disclosure with the quality of performance. They are different. When the gap widens, even the neatest report can turn into an accusation of greenwashing.",
                    ),
                    ltext(
                        "Accountable ESG membahas cara menutup jarak itu: menentukan isu yang benar-benar material, menanamkan ESG ke dalam tata kelola dan insentif, membangun data yang siap diverifikasi, dan memilih standar pelaporan yang tepat. Semuanya dibaca lewat kasus perusahaan Indonesia, termasuk kasus yang menunjukkan bahwa penghargaan tidak sama dengan kinerja.",
                        "Accountable ESG covers how to close that gap: identifying the truly material issues, embedding ESG into governance and incentives, building verification-ready data, and choosing the right reporting standards. All of it is read through Indonesian company cases, including ones that show an award is not the same as performance.",
                    ),
                ),
                audienceTitle = audienceTitle,
                audience = listOf(
                    ltext("Tim ESG dan sustainability", "ESG and sustainability teams"),
                    ltext("Investor relations, risiko, dan strategi korporasi", "Investor relations, risk, and corporate strategy"),
                ),
                contentsTitle = contentsTitle,
                contents = listOf(
                    item(
                        "Fondasi", "Foundations",
                        "Dari CSR ke ESG, dan Creating Shared Value.",
                        "From CSR to ESG, and Creating Shared Value.",
                    ),
                    item(
                        "Kerangka", "Framework",
                        "Materialitas, SDG, dan penanaman ESG ke dalam strategi.",
                        "Materiality, the SDGs, and embedding ESG into strategy.",
                    ),
                    item(
                        "Kinerja", "Performance",
                        "KPI, GRI/ISSB, assurance, komunikasi pemangku kepentingan, dan perbaikan berkelanjutan.",
                        "KPIs, GRI/ISSB, assurance, stakeholder communication, and continuous improvement.",
                    ),
                ),
            ),
            BookDetail(
                slug = BookSlug.SustainabilityLeadership,
                author = author,
                title = "Sustainability Leadership",
                subtitle = ltext(
                    "Dari Program Perusahaan ke Transformasi Sistemik",
                    "From Company Programs to Systemic Transformation",
                ),
                quote = ltext(
                    "Tidak ada perusahaan yang bisa berkelanjutan sendirian.",
                    "No company can be sustainable on its own.",
                ),
                paragraphs = listOf(
                    ltext(
                        "Keberlanjutan tidak lagi berada di pinggiran organisasi. Ia sudah masuk ke ruang dewan, ke keputusan investasi, desain produk, dan rantai pasok, dan semakin sering dianggap bagian dari tanggung jawab fidusia.",
                        "Sustainability no longer sits at the edge of the organization. It has entered the boardroom, investment decisions, product design, and supply chains, and is increasingly seen as part of fiduciary duty.",
                    ),
                    ltext(
                        "Tantangan seperti ini membutuhkan pemimpin baru, bukan pelaksana program. Sustainability Leadership membahas cara membangun keselarasan di C-suite, membaca persoalan sebagai sistem, berkolaborasi dengan pesaing pada isu yang tidak bisa diselesaikan sendirian, dan terlibat dalam kebijakan secara etis. Buku ini juga membahas cara memastikan transisi energi tidak meninggalkan pekerja dan komunitas.",
                        "Challenges like these need a new kind of leader, not a program manager. Sustainability Leadership covers how to build alignment in the C-suite, read problems as systems, collaborate with competitors on issues no one can solve alone, and engage in policy ethically. It also covers how to make sure the energy transition doesn't leave workers and communities behind.",
                    ),
                    ltext(
                        "Pertanyaannya bukan lagi “apakah kita sudah hijau”, melainkan “warisan apa yang akan ditinggalkan bisnis kita.”",
                        "The question is no longer “are we green yet?” but “what legacy will our business leave?”",
                    ),
                ),
                audienceTitle = audienceTitle,
                audience = listOf(
                    ltext("CEO, direksi, dan komisaris", "CEOs, directors, and commissioners"),
                    ltext("Chief Sustainability Officer", "Chief Sustainability Officers"),
                ),
                contentsTitle = contentsTitle,
                contents = listOf(
                    item(
                        "Foundations", "Foundations",
                        "Kepemimpinan dan dewan, CSR 2.0, dan systems thinking.",
                        "Leadership and the board, CSR 2.0, and systems thinking.",
                    ),
                    item(
                        "Boundaries", "Boundaries",
                        "Batas planet dan Doughnut, ekonomi sirkular, dan transformasi industri.",
                        "Planetary boundaries and the Doughnut, the circular economy, and industrial transformation.",
                    ),
                    item(
                        "Transformation", "Transformation",
                        "Integrated reporting dan advokasi kebijakan.",
                        "Integrated reporting and policy advocacy.",
                    ),
                    item(
                        "Future", "Future",
                        "Transisi yang adil dan bisnis regeneratif.",
                        "Just transition and regenerative business.",
                    ),
                ),
            ),
        ),
        series = SeriesContent(
            overline = ltext("Sekota Sustainability Series", "Sekota Sustainability Series"),
            title = ltext(
                "Trilogi CSR, ESG, dan Kepemimpinan Keberlanjutan",
                "A trilogy on CSR, ESG, and sustainability leadership",
            ),
            text = ltext(
                "Banyak perusahaan sudah patuh: anggaran terserap, laporan terbit, penghargaan berjajar. Namun satu pertanyaan masih sering membuat ruang rapat hening: apa yang sebenarnya berubah? Trilogi ini mengajak manajer dan pimpinan perusahaan menutup jarak itu dalam tiga tahap: dari kepatuhan ke dampak, dari laporan ke kinerja, dan dari program perusahaan ke transformasi sistemik. Setiap buku bisa dibaca terpisah.",
                "Many companies are already compliant: budgets spent, reports published, awards on the wall. Yet one question still silences the meeting room: what has actually changed? This trilogy invites managers and company leaders to close that gap in three stages: from compliance to impact, from reporting to performance, and from company programs to systemic transformation. Each book can be read on its own.",
            ),
            quote = ltext(
                "Menutup jarak antara yang tampak baik dan yang benar-benar terjadi.",
                "Closing the gap between what looks good and what actually happens.",
            ),
            links = listOf(
                SeriesLink(
                    BookSlug.ImpactDrivenCsr,
                    ltext("Impact-Driven CSR", "Impact-Driven CSR"),
                    ltext(
                        "Buku 1 · kepatuhan → dampak · Skala program · pengelola program",
                        "Book 1 · compliance → impact · Program scale · program managers",
                    ),
                ),
                SeriesLink(
                    BookSlug.AccountableEsg,
                    ltext("Accountable ESG", "Accountable ESG"),
                    ltext(
                        "Buku 2 · laporan → kinerja · Skala organisasi · integrator strategi",
                        "Book 2 · report → performance · Organization scale · strategy integrator",
                    ),
                ),
                SeriesLink(
                    BookSlug.SustainabilityLeadership,
                    ltext("Sustainability Leadership", "Sustainability Leadership"),
                    ltext(
                        "Buku 3 · perusahaan → sistem · Skala sistem · pemimpin transformasi",
                        "Book 3 · company → system · System scale · transformation leaders",
                    ),
                ),
            ),
        ),
        ctaTitle = ltext(
            "Ingin membahas gagasan buku ini untuk organisasi Anda?",
            "Want to discuss these ideas for your organization?",
        ),
        ctaText = ltext(
            "Tinggalkan pesan singkat, tim kami akan menghubungi Anda.",
            "Leave a short message and our team will get in touch.",
        ),
        ctaButton = contact,
    )
}
