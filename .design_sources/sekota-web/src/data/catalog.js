/**
 * Data katalog e-book & merchandise.
 * Dipakai di: /ebook, /merchandise, halaman detail, dan section E-Book di Home.
 */
import bookBlindSpot from '@/assets/images/books/book-blind-spot.webp';
import bookTheBook from '@/assets/images/books/book-the-book.webp';
import bookMidnight from '@/assets/images/books/book-midnight.webp';
import bookDune from '@/assets/images/books/book-dune.webp';
import bookKlara from '@/assets/images/books/book-klara.webp';
import book7Habits from '@/assets/images/books/book-7-habits.webp';
import coverBlindSpot from '@/assets/images/covers/cover-blind-spot.webp';
import merchTotebag from '@/assets/images/merch/merch-totebag.webp';
import merchTumbler from '@/assets/images/merch/merch-tumbler.webp';
import merchTshirt from '@/assets/images/merch/merch-tshirt.webp';
import merchNotebook from '@/assets/images/merch/merch-notebook.webp';
import merchKeychain from '@/assets/images/merch/merch-keychain.webp';

/* ---------- Kartu e-book di Home (section "Perpustakaan Digital") ---------- */
export const featuredEbooks = [
  { title: 'Transformasi Digital Pemerintah Daerah', tag: 'Smart City', category: 'smartcity', gradient: 'var(--gradient-ebook-blue)', excerpt: 'Strategi dan framework implementasi Smart City…' },
  { title: 'Strategi Pemilu Berbasis Data Analytics', tag: 'Politik', category: 'politik', gradient: 'var(--gradient-ebook-orange)', excerpt: 'Panduan untuk caleg dan tim sukses memanfaatkan…' },
  { title: 'Implementasi Satu Data Kab/Kota', tag: 'Data', category: 'smartcity', gradient: 'var(--gradient-ebook-green)', excerpt: 'Langkah teknis membangun datawarehouse lintas OPD…' },
  { title: 'OSINT & Data Intelligence Modern', tag: 'Intelligence', category: 'intelligence', gradient: 'var(--gradient-ebook-violet)', excerpt: 'Metodologi Open Source Intelligence untuk…' },
];

/* ---------- Katalog e-book (/ebook) ---------- */
export const books = [
  { slug: 'blind-spot-radar', title: 'Blind Spot Radar', author: 'Putu aan j.', image: bookBlindSpot, rating: 4, score: '4.8', genre: 'self', year: 2025, order: 1 },
  { slug: 'the-book', title: 'The Book', author: 'Alf lowans', image: bookTheBook, rating: 4, genre: 'other', year: 2025, order: 2 },
  { slug: 'the-midnight-library', title: 'The Midnight Library', author: 'Matt Haig', image: bookMidnight, rating: 4, score: '4.8', genre: 'self', year: 2024, order: 3 },
  { slug: 'dune-part-one', title: 'Dune: Part One', author: 'Frank Herbert', image: bookDune, rating: 4, genre: 'other', year: 2023, order: 4 },
  { slug: 'klara-and-the-sun', title: 'Klara and the Sun', author: 'Kazuo Ishiguro', image: bookKlara, rating: 4, genre: 'social', year: 2024, order: 5 },
  { slug: 'the-7-habits', title: 'The 7 Habits', author: 'Stephen Covey', image: book7Habits, rating: 4, genre: 'self', year: 2023, order: 6 },
];

export const bookGenres = [
  { id: 'self', label: 'Self-Improvement' },
  { id: 'social', label: 'Social-Improvement' },
  { id: 'sust', label: 'Sustainablity' },
  { id: 'other', label: 'Other' },
];

export const bookYears = [2025, 2024, 2023];

/* ---------- Detail buku (/ebook/blind-spot-radar) ---------- */
export const bookDetail = {
  slug: 'blind-spot-radar',
  title: 'Blind Spot Radar',
  author: 'Putu Aan J.',
  cover: coverBlindSpot,
  breadcrumb: ['Self-Improvement', 'Mindfulness'],
  rating: 4,
  ratingLabel: '(4.8/5 from 1,240 readers)',
  description: [
    'Buku "Blind Spot Radar: Mengapa Pemimpin Cerdas Melewatkan Sinyal Besar" merupakan karya thought leadership yang menyoroti fenomena di mana para pengambil keputusan tingkat atas sering kali gagal mendeteksi ancaman nyata atau peluang strategis, meskipun mereka memiliki kecerdasan dan data yang memadai.',
    'Buku ini ditujukan bagi para pengambil keputusan di berbagai level (CEO, Manajemen Senior, hingga Menengah) yang ingin meningkatkan kualitas keputusan dan menghindari "biaya" tak terlihat dari blind spot.',
  ],
  insideTitle: "What's Inside",
  inside: [
    ['The Attention Economy', 'Understanding the mechanics behind digital distractions.'],
    ['Deep Work Protocols', 'Actionable steps to enter flow state on command.'],
    ['Mindful Tech Integration', 'Setting boundaries that stick without the guilt.'],
    ['Cognitive Recovery', 'Restoring your mental energy after intense digital usage.'],
  ],
  progress: { label: 'AVERAGE READING TIME', value: '3H 45M', percent: 66.67 },
  specs: [
    ['Published', 'Nov 2025'],
    ['Pages', '240'],
    ['Language', 'Indonesia'],
    ['Format', 'eBook, PDF'],
  ],
};

/* ---------- Merchandise (/merchandise) ---------- */
export const merchandise = [
  { slug: 'black-totebag', title: 'Black Totebag', author: '“The Urban Collaborator”', image: merchTotebag, rating: 4, category: 'others' },
  { slug: 'matte-black-tumbler', title: 'Matte Black Tumbler', author: '“The Sinergi Executive”', image: merchTumbler, rating: 4, score: '4.2', category: 'others' },
  { slug: 'black-t-shirt', title: 'Black T Shirt', author: '“Sinergi Everyday”', image: merchTshirt, rating: 4, score: '4.8', category: 'tshirt' },
  { slug: 'note-book-a5', title: 'Note Book A5', author: '“Strategic Foresight”', image: merchNotebook, rating: 4, category: 'others' },
  { slug: 'keychain', title: 'Keychain', author: '“Connectivity”', image: merchKeychain, rating: 4, category: 'others' },
];

export const merchCategories = [
  { id: 'tshirt', label: 'T-Shirt' },
  { id: 'pin', label: 'Pin' },
  { id: 'sticker', label: 'Sticker' },
  { id: 'others', label: 'Others' },
];

/* ---------- Detail merchandise (/merchandise/black-totebag) ---------- */
export const merchDetail = {
  slug: 'black-totebag',
  title: 'Black Totebag',
  author: '“The Urban Collaborator”',
  cover: merchTotebag,
  breadcrumb: ['Merchandise', 'Bag & Accessories'],
  rating: 4,
  ratingLabel: '(4.7/5 dari 318 ulasan)',
  description: [
    'Black Totebag “The Urban Collaborator” dirancang untuk para profesional yang bergerak di antara ruang rapat dan lapangan. Tampilan minimalis berwarna hitam dengan aksen logo Sekota menjadikannya pernyataan identitas yang tenang namun kuat.',
    'Cocok sebagai perlengkapan harian tim, souvenir acara strategis, maupun hadiah korporat untuk mitra dan klien. Tersedia opsi kustomisasi untuk pemesanan dalam jumlah besar.',
  ],
  insideTitle: 'Keunggulan Produk',
  inside: [
    ['Canvas 12oz Premium', 'Bahan kanvas tebal yang awet untuk aktivitas harian dan lapangan.'],
    ['Kapasitas Laptop 14"', 'Kompartemen dalam berlapis untuk laptop dan dokumen.'],
    ['Sablon Logo Presisi', 'Logo Sekota dicetak DTF, tidak mudah pudar.'],
    ['Ramah Lingkungan', 'Diproduksi dengan bahan dan proses yang lebih berkelanjutan.'],
  ],
  progress: { label: 'STOK TERSEDIA', value: '64 PCS', percent: 64 },
  specs: [
    ['Harga', 'Rp 149.000'],
    ['Dimensi', '38 × 42 × 10 cm'],
    ['Warna', 'Hitam'],
    ['Minimum Order', '1 pcs · Grosir mulai 25 pcs'],
  ],
};
