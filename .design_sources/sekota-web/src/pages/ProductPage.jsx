import { useParams } from 'react-router-dom';
import ProductHero from '@/components/product/ProductHero';
import FeatureGrid from '@/components/product/FeatureGrid';
import HowItWorks from '@/components/product/HowItWorks';
import NotFoundPage from './NotFoundPage';
import { getProduct } from '@/data/products';

/**
 * Halaman detail produk — frame "Product - Veridia / Ascendio / Sociara / Ecoflow" di Figma.
 * Satu komponen dipakai untuk keempat produk; isinya diambil dari src/data/products.js berdasarkan URL.
 */
export default function ProductPage() {
  const { slug } = useParams();
  const product = getProduct(slug);

  if (!product) return <NotFoundPage />;

  return (
    <>
      <ProductHero product={product} />
      <FeatureGrid features={product.features} />
      <HowItWorks productName={product.name} />
    </>
  );
}
