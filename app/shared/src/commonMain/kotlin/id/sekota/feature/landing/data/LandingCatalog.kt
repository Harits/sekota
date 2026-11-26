package id.sekota.feature.landing.data

import id.sekota.core.content.SiteContent
import id.sekota.core.model.BookSlug
import id.sekota.core.model.IconKey
import id.sekota.core.model.ProductSlug
import id.sekota.core.model.ltext
import id.sekota.feature.landing.domain.AboutContent
import id.sekota.feature.landing.domain.BookCard
import id.sekota.feature.landing.domain.BooksSectionContent
import id.sekota.feature.landing.domain.FaqContent
import id.sekota.feature.landing.domain.FaqItem
import id.sekota.feature.landing.domain.HeroContent
import id.sekota.feature.landing.domain.LandingContent
import id.sekota.feature.landing.domain.LandingRepository
import id.sekota.feature.landing.domain.PartnerContent
import id.sekota.feature.landing.domain.PlatformCard
import id.sekota.feature.landing.domain.PlatformsContent
import id.sekota.feature.landing.domain.ProcessContent
import id.sekota.feature.landing.domain.ProcessStep
import id.sekota.feature.landing.domain.ReasonItem
import id.sekota.feature.landing.domain.SectorItem
import id.sekota.feature.landing.domain.ServiceItem
import id.sekota.feature.landing.domain.ServicesContent
import id.sekota.feature.landing.domain.SuiteRow
import id.sekota.feature.landing.domain.TagStyle

/**
 * In-memory content source. Keeping copy in one object means the landing page is
 * fully testable and works offline on every target; swapping it for a CMS later
 * only means writing another [LandingRepository].
 */
class StaticLandingRepository : LandingRepository {
    override fun load(): LandingContent = LandingCatalog.content
}

object LandingCatalog {

    val content = LandingContent(
        navItems = SiteContent.navItems,
        hero = HeroContent(
            chip = ltext("Bagian dari eBdesk Group", "Part of eBdesk Group"),
            titleLine1 = ltext("Data jadi keputusan.", "Data into decisions."),
            titleLine2 = ltext("Keputusan jadi dampak.", "Decisions into impact."),
            lead = ltext(
                "Sekota adalah mitra analisis yang membantu organisasi membaca risiko, menyiapkan tim, dan membuktikan dampak, lalu tumbuh bersama Anda.",
                "Sekota is an intelligence partner that helps organizations read risk, prepare their people, and prove their impact, and grows together with you.",
            ),
            primaryCta = ltext("Lihat Layanan", "View Services"),
            secondaryCta = SiteContent.contactUs,
            proofPlatforms = ltext("Platform AI", "AI platforms"),
            proofBooks = ltext("Buku thought leadership", "Thought leadership books"),
            suiteTitle = ltext("Sekota Intelligence Suite", "Sekota Intelligence Suite"),
            suiteSubtitle = ltext("Empat platform untuk empat jarak", "Four platforms, four gaps to close"),
            suiteCount = ltext("4 platform AI", "4 AI platforms"),
            suiteRows = listOf(
                SuiteRow(
                    slug = ProductSlug.Clarivio,
                    serviceName = ltext("Analisis Strategis & Risiko", "Strategic & Risk Intelligence"),
                    description = ltext(
                        "Analisis ancaman dan peluang berbasis AI",
                        "AI-driven threat and opportunity intelligence",
                    ),
                    tag = ltext("Sinyal → keputusan", "Signal → decision"),
                    tagStyle = TagStyle.Neutral,
                ),
                SuiteRow(
                    slug = ProductSlug.Ascendio,
                    serviceName = ltext("Kesiapan & Kompetensi SDM", "Workforce Readiness & Competence"),
                    description = ltext(
                        "Menjaga kompetensi tetap hidup setelah pelatihan",
                        "Keeping competence alive after training",
                    ),
                    tag = ltext("Tahu → melakukan", "Knowing → doing"),
                    tagStyle = TagStyle.Green,
                ),
                SuiteRow(
                    slug = ProductSlug.Sociara,
                    serviceName = ltext("Dampak Sosial & CSR", "Social Impact & CSR"),
                    description = ltext(
                        "CSR dan pemberdayaan masyarakat hingga dampak terbukti",
                        "CSR and community empowerment through to proven impact",
                    ),
                    tag = ltext("Program → dampak", "Program → impact"),
                    tagStyle = TagStyle.Green,
                ),
                SuiteRow(
                    slug = ProductSlug.Evidara,
                    serviceName = ltext("Kinerja ESG", "ESG Performance"),
                    description = ltext(
                        "Kesiapan ESG: skor, celah, dan peta jalan",
                        "ESG readiness: scores, gaps, and a roadmap",
                    ),
                    tag = ltext("Laporan → kinerja", "Report → performance"),
                    tagStyle = TagStyle.Cyan,
                ),
            ),
            illustrationLabel = ltext(
                "Ilustrasi: portofolio platform Sekota",
                "Illustration: the Sekota platform portfolio",
            ),
        ),
        sectorsTitle = ltext("Sektor yang kami layani", "Sectors we serve"),
        sectors = listOf(
            SectorItem(IconKey.SectorEnergy, ltext("Energi, Migas & Pertambangan", "Energy, Oil & Gas, and Mining")),
            SectorItem(IconKey.SectorSecurity, ltext("Keamanan & Jasa Pengamanan", "Security Services")),
            SectorItem(IconKey.SectorCorporate, ltext("BUMN & Grup Korporasi", "SOEs & Corporate Groups")),
            SectorItem(IconKey.SectorGovernment, ltext("Pemerintah Daerah & K/L", "Regional Governments & Ministries")),
            SectorItem(IconKey.SectorAgri, ltext("Perkebunan & Agribisnis", "Plantations & Agribusiness")),
            SectorItem(IconKey.SectorBanking, ltext("Perbankan & Keuangan", "Banking & Finance")),
        ),
        about = AboutContent(
            overline = ltext("Tentang Sekota", "About Sekota"),
            title = ltext(
                "Mitra analisis yang tumbuh bersama Anda.",
                "An intelligence partner that grows with you.",
            ),
            lead = ltext(
                "Unit bisnis eBdesk Group yang membantu organisasi di Indonesia memakai data untuk mengambil keputusan.",
                "A business unit of eBdesk Group that helps organizations in Indonesia use data to make decisions.",
            ),
            reasons = listOf(
                ReasonItem(
                    icon = IconKey.BookOpen,
                    title = ltext("Gagasan yang dibukukan", "Ideas in print"),
                    text = ltext(
                        "6 buku thought leadership berbahasa Indonesia menjadi landasan cara kami berpikir dan bekerja.",
                        "Six Indonesian-language thought leadership books shape how we think and work.",
                    ),
                ),
                ReasonItem(
                    icon = IconKey.Cpu,
                    title = ltext("Teknologi eBdesk", "eBdesk technology"),
                    text = ltext(
                        "Big data dan AI di atas infrastruktur eBdesk bersertifikasi ISO 27001.",
                        "Big data and AI running on eBdesk's ISO 27001-certified infrastructure.",
                    ),
                ),
                ReasonItem(
                    icon = IconKey.Trend,
                    title = ltext("Tumbuh bersama", "Growing together"),
                    text = ltext(
                        "Target disepakati bersama, tim Anda ikut mandiri.",
                        "Targets are agreed together, and your team becomes self-reliant.",
                    ),
                ),
            ),
        ),
        services = ServicesContent(
            overline = ltext("Layanan", "Services"),
            title = ltext(
                "Empat layanan, satu tujuan: keputusan yang lebih tepat.",
                "Four services, one goal: better decisions.",
            ),
            items = listOf(
                ServiceItem(
                    number = "01",
                    title = ltext("Analisis Strategis & Risiko", "Strategic & Risk Intelligence"),
                    tagline = ltext("Baca sinyal sebelum jadi krisis.", "Read the signals before they become a crisis."),
                    points = listOf(
                        ltext("Pemantauan isu", "Issue monitoring"),
                        ltext("Peringatan dini", "Early warning"),
                        ltext("Paparan untuk pimpinan", "Leadership briefings"),
                    ),
                    icon = IconKey.Radar,
                    platform = ProductSlug.Clarivio,
                ),
                ServiceItem(
                    number = "02",
                    title = ltext("Kesiapan & Kompetensi SDM", "Workforce Readiness & Competence"),
                    tagline = ltext(
                        "Kompetensi tetap hidup setelah pelatihan.",
                        "Competence that lasts beyond training.",
                    ),
                    points = listOf(
                        ltext("Asesmen skenario", "Scenario assessments"),
                        ltext("Penguatan berkala", "Periodic reinforcement"),
                        ltext("Dasbor kesiapan", "Readiness dashboard"),
                    ),
                    icon = IconKey.Users,
                    platform = ProductSlug.Ascendio,
                ),
                ServiceItem(
                    number = "03",
                    title = ltext("Dampak Sosial & CSR", "Social Impact & CSR"),
                    tagline = ltext("Dari kegiatan ke dampak yang terbukti.", "From activities to proven impact."),
                    points = listOf(
                        ltext("Data dasar sosial", "Social baseline"),
                        ltext("Indikator dampak", "Impact indicators"),
                        ltext("Penerimaan sosial", "Social license"),
                    ),
                    icon = IconKey.Sprout,
                    platform = ProductSlug.Sociara,
                ),
                ServiceItem(
                    number = "04",
                    title = ltext("Kinerja ESG", "ESG Performance"),
                    tagline = ltext("Dari laporan ke kinerja yang teruji.", "From reports to tested performance."),
                    points = listOf(
                        ltext("Kesiapan & celah", "Readiness & gaps"),
                        ltext("Materialitas", "Materiality"),
                        ltext("Peta jalan ESG", "ESG roadmap"),
                    ),
                    icon = IconKey.Leaf,
                    platform = ProductSlug.Evidara,
                ),
            ),
            alsoAvailable = ltext("Juga tersedia:", "Also available:"),
            extras = listOf(
                ltext("Solusi analisis kustom", "Custom intelligence solutions"),
                ltext("Pengembangan kapabilitas tim", "Team capability development"),
            ),
        ),
        process = ProcessContent(
            overline = ltext("Cara Kerja", "How We Work"),
            title = ltext(
                "Empat tahap, dari masalah sampai hasil yang bertahan.",
                "Four stages, from problem to lasting results.",
            ),
            subtitle = ltext(
                "Prosesnya jelas dan terukur di setiap tahap.",
                "A clear, measurable process at every stage.",
            ),
            steps = listOf(
                ProcessStep(
                    "01",
                    ltext("Diagnosis", "Diagnose"),
                    ltext("Petakan masalah dengan kerangka buku.", "Map the problem using our book frameworks."),
                    IconKey.Search,
                ),
                ProcessStep(
                    "02",
                    ltext("Perancangan", "Design"),
                    ltext("Rancang solusi dan target bersama.", "Design the solution and targets together."),
                    IconKey.Layers,
                ),
                ProcessStep(
                    "03",
                    ltext("Penerapan", "Deploy"),
                    ltext("Jalankan platform atau solusi kustom.", "Run the platform or a custom solution."),
                    IconKey.Rocket,
                ),
                ProcessStep(
                    "04",
                    ltext("Keberlanjutan", "Sustain"),
                    ltext("Evaluasi berkala, tim makin mandiri.", "Regular reviews as your team becomes self-reliant."),
                    IconKey.Refresh,
                ),
            ),
            collaborationTitle = ltext("Bentuk kerja sama", "Ways to work with us"),
            collaborationModels = listOf(
                ltext("Proyek pendampingan", "Advisory projects"),
                ltext("Layanan analisis terkelola", "Managed intelligence"),
                ltext("Langganan platform", "Platform subscription"),
                ltext("Program penguatan kapabilitas", "Capability programs"),
            ),
        ),
        platforms = PlatformsContent(
            overline = ltext("Platform", "Platforms"),
            title = ltext("Didukung platform AI milik Sekota.", "Powered by Sekota's own AI platforms."),
            subtitle = ltext(
                "Layanan Sekota punya alatnya sendiri — dibangun di atas teknologi eBdesk.",
                "Every Sekota service comes with its own tools, built on eBdesk technology.",
            ),
            techBadge = ltext("Teknologi oleh eBdesk", "Technology by eBdesk"),
            items = listOf(
                PlatformCard(
                    ProductSlug.Clarivio,
                    ltext("Analisis ancaman dan peluang untuk pimpinan.", "Threat and opportunity intelligence for leadership."),
                    ltext("Pelajari", "Learn more"),
                ),
                PlatformCard(
                    ProductSlug.Ascendio,
                    ltext("Kesiapan dan kompetensi tim setelah pelatihan.", "Team readiness and competence after training."),
                    ltext("Pelajari", "Learn more"),
                ),
                PlatformCard(
                    ProductSlug.Sociara,
                    ltext("Manajemen CSR hingga dampak terbukti.", "CSR management through to proven impact."),
                    ltext("Pelajari", "Learn more"),
                ),
                PlatformCard(
                    ProductSlug.Evidara,
                    ltext("Kesiapan dan kinerja ESG berbasis AI.", "AI-driven ESG readiness and performance."),
                    ltext("Pelajari", "Learn more"),
                ),
            ),
        ),
        books = BooksSectionContent(
            overline = ltext("Wawasan", "Insights"),
            title = ltext("Cara berpikir kami, dibukukan.", "Our way of thinking, in print."),
            subtitle = ltext(
                "Enam buku thought leadership berbahasa Indonesia yang menjadi landasan cara berpikir Sekota.",
                "Six Indonesian-language thought leadership books that shape how Sekota thinks.",
            ),
            items = listOf(
                BookCard(
                    BookSlug.ForgettingEnterprise,
                    "The Forgetting Enterprise",
                    ltext("tahu → melakukan", "knowing → doing"),
                    ltext("Mengapa yang sudah dilatih tidak dijalankan?", "Why isn't what we trained actually practiced?"),
                ),
                BookCard(
                    BookSlug.BlindSpotRadar,
                    "Blind Spot Radar",
                    ltext("sinyal → keputusan", "signal → decision"),
                    ltext("Apa yang sedang kita lewatkan?", "What are we missing right now?"),
                ),
                BookCard(
                    BookSlug.ManifestoEkuitasLahan,
                    "Manifesto Ekuitas Lahan",
                    ltext("kepemilikan → kepercayaan", "ownership → trust"),
                    ltext("Mengapa lahan kita tidur?", "Why is our land lying idle?"),
                ),
                BookCard(
                    BookSlug.ImpactDrivenCsr,
                    "Impact-Driven CSR",
                    ltext("program → dampak", "program → impact"),
                    ltext(
                        "Dari kepatuhan ke dampak nyata bagi komunitas.",
                        "From compliance to real impact for communities.",
                    ),
                ),
                BookCard(
                    BookSlug.AccountableEsg,
                    "Accountable ESG",
                    ltext("laporan → kinerja", "report → performance"),
                    ltext(
                        "Dari laporan keberlanjutan ke kinerja yang teruji.",
                        "From sustainability reports to tested performance.",
                    ),
                ),
                BookCard(
                    BookSlug.SustainabilityLeadership,
                    "Sustainability Leadership",
                    ltext("perusahaan → sistem", "company → system"),
                    ltext(
                        "Dari program perusahaan ke transformasi sistemik.",
                        "From company programs to systemic transformation.",
                    ),
                ),
            ),
            detailLabel = ltext("Lihat detail", "Read more"),
        ),
        partner = PartnerContent(
            title = ltext("Mari tumbuh dan berkembang bersama.", "Let's grow together."),
            text = ltext(
                "Program Mitra Awal: bangun solusi analisis bersama tim inti Sekota, dengan target yang kita sepakati bersama.",
                "Early Partner Program: build intelligence solutions with Sekota's core team, against targets we agree on together.",
            ),
            cta = SiteContent.contactUs,
        ),
        faq = FaqContent(
            overline = ltext("FAQ", "FAQ"),
            title = ltext("Pertanyaan sebelum bekerja sama.", "Questions before we work together."),
            mailboxLabel = ltext("Pertanyaan lain?", "Other questions?"),
            email = SiteContent.email,
            items = listOf(
                FaqItem(
                    0,
                    ltext("Apa hubungan Sekota dan eBdesk?", "How are Sekota and eBdesk related?"),
                    ltext(
                        "Sekota adalah unit bisnis eBdesk Group. eBdesk membangun teknologinya; Sekota mendampingi organisasi memakainya, dari kerangka berpikir sampai solusi dipakai.",
                        "Sekota is a business unit of eBdesk Group. eBdesk builds the technology; Sekota helps organizations use it, from the thinking framework to solutions in daily use.",
                    ),
                ),
                FaqItem(
                    1,
                    ltext(
                        "Layanan mana yang tepat untuk organisasi kami?",
                        "Which service is right for our organization?",
                    ),
                    ltext(
                        "Mulai dari tantangan utamanya. Untuk membaca risiko dan isu, pilih Analisis Strategis & Risiko. Untuk menjaga kompetensi tim setelah pelatihan, pilih Kesiapan & Kompetensi SDM. Untuk membuktikan dampak program CSR, pilih Dampak Sosial & CSR. Untuk kesiapan dan kinerja ESG, pilih Kinerja ESG. Kalau belum yakin, tahap Diagnosis membantu memetakan kebutuhan sebelum memilih.",
                        "Start from your main challenge. To read risks and issues, choose Strategic & Risk Intelligence. To keep your team competent after training, choose Workforce Readiness & Competence. To prove the impact of CSR programs, choose Social Impact & CSR. For ESG readiness and performance, choose ESG Performance. Not sure yet? The Diagnose stage maps your needs before you choose.",
                    ),
                ),
                FaqItem(
                    2,
                    ltext(
                        "Apakah harus berlangganan platform untuk memakai layanan Sekota?",
                        "Do we need a platform subscription to use Sekota's services?",
                    ),
                    ltext(
                        "Tidak harus. Kerja sama bisa berbentuk proyek pendampingan, layanan analisis terkelola, langganan platform, atau program penguatan kapabilitas. Platform dipakai bila memang membantu mencapai target yang disepakati bersama.",
                        "No. We can work together through advisory projects, managed intelligence, platform subscriptions, or capability programs. A platform is used only when it helps reach the targets we agree on.",
                    ),
                ),
                FaqItem(
                    3,
                    ltext("Bagaimana data organisasi kami dijaga?", "How is our organization's data protected?"),
                    ltext(
                        "Layanan Sekota berjalan di atas infrastruktur eBdesk yang tersertifikasi ISO 27001. Akses data dibatasi sesuai kebutuhan pekerjaan, dan aturan penggunaan data disepakati di awal kerja sama.",
                        "Sekota's services run on eBdesk's ISO 27001-certified infrastructure. Data access is limited to what the work requires, and data-use rules are agreed at the start of the engagement.",
                    ),
                ),
                FaqItem(
                    4,
                    ltext(
                        "Apakah layanan dan platform bisa disesuaikan dengan proses kami?",
                        "Can the services and platforms be tailored to our processes?",
                    ),
                    ltext(
                        "Bisa. Solusi dirancang bersama pada tahap Perancangan, dan platform dapat dikonfigurasi sesuai data serta alur kerja organisasi Anda, atau dikembangkan sebagai solusi analisis kustom.",
                        "Yes. Solutions are designed together in the Design stage, and platforms can be configured to your data and workflows, or built as a custom intelligence solution.",
                    ),
                ),
                FaqItem(
                    5,
                    ltext("Berapa lama sebuah proyek biasanya berjalan?", "How long does a project usually take?"),
                    ltext(
                        "Tergantung ruang lingkupnya. Proyek pendampingan umumnya berjangka tertentu, sedangkan layanan analisis terkelola dan langganan platform berjalan berkelanjutan dengan evaluasi berkala. Perkiraan durasi kami sampaikan setelah tahap Diagnosis.",
                        "It depends on the scope. Advisory projects usually run for a set period, while managed intelligence and platform subscriptions run continuously with regular reviews. We share an estimated timeline after the Diagnose stage.",
                    ),
                ),
                FaqItem(
                    6,
                    ltext("Bagaimana cara memulai kerja sama?", "How do we get started?"),
                    ltext(
                        "Hubungi kami lewat info@sekota.id atau tombol Hubungi Kami. Kami mulai dengan diskusi singkat dan tahap Diagnosis untuk memetakan kebutuhan, lalu menyusun rencana dan target bersama.",
                        "Reach us at info@sekota.id or through the Contact Us page. We start with a short discussion and the Diagnose stage to map your needs, then set the plan and targets together.",
                    ),
                ),
            ),
        ),
    )
}
