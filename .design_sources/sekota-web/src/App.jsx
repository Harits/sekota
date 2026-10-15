import { Routes, Route } from 'react-router-dom';
import Layout from '@/components/layout/Layout';
import HomePage from '@/pages/HomePage';
import EbookCatalogPage from '@/pages/EbookCatalogPage';
import BookDetailPage from '@/pages/BookDetailPage';
import ReaderPage from '@/pages/ReaderPage';
import MerchandisePage from '@/pages/MerchandisePage';
import MerchDetailPage from '@/pages/MerchDetailPage';
import ProductPage from '@/pages/ProductPage';
import NotFoundPage from '@/pages/NotFoundPage';

/**
 * Daftar halaman (routing).
 *   /                               → Landing Page
 *   /ebook                          → eBook Catalog
 *   /ebook/blind-spot-radar         → Book - Details
 *   /ebook/blind-spot-radar/baca    → E-Book Reader
 *   /merchandise                    → Merchandise
 *   /merchandise/black-totebag      → Merchandise - Details
 *   /produk/:slug                   → Product - Veridia / Ascendio / Sociara / Ecoflow
 */
export default function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route index element={<HomePage />} />
        <Route path="ebook" element={<EbookCatalogPage />} />
        <Route path="ebook/:slug" element={<BookDetailPage />} />
        <Route path="ebook/:slug/baca" element={<ReaderPage />} />
        <Route path="merchandise" element={<MerchandisePage />} />
        <Route path="merchandise/:slug" element={<MerchDetailPage />} />
        <Route path="produk/:slug" element={<ProductPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Route>
    </Routes>
  );
}
