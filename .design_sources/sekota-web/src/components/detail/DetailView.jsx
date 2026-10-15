import { useState } from 'react';
import { Link } from 'react-router-dom';
import Stars from '@/components/ui/Stars';
import inside1 from '@/assets/icons/inside-1.png';
import inside2 from '@/assets/icons/inside-2.png';
import inside3 from '@/assets/icons/inside-3.png';
import inside4 from '@/assets/icons/inside-4.png';
import './DetailView.css';

const INSIDE_ICONS = [inside1, inside2, inside3, inside4];

/**
 * Tampilan detail (dipakai untuk Book Details dan Merchandise Details).
 *
 * @param {object} data       Lihat bookDetail / merchDetail di src/data/catalog.js
 * @param {object} primary    { label, to } tombol utama
 * @param {object} secondary  { label, savedLabel } tombol toggle sekunder
 */
export default function DetailView({ data, primary, secondary }) {
  const [saved, setSaved] = useState(false);

  return (
    <div className="detail">
      <div className="detail__container">
        {/* ---------- Hero ---------- */}
        <div className="detail__hero">
          <div className="detail__cover">
            <img src={data.cover} alt={data.title} />
          </div>
          <div className="detail__info">
            <div className="detail__crumbs">
              {data.breadcrumb.map((c, i) => (
                <span key={c}>
                  {i > 0 && <span className="detail__crumb-sep">/</span>}
                  {c}
                </span>
              ))}
            </div>
            <h1 className="detail__title">{data.title}</h1>
            <p className="detail__author">{data.author.startsWith('“') ? data.author : `by ${data.author}`}</p>
            <div className="detail__rating">
              <Stars value={data.rating} size="lg" label={data.ratingLabel} />
            </div>
            <div className="detail__desc">
              {data.description.map((p) => (
                <p key={p.slice(0, 20)}>{p}</p>
              ))}
            </div>
            <div className="detail__actions">
              <Link to={primary.to} className="detail__btn detail__btn--primary">
                {primary.label}
              </Link>
              <button
                type="button"
                className={`detail__btn detail__btn--secondary ${saved ? 'is-saved' : ''}`}
                aria-pressed={saved}
                onClick={() => setSaved((v) => !v)}
              >
                {saved ? secondary.savedLabel : secondary.label}
              </button>
            </div>
          </div>
        </div>

        {/* ---------- Bento ---------- */}
        <div className="detail__bento">
          <div className="detail__inside">
            <h3 className="detail__h3">{data.insideTitle}</h3>
            <div className="detail__inside-grid">
              {data.inside.map(([title, text], i) => (
                <div key={title} className="detail__inside-item">
                  <img src={INSIDE_ICONS[i % 4]} alt="" />
                  <div>
                    <h4 className="detail__h3">{title}</h4>
                    <p>{text}</p>
                  </div>
                </div>
              ))}
            </div>
            <div className="detail__progress">
              <div className="detail__progress-row">
                <span>{data.progress.label}</span>
                <span className="grad-text">{data.progress.value}</span>
              </div>
              <div className="detail__track">
                <i style={{ width: `${data.progress.percent}%` }} />
              </div>
            </div>
          </div>

          <dl className="detail__specs">
            {data.specs.map(([label, value]) => (
              <div key={label}>
                <dt>{label}</dt>
                <dd>{value}</dd>
              </div>
            ))}
          </dl>
        </div>
      </div>
    </div>
  );
}
