import { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import logo from '@/assets/logos/logo-sekota.png';
import Button from '@/components/ui/Button';
import useActiveSection from '@/hooks/useActiveSection';
import { navLinks } from '@/data/site';
import './Navbar.css';

const HOME_SECTIONS = ['hero', 'solusi', 'produk', 'mengapa', 'kontak'];

/** Tentukan menu aktif berdasarkan halaman yang sedang dibuka. */
function activeKeyFor(pathname, section) {
  if (pathname === '/') return section;
  if (pathname.startsWith('/ebook')) return 'ebook';
  if (pathname.startsWith('/produk')) return 'produk';
  return '';
}

export default function Navbar() {
  const [open, setOpen] = useState(false);
  const { pathname } = useLocation();
  const section = useActiveSection(HOME_SECTIONS, pathname === '/');
  const activeKey = activeKeyFor(pathname, section);
  const close = () => setOpen(false);

  return (
    <header className={`navbar ${open ? 'is-open' : ''}`}>
      <div className="container navbar__inner">
        <Link to="/#hero" className="navbar__logo" onClick={close} aria-label="Sekota beranda">
          <img src={logo} alt="Sekota Sinergi Indonesia" />
        </Link>

        <nav className="navbar__links" aria-label="Navigasi utama">
          {navLinks.map((link) => (
            <Link
              key={link.key}
              to={link.to}
              onClick={close}
              className={activeKey === link.key ? 'is-active' : ''}
              aria-current={activeKey === link.key ? 'page' : undefined}
            >
              {link.label}
            </Link>
          ))}
        </nav>

        <Button to="/#kontak" variant="gradient" size="sm" className="navbar__cta">
          Konsultasi Strategis
        </Button>

        <button
          className="navbar__toggle"
          onClick={() => setOpen((v) => !v)}
          aria-label={open ? 'Tutup menu' : 'Buka menu'}
          aria-expanded={open}
        >
          <span />
        </button>
      </div>
    </header>
  );
}
