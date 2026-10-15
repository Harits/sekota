import './SectionHeader.css';

/**
 * Judul section: eyebrow bergradien + judul besar + paragraf pembuka.
 * Pola ini dipakai di hampir semua section (Tantangan, Solusi, Produk, dst).
 *
 * @param {string} eyebrow   Label kecil di atas judul, contoh "SOLUSI KAMI"
 * @param {ReactNode} title  Judul utama
 * @param {ReactNode} lead   Paragraf pembuka (opsional)
 * @param {'left'|'center'} align
 * @param {'48'|'40'} size   Ukuran judul (48 = style "Title" di Figma)
 * @param {boolean} dark     Pakai warna putih (untuk section berlatar navy)
 */
export default function SectionHeader({
  eyebrow,
  title,
  lead,
  align = 'left',
  size = '48',
  dark = false,
  className = '',
  titleAs: TitleTag = 'h2',
}) {
  return (
    <div className={`section-header section-header--${align} ${dark ? 'section-header--dark' : ''} ${className}`}>
      {eyebrow && <p className="eyebrow grad-text">{eyebrow}</p>}
      <TitleTag className={`section-header__title section-header__title--${size}`}>{title}</TitleTag>
      {lead && <p className="section-header__lead">{lead}</p>}
    </div>
  );
}
