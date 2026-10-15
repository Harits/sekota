import { Outlet } from 'react-router-dom';
import Navbar from './Navbar';
import Footer from './Footer';
import useScrollToHash from '@/hooks/useScrollToHash';

/** Kerangka semua halaman: Navbar di atas, isi halaman, Footer di bawah. */
export default function Layout() {
  useScrollToHash();

  return (
    <>
      <Navbar />
      <main>
        <Outlet />
      </main>
      <Footer />
    </>
  );
}
