/**
 * Data umum website: menu navigasi, kontak, dan link footer.
 * "to" memakai path react-router. Path dengan "#" akan menggulir ke section di Home.
 */
export const navLinks = [
  { label: 'Solusi', to: '/#solusi', key: 'solusi' },
  { label: 'Produk', to: '/#produk', key: 'produk' },
  { label: 'E-Book', to: '/ebook', key: 'ebook' },
  { label: 'Mengapa Sekota', to: '/#mengapa', key: 'mengapa' },
  { label: 'Kontak', to: '/#kontak', key: 'kontak' },
];

export const contact = {
  address: ['Jl. H. Rasam No.96, Parigi Baru,', 'Pd. Aren, Tangerang Selatan 15228'],
  email: 'sales@sekota.id',
  website: 'www.sekota.id',
};

export const footerColumns = [
  {
    title: 'Produk',
    links: [
      { label: 'VERIDIA', to: '/produk/veridia' },
      { label: 'ASCENDIO', to: '/produk/ascendio' },
      { label: 'SOCIARA', to: '/produk/sociara' },
      { label: 'ECOFLOW', to: '/produk/ecoflow' },
    ],
  },
  {
    title: 'Perusahaan',
    links: [
      { label: 'Tentang Kami', to: '/#mengapa' },
      { label: 'E-Book', to: '/ebook' },
      { label: 'Merchandise', to: '/merchandise' },
      { label: 'Insights' },
      { label: 'Kontak', to: '/#kontak' },
    ],
  },
  {
    title: 'Legal',
    links: [{ label: 'Privasi' }, { label: 'Syarat & Ketentuan' }],
  },
];

export const socialLinks = [
  { label: 'LinkedIn', href: 'https://www.linkedin.com' },
  { label: 'Twitter', href: 'https://twitter.com' },
];
