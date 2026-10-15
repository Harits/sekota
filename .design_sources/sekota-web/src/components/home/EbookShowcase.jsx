import { useState } from 'react';
import { Link } from 'react-router-dom';
import SectionHeader from '@/components/ui/SectionHeader';
import Button from '@/components/ui/Button';
import caretRight from '@/assets/icons/caret-right.png';
import { featuredEbooks } from '@/data/catalog';
import './EbookShowcase.css';

const FILTERS = [
  { id: 'all', label: 'Semua' },
  { id: 'smartcity', label: 'Smart City' },
  { id: 'esg', label: 'ESG' },
];

export default function EbookShowcase() {
  const [filter, setFilter] = useState('all');
  const list = featuredEbooks.filter((b) => filter === 'all' || b.category === filter);

  return (
    <section className="section bg-white">
      <div className="container">
        <div className="split-head">
          <SectionHeader eyebrow="Perpustakaan Digital" title="E-Book & Panduan Strategis." className="ebook-showcase__header" />
          <div className="pills" role="tablist" aria-label="Filter e-book">
            {FILTERS.map((f) => (
              <button
                key={f.id}
                role="tab"
                aria-selected={filter === f.id}
                className={filter === f.id ? 'is-active' : ''}
                onClick={() => setFilter(f.id)}
              >
                {f.label}
              </button>
            ))}
          </div>
        </div>

        <div className="ebook-grid">
          {list.map((book) => (
            <Link key={book.title} to="/ebook/blind-spot-radar" className="ebook-card">
              <div className="ebook-card__top" style={{ background: book.gradient }}>
                <span className="ebook-card__tag">{book.tag}</span>
                {book.title}
              </div>
              <div className="ebook-card__body">
                <p>{book.excerpt}</p>
                <span className="ebook-card__more">
                  Lihat Detail <img src={caretRight} alt="" />
                </span>
              </div>
            </Link>
          ))}
          {list.length === 0 && (
            <p className="ebook-grid__empty">E-book kategori ESG sedang disiapkan. Lihat katalog untuk judul lainnya.</p>
          )}
        </div>

        <div className="ebook-showcase__cta">
          <Button to="/ebook" variant="outline" gradientText>
            Lihat Katalog
          </Button>
        </div>
      </div>
    </section>
  );
}
