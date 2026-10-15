import { Link } from 'react-router-dom';
import './Button.css';

/**
 * Tombol utama Sekota.
 *
 * @param {'dark'|'outline'|'gradient'} variant  Gaya tombol (default: 'dark')
 * @param {'md'|'sm'} size                       Ukuran tombol
 * @param {string} to                            Kalau diisi, tombol jadi link (react-router)
 * @param {string} icon                          Path gambar ikon di kanan teks (opsional)
 * @param {boolean} gradientText                 Teks bergradien (dipakai di tombol outline "Lihat Katalog")
 *
 * Contoh:
 *   <Button to="/#kontak">Konsultasi Strategis</Button>
 *   <Button variant="outline" onClick={...}>Lihat Produk Kami</Button>
 */
export default function Button({
  variant = 'dark',
  size = 'md',
  to,
  icon,
  gradientText = false,
  className = '',
  children,
  ...props
}) {
  const classes = `btn btn--${variant} btn--${size} ${className}`.trim();
  const content = (
    <>
      <span className={gradientText ? 'grad-text' : undefined}>{children}</span>
      {icon && <img className="btn__icon" src={icon} alt="" />}
    </>
  );

  if (to) {
    return (
      <Link to={to} className={classes} {...props}>
        {content}
      </Link>
    );
  }
  return (
    <button type="button" className={classes} {...props}>
      {content}
    </button>
  );
}
