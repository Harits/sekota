import starSm from '@/assets/icons/star-sm.png';
import starSmEmpty from '@/assets/icons/star-sm-empty.png';
import starLg from '@/assets/icons/star-lg.png';
import starLgEmpty from '@/assets/icons/star-lg-empty.png';
import './Stars.css';

/**
 * Rating bintang (ikon dari Figma).
 * @param {number} value  Jumlah bintang penuh (0–5)
 * @param {'sm'|'lg'} size
 * @param {string} label  Teks di kanan bintang, contoh "(4.8)"
 */
export default function Stars({ value = 4, size = 'sm', label }) {
  const full = size === 'lg' ? starLg : starSm;
  const empty = size === 'lg' ? starLgEmpty : starSmEmpty;

  return (
    <div className={`stars stars--${size}`}>
      <span className="stars__icons" role="img" aria-label={`${value} dari 5 bintang`}>
        {Array.from({ length: 5 }, (_, i) => (
          <img key={i} src={i < value ? full : empty} alt="" />
        ))}
      </span>
      {label && <span className="stars__label">{label}</span>}
    </div>
  );
}
