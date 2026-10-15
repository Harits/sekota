import { useState } from 'react';
import Button from '@/components/ui/Button';
import './ReadingPane.css';

/**
 * Area baca: toolbar (ukuran huruf, mode malam, tandai), isi bab, dan navigasi bab.
 */
export default function ReadingPane({ chapter, index, total, totalPages, paragraphs, quote, onPrev, onNext }) {
  const [fontSize, setFontSize] = useState(17);
  const [night, setNight] = useState(false);
  const [marked, setMarked] = useState(false);

  const chapterLabel = chapter.label.startsWith('Bab') ? `${chapter.label} dari 5` : chapter.label;

  return (
    <article className={`reading-pane ${night ? 'reading-pane--night' : ''}`} style={{ '--read-size': `${fontSize}px` }}>
      <div className="reading-pane__bar">
        <p className="reading-pane__eyebrow grad-text">{chapterLabel}</p>
        <div className="reading-pane__tools">
          <button type="button" onClick={() => setFontSize((s) => Math.max(14, s - 1))} aria-label="Perkecil teks">A−</button>
          <button type="button" onClick={() => setFontSize((s) => Math.min(22, s + 1))} aria-label="Perbesar teks">A+</button>
          <button type="button" aria-pressed={night} onClick={() => setNight((v) => !v)}>☾ Mode Malam</button>
          <button type="button" aria-pressed={marked} onClick={() => setMarked((v) => !v)}>{marked ? 'Ditandai ✓' : 'Tandai'}</button>
        </div>
      </div>

      <h2 className="reading-pane__title">{chapter.title}</h2>
      <p className="reading-pane__subtitle">{chapter.subtitle}</p>
      <hr />

      <div className="reading-pane__text">
        {paragraphs.map((p) => (
          <p key={p.slice(0, 24)}>{p}</p>
        ))}
      </div>
      <blockquote className="reading-pane__quote">{quote}</blockquote>

      <div className="reading-pane__pager">
        <Button variant="outline" onClick={onPrev} disabled={index === 0}>← Bab Sebelumnya</Button>
        <span>
          Halaman {chapter.page} / {totalPages}
        </span>
        <Button onClick={onNext} disabled={index === total - 1}>Bab Berikutnya →</Button>
      </div>
    </article>
  );
}
