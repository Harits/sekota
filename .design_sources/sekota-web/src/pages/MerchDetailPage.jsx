import DetailView from '@/components/detail/DetailView';
import { merchDetail } from '@/data/catalog';

/** Halaman detail merchandise — frame "Merchandise - Details" di Figma. */
export default function MerchDetailPage() {
  return (
    <DetailView
      data={merchDetail}
      primary={{ label: 'Pesan Sekarang', to: '/#kontak' }}
      secondary={{ label: 'Tambah ke Wishlist', savedLabel: 'Di Wishlist ✓' }}
    />
  );
}
