import prevIcon from '@/assets/icons/page-prev.png';
import nextIcon from '@/assets/icons/page-next.png';
import './Pagination.css';

/**
 * Pagination: 1 2 3 ... 10 dengan tombol panah.
 * @param {number} page      Halaman aktif
 * @param {number} total     Jumlah halaman
 * @param {function} onChange
 */
export default function Pagination({ page, total = 10, onChange }) {
  const pages = [1, 2, 3, '...', total];
  const go = (p) => onChange(Math.min(total, Math.max(1, p)));

  return (
    <nav className="pagination" aria-label="Pagination">
      <button className="pagination__arrow" onClick={() => go(page - 1)} aria-label="Halaman sebelumnya">
        <img src={prevIcon} alt="" />
      </button>
      <div className="pagination__nums">
        {pages.map((p, i) =>
          p === '...' ? (
            <span key={i}>...</span>
          ) : (
            <button
              key={i}
              className={p === page ? 'is-active' : ''}
              aria-current={p === page ? 'page' : undefined}
              onClick={() => go(p)}
            >
              {p}
            </button>
          ),
        )}
      </div>
      <button className="pagination__arrow" onClick={() => go(page + 1)} aria-label="Halaman berikutnya">
        <img src={nextIcon} alt="" />
      </button>
    </nav>
  );
}
