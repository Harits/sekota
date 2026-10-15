/**
 * Data 4 produk Intelligence Suite.
 * Dipakai di: Home (section Ekosistem Produk), halaman /produk/:slug, dan Footer.
 * Untuk menambah/mengubah produk cukup edit file ini.
 */
import logoVeridia from '@/assets/logos/logo-veridia.png';
import logoAscendio from '@/assets/logos/logo-ascendio.png';
import logoSociara from '@/assets/logos/logo-sociara.png';
import logoEcoflow from '@/assets/logos/logo-ecoflow.png';
import markVeridia from '@/assets/logos/mark-veridia.png';
import markAscendio from '@/assets/logos/mark-ascendio.png';
import markSociara from '@/assets/logos/mark-sociara.png';
import markEcoflow from '@/assets/logos/mark-ecoflow.png';
import arrowTeal from '@/assets/icons/arrow-teal.png';
import arrowBlue from '@/assets/icons/arrow-blue.png';
import arrowSgreen from '@/assets/icons/arrow-sgreen.png';
import arrowGreen from '@/assets/icons/arrow-green.png';

export const products = [
  {
    slug: 'veridia',
    name: 'VERIDIA',
    category: 'BI Dashboard',
    accent: 'var(--color-veridia)',
    gradientDot: true,
    logo: logoVeridia,
    mark: markVeridia,
    arrow: arrowTeal,
    summary:
      'Platform Intelligence Dashboard (BI) tingkat lanjut yang mengagregasi data dari berbagai sumber untuk memberikan visualisasi strategis bagi pengambil keputusan.',
    highlights: ['Custom Strategic Dashboards', 'Predictive Trend Analytics', 'Cross-Department Data Integration'],
    tagline: 'Ubah data yang tersebar menjadi keputusan strategis yang terukur.',
    description:
      'Veridia adalah platform Intelligence Dashboard (BI) tingkat lanjut yang mengagregasi data dari berbagai sumber, membersihkannya, dan menyajikan visualisasi strategis bagi pimpinan organisasi, dari level eksekutif hingga operasional.',
    features: [
      ['Custom Strategic Dashboards', 'Dashboard yang disusun sesuai KPI dan prioritas strategis setiap pimpinan.'],
      ['Predictive Trend Analytics', 'Model prediktif untuk membaca arah tren sebelum menjadi masalah.'],
      ['Cross-Department Data Integration', 'Satukan data lintas unit dan sistem dalam satu sumber kebenaran.'],
      ['Real-time Alert & Monitoring', 'Notifikasi otomatis saat indikator penting keluar dari ambang batas.'],
      ['Executive Report Builder', 'Laporan eksekutif siap presentasi dalam hitungan menit.'],
      ['Role-based Access Control', 'Hak akses berlapis untuk menjaga keamanan dan kerahasiaan data.'],
    ],
  },
  {
    slug: 'ascendio',
    name: 'ASCENDIO',
    category: 'Community',
    accent: 'var(--color-ascendio)',
    logo: logoAscendio,
    mark: markAscendio,
    arrow: arrowBlue,
    summary:
      'Platform manajemen komunitas dan pengembangan kapasitas yang fokus pada penilaian kompetensi dan pemantauan aktivitas lapangan secara digital.',
    highlights: ['Capacity & Competency Assessment', 'Community Engagement Tracking', 'Field Activity Reporting'],
    tagline: 'Kelola komunitas dan kembangkan kapasitas secara terukur.',
    description:
      'Ascendio adalah platform manajemen komunitas dan pengembangan kapasitas yang fokus pada penilaian kompetensi serta pemantauan aktivitas lapangan secara digital, sehingga setiap program pemberdayaan dapat dipantau dan dievaluasi dengan jelas.',
    features: [
      ['Capacity & Competency Assessment', 'Penilaian kompetensi terstandar untuk memetakan kebutuhan pengembangan.'],
      ['Community Engagement Tracking', 'Pantau partisipasi dan keterlibatan anggota komunitas dari waktu ke waktu.'],
      ['Field Activity Reporting', 'Laporan kegiatan lapangan langsung dari perangkat petugas.'],
      ['Program & Training Management', 'Rencanakan, jadwalkan, dan evaluasi program pelatihan dalam satu tempat.'],
      ['Geo-tagged Monitoring', 'Verifikasi aktivitas lapangan dengan lokasi dan dokumentasi.'],
      ['Impact Dashboard', 'Ringkasan dampak program untuk pelaporan ke pemangku kepentingan.'],
    ],
  },
  {
    slug: 'sociara',
    name: 'SOCIARA',
    category: 'Social',
    accent: 'var(--color-sociara)',
    logo: logoSociara,
    mark: markSociara,
    arrow: arrowSgreen,
    summary:
      "Platform manajemen dampak sosial untuk mengelola program CSR dan memantau status 'Social License to Operate' (SLO) perusahaan.",
    highlights: ['Social License to Operate (SLO) Monitoring', 'CSR Impact Measurement & Attribution', 'Stakeholder Perception Mapping'],
    tagline: 'Jaga Social License to Operate dengan data dampak yang nyata.',
    description:
      'Sociara adalah platform manajemen dampak sosial untuk mengelola program CSR, memetakan persepsi pemangku kepentingan, dan memantau status Social License to Operate (SLO) perusahaan secara berkelanjutan.',
    features: [
      ['SLO Monitoring', 'Pantau tingkat penerimaan sosial perusahaan di setiap wilayah operasi.'],
      ['CSR Impact Measurement & Attribution', 'Ukur dan atribusikan dampak setiap program CSR secara objektif.'],
      ['Stakeholder Perception Mapping', 'Peta persepsi dan kepentingan para pemangku kepentingan utama.'],
      ['Grievance Management', 'Kelola keluhan masyarakat dengan alur penanganan yang transparan.'],
      ['Program Portfolio Tracking', 'Lihat status anggaran, progres, dan capaian seluruh program.'],
      ['Social Impact Reporting', 'Laporan dampak sosial siap untuk audit dan publikasi.'],
    ],
  },
  {
    slug: 'ecoflow',
    name: 'ECOFLOW',
    category: 'Sustainability',
    accent: 'var(--color-ecoflow)',
    logo: logoEcoflow,
    mark: markEcoflow,
    arrow: arrowGreen,
    summary:
      'Solusi Intelligence untuk ESG (Environmental, Social, and Governance) yang mengotomatisasi pengukuran jejak karbon dan kesiapan audit kepatuhan.',
    highlights: ['Automated ESG Audit Readiness', 'Carbon Footprint Calculator (MRV)', 'Compliance Status Monitoring'],
    tagline: 'Jadikan ESG kompas strategis, bukan sekadar beban administratif.',
    description:
      'Ecoflow adalah solusi intelligence untuk ESG (Environmental, Social, and Governance) yang mengotomatisasi pengukuran jejak karbon, memetakan kepatuhan terhadap standar pelaporan, dan menyiapkan organisasi menghadapi audit keberlanjutan.',
    features: [
      ['Automated ESG Audit Readiness', 'Dokumen dan bukti pendukung audit ESG tersusun otomatis.'],
      ['Carbon Footprint Calculator (MRV)', 'Hitung, laporkan, dan verifikasi emisi karbon Scope 1, 2, dan 3.'],
      ['Compliance Status Monitoring', 'Status kepatuhan regulasi terpantau dalam satu dashboard.'],
      ['Framework Mapping', 'Pemetaan indikator ke standar seperti GRI dan regulasi keberlanjutan nasional.'],
      ['Supply Chain Emissions', 'Lacak emisi dari pemasok dan rantai nilai.'],
      ['ESG Scorecard', 'Skor kinerja ESG untuk dipantau pimpinan secara berkala.'],
    ],
  },
];

export const getProduct = (slug) => products.find((p) => p.slug === slug);
