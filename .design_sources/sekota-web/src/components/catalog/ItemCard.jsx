import { Link } from 'react-router-dom';
import Stars from '@/components/ui/Stars';
import './ItemCard.css';

/**
 * Kartu produk katalog (dipakai untuk e-book dan merchandise).
 * @param {object} item  { title, author, image, rating, score }
 * @param {string} to    Tujuan saat kartu diklik
 */
export default function ItemCard({ item, to }) {
  return (
    <Link to={to} className="item-card">
      <div className="item-card__image">
        <img src={item.image} alt={item.title} loading="lazy" />
      </div>
      <div className="item-card__body">
        <Stars value={item.rating} label={item.score ? `(${item.score})` : undefined} />
        <h3 className="item-card__title">{item.title}</h3>
        <p className="item-card__author">{item.author}</p>
        <span className="item-card__button">
          <span className="grad-text">View Details</span>
        </span>
      </div>
    </Link>
  );
}
