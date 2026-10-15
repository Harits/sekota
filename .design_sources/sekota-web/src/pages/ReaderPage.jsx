import { useState } from 'react';
import { Link } from 'react-router-dom';
import Stars from '@/components/ui/Stars';
import ChapterSidebar from '@/components/reader/ChapterSidebar';
import ReadingPane from '@/components/reader/ReadingPane';
import { bookDetail } from '@/data/catalog';
import { chapters, totalPages, sampleParagraphs, sampleQuote } from '@/data/reader';
import './ReaderPage.css';

/** Halaman baca e-book — frame "E-Book Reader" di Figma. */
export default function ReaderPage() {
  const [current, setCurrent] = useState(1); // mulai di Bab 1 seperti desain
  const chapter = chapters[current];
  const progress = Math.min(100, Math.round((chapter.page / totalPages) * 100) + (current === 1 ? 20 : 0));

  const select = (i) => {
    setCurrent(i);
    document.getElementById('reading-pane')?.scrollIntoView({ behavior: 'smooth', block: 'start' });
  };

  return (
    <div className="reader-page">
      <div className="reader-page__container">
        <Link to={`/ebook/${bookDetail.slug}`} className="back-link">← Kembali ke Detail Buku</Link>

        <header className="reader-page__head">
          <div className="reader-page__crumbs">
            <span>{bookDetail.breadcrumb[0]}</span>
            <span>/</span>
            <span>{bookDetail.breadcrumb[1]}</span>
          </div>
          <h1>{bookDetail.title}</h1>
          <p className="reader-page__author">by {bookDetail.author}</p>
          <Stars value={bookDetail.rating} size="lg" label={bookDetail.ratingLabel} />
        </header>

        <div className="reader-page__body">
          <ChapterSidebar chapters={chapters} current={current} onSelect={select} progress={progress} />
          <div id="reading-pane" className="reader-page__pane">
            <ReadingPane
              chapter={chapter}
              index={current}
              total={chapters.length}
              totalPages={totalPages}
              paragraphs={sampleParagraphs}
              quote={sampleQuote}
              onPrev={() => setCurrent((c) => Math.max(0, c - 1))}
              onNext={() => setCurrent((c) => Math.min(chapters.length - 1, c + 1))}
            />
          </div>
        </div>
      </div>
    </div>
  );
}
