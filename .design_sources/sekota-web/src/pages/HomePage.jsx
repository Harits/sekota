import Hero from '@/components/home/Hero';
import ProblemSection from '@/components/home/ProblemSection';
import SolutionSection from '@/components/home/SolutionSection';
import ProductEcosystem from '@/components/home/ProductEcosystem';
import EbookShowcase from '@/components/home/EbookShowcase';
import WhySekota from '@/components/home/WhySekota';
import ContactSection from '@/components/home/ContactSection';

/** Halaman utama — frame "Landing Page" di Figma. Urutan section sama dengan desain. */
export default function HomePage() {
  return (
    <>
      <Hero />
      <ProblemSection />
      <SolutionSection />
      <ProductEcosystem />
      <EbookShowcase />
      <WhySekota />
      <ContactSection />
    </>
  );
}
