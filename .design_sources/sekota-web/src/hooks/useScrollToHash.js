import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

/**
 * Setiap pindah halaman: gulir ke atas.
 * Kalau URL punya hash (contoh /#kontak), gulir ke elemen dengan id tersebut.
 */
export default function useScrollToHash() {
  const { pathname, hash, key } = useLocation();

  useEffect(() => {
    if (hash) {
      // tunggu halaman selesai render sebelum menggulir
      const id = decodeURIComponent(hash.slice(1));
      const timer = setTimeout(() => {
        document.getElementById(id)?.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }, 50);
      return () => clearTimeout(timer);
    }
    window.scrollTo({ top: 0, left: 0, behavior: 'instant' });
    return undefined;
  }, [pathname, hash, key]);
}
