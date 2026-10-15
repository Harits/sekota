import { useEffect, useState } from 'react';

/**
 * Scroll-spy: mengembalikan id section yang sedang terlihat di tengah layar.
 * Dipakai Navbar untuk menandai menu aktif saat di Home.
 *
 * @param {string[]} ids     Daftar id section yang dipantau
 * @param {boolean} enabled  Matikan di halaman selain Home
 */
export default function useActiveSection(ids, enabled = true) {
  const [active, setActive] = useState('');

  useEffect(() => {
    if (!enabled) {
      setActive('');
      return undefined;
    }
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) setActive(entry.target.id);
        });
      },
      { rootMargin: '-45% 0px -50% 0px' },
    );
    ids.forEach((id) => {
      const el = document.getElementById(id);
      if (el) observer.observe(el);
    });
    return () => observer.disconnect();
  }, [ids, enabled]);

  return active;
}
