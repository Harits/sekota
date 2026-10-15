import { Link } from 'react-router-dom';
import Button from '@/components/ui/Button';
import './ProductHero.css';

const BAR_HEIGHTS = [40, 64, 52, 88, 72, 104, 96];

/** Hero halaman produk: teks di kiri, kartu logo + mock dashboard di kanan. */
export default function ProductHero({ product }) {
  return (
    <section className="product-hero">
      <div className="container product-hero__grid">
        <div className="product-hero__text">
          <Link to="/#produk" className="back-link">← Kembali ke Intelligence Suite</Link>
          <p className="eyebrow grad-text">{product.category}</p>
          <h1 className="product-hero__title">{product.name}</h1>
          <p className="product-hero__tagline">{product.tagline}</p>
          <p className="product-hero__desc">{product.description}</p>
          <div className="product-hero__ctas">
            <Button to="/#kontak">Jadwalkan Demo →</Button>
            <Button to="/#kontak" variant="outline">Unduh Brosur</Button>
          </div>
        </div>

        <div className="product-hero__visual">
          <img src={product.logo} alt={`Logo ${product.name}`} />
          <div className="mock" aria-hidden="true">
            <div className="mock__kpis">
              {[0, 1, 2].map((i) => (
                <div key={i} className="mock__kpi">
                  <i />
                  <b />
                </div>
              ))}
            </div>
            <div className="mock__bars">
              {BAR_HEIGHTS.map((h, i) => (
                <i key={i} className={i === 5 ? 'is-high' : ''} style={{ height: h, animationDelay: `${i * 60}ms` }} />
              ))}
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
