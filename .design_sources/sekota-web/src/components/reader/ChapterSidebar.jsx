import './ChapterSidebar.css';

/** Daftar isi + progres baca di kiri halaman reader. */
export default function ChapterSidebar({ chapters, current, onSelect, progress }) {
  return (
    <nav className="chapter-sidebar" aria-label="Daftar isi">
      <p className="chapter-sidebar__eyebrow">Daftar Isi</p>
      <ol className="chapter-sidebar__list">
        {chapters.map((ch, i) => (
          <li key={ch.id}>
            <button
              type="button"
              className={i === current ? 'is-active' : ''}
              aria-current={i === current ? 'true' : undefined}
              onClick={() => onSelect(i)}
            >
              <i>{i + 1}</i>
              <span>
                {ch.label}
                {ch.label.startsWith('Bab') ? ' — ' : ': '}
                {ch.title}
              </span>
            </button>
          </li>
        ))}
      </ol>
      <div className="chapter-sidebar__progress">
        <div className="chapter-sidebar__progress-row">
          <span>PROGRES BACA</span>
          <b>{progress}%</b>
        </div>
        <div className="chapter-sidebar__track">
          <i style={{ width: `${progress}%` }} />
        </div>
      </div>
    </nav>
  );
}
