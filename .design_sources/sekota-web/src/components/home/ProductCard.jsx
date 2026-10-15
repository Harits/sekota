import { Link } from 'react-router-dom';
import './ProductCard.css';

/** Kartu produk di section "Intelligence Suite". Data dari src/data/products.js */
export default function ProductCard({ product }) {
  const { slug, name, category, accent, gradientDot, mark, arrow, summary, highlights } = product;

  return (
    <Link
      to={`/produk/${slug}`}
      className={`product-card ${gradientDot ? 'product-card--gradient-dot' : ''}`}
      style={{ '--accent': accent }}
    >
      <div className="product-card__top">
        <div>
          <p className="product-card__category">{category}</p>
          <h3 className="product-card__name">{name}</h3>
        </div>
        <img className="product-card__mark" src={mark} alt="" />
      </div>
      <p className="product-card__summary">{summary}</p>
      <ul className="product-card__list">
        {highlights.map((h) => (
          <li key={h}>{h}</li>
        ))}
      </ul>
      <span className="product-card__link">
        Pelajari Selengkapnya <img src={arrow} alt="" />
      </span>
    </Link>
  );
}
