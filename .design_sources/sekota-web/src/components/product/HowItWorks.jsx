import SectionHeader from '@/components/ui/SectionHeader';
import Button from '@/components/ui/Button';
import './HowItWorks.css';

const STEPS = [
  { title: 'Integrasi', text: 'Hubungkan sumber data dan sistem yang sudah ada tanpa mengganggu operasional.' },
  { title: 'Analisis', text: 'Data diolah, divalidasi, dan dianalisis dengan model yang transparan.' },
  { title: 'Aksi', text: 'Insight disajikan sebagai rekomendasi yang siap ditindaklanjuti pimpinan.' },
];

const AUDIENCE = ['Pemerintah Daerah', 'BUMN', 'Korporasi', 'Lembaga & NGO'];

/** Section "Cara Kerja" + segmen "Cocok untuk" + banner ajakan konsultasi. */
export default function HowItWorks({ productName }) {
  return (
    <section className="section bg-page">
      <div className="container">
        <SectionHeader size="40" eyebrow="Cara Kerja" title="Dari data mentah menjadi keputusan." />

        <ol className="steps">
          {STEPS.map((s, i) => (
            <li key={s.title} className="step">
              <span className="step__num grad-text">{String(i + 1).padStart(2, '0')}</span>
              <h3>{s.title}</h3>
              <p>{s.text}</p>
            </li>
          ))}
        </ol>

        <div className="audience">
          <b>Cocok untuk:</b>
          {AUDIENCE.map((a) => (
            <span key={a} className="chip">{a}</span>
          ))}
        </div>

        <div className="cta-band">
          <div>
            <h2>Siap melihat {productName} bekerja untuk Anda?</h2>
            <p>Diskusikan kebutuhan Anda bersama tim strategis Sekota.</p>
          </div>
          <Button to="/#kontak" variant="gradient">Mulai Konsultasi Strategis</Button>
        </div>
      </div>
    </section>
  );
}
