import SectionHeader from '@/components/ui/SectionHeader';
import './FeatureGrid.css';

function CheckIcon() {
  return (
    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path d="M5 12.5l4.5 4.5L19 7.5" stroke="currentColor" strokeWidth="2.4" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  );
}

/** Section "Fitur Utama" — 6 kartu fitur. */
export default function FeatureGrid({ features }) {
  return (
    <section className="section bg-white">
      <div className="container">
        <SectionHeader size="40" eyebrow="Fitur Utama" title="Semua yang dibutuhkan, dalam satu platform." />
        <div className="feature-grid">
          {features.map(([title, text]) => (
            <div key={title} className="feature-card">
              <span className="feature-card__icon">
                <CheckIcon />
              </span>
              <h3>{title}</h3>
              <p>{text}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
