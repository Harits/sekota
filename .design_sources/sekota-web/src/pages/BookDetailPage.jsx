import DetailView from '@/components/detail/DetailView';
import { bookDetail } from '@/data/catalog';

/** Halaman detail buku — frame "Book - Details" di Figma. */
export default function BookDetailPage() {
  return (
    <DetailView
      data={bookDetail}
      primary={{ label: 'Read', to: `/ebook/${bookDetail.slug}/baca` }}
      secondary={{ label: 'Add to Library', savedLabel: 'Saved to Library ✓' }}
    />
  );
}
