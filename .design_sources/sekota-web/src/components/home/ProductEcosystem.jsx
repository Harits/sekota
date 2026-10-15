import SectionHeader from '@/components/ui/SectionHeader';
import ProductCard from './ProductCard';
import { products } from '@/data/products';
import './ProductEcosystem.css';

export default function ProductEcosystem() {
  return (
    <section className="section bg-soft" id="produk">
      <div className="container">
        <div className="split-head">
          <SectionHeader eyebrow="Ekosistem Produk" title="Intelligence Suite." />
          <p className="split-head__side">Dirancang untuk saling terhubung dalam satu ekosistem data yang kohesif.</p>
        </div>
        <div className="product-grid">
          {products.map((p) => (
            <ProductCard key={p.slug} product={p} />
          ))}
        </div>
      </div>
    </section>
  );
}
