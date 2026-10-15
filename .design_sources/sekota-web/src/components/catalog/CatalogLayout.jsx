import { Link } from 'react-router-dom';
import bagIcon from '@/assets/icons/bag.png';
import './CatalogLayout.css';

/**
 * Kerangka halaman katalog: sidebar kiri (filter + promo) dan konten kanan.
 * Dipakai oleh EbookCatalogPage dan MerchandisePage.
 */
export default function CatalogLayout({ sidebar, children }) {
  return (
    <div className="catalog">
      <div className="catalog__layout">
        <aside className="catalog__aside">{sidebar}</aside>
        <div className="catalog__main">{children}</div>
      </div>
    </div>
  );
}

/** Kotak putih di sidebar. */
export function SidebarBox({ tight = false, children }) {
  return <div className={`sidebar-box ${tight ? 'sidebar-box--tight' : ''}`}>{children}</div>;
}

/** Grup filter dengan judul ("Sort by", "Genre", "Year", ...). */
export function FilterGroup({ title, children }) {
  return (
    <div className="filter-group">
      <h3 className="sidebar-box__title">{title}</h3>
      {children}
    </div>
  );
}

/** Kartu promo "Customize Solution". */
export function CustomizePromo() {
  return (
    <SidebarBox tight>
      <h3 className="sidebar-box__title sidebar-box__title--dim">Customize Solution</h3>
      <p className="sidebar-box__text">Hubungi kami untuk proposal khusus</p>
      <Link to="/#kontak" className="sidebar-btn sidebar-btn--gradient">Contact Us</Link>
    </SidebarBox>
  );
}

/** Kartu promo "Executive Presence" (ajakan ke halaman merchandise). */
export function MerchPromo() {
  return (
    <SidebarBox tight>
      <img src={bagIcon} width="32" height="32" alt="" />
      <h3 className="sidebar-box__title sidebar-box__title--dim">Executive Presence</h3>
      <p className="sidebar-box__text">Lebih dari Sekadar Merchandise, Ini Adalah Pesan Strategis.</p>
      <Link to="/merchandise" className="sidebar-btn sidebar-btn--cyan">Get Merchandise</Link>
    </SidebarBox>
  );
}

/** Judul halaman katalog + deskripsi + slot kanan (misal dropdown sort). */
export function CatalogHeader({ title, description, aside, descriptionWidth }) {
  return (
    <div className="catalog-header">
      <div style={{ maxWidth: descriptionWidth }}>
        <h1>{title}</h1>
        <p>{description}</p>
      </div>
      {aside && <div className="catalog-header__aside">{aside}</div>}
    </div>
  );
}
