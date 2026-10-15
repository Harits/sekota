import SectionHeader from '@/components/ui/SectionHeader';
import './SolutionSection.css';

/* Urutan ini memang sebuah siklus (Strategic Value Loop), jadi nomor 01–05 bermakna. */
const steps = [
  { label: 'Intent', caption: 'Definisi Strategis' },
  { label: 'Execution', caption: 'Implementasi Data' },
  { label: 'Value', caption: 'Penciptaan Nilai' },
  { label: 'Measurement', caption: 'Audit Dampak' },
  { label: 'Learning', caption: 'Optimasi Berkelanjutan' },
];

export default function SolutionSection() {
  return (
    <section className="section bg-white" id="solusi">
      <div className="container">
        <SectionHeader
          align="center"
          eyebrow="Solusi Kami"
          title="Strategic Value Loop"
          lead="Setiap solusi Sekota dirancang dalam satu siklus tertutup yang memastikan data berubah menjadi keputusan strategis."
        />
        <div className="loop">
          <ol className="loop__grid">
            {steps.map((s, i) => (
              <li key={s.label} className="loop-card">
                <span className="loop-card__num">{String(i + 1).padStart(2, '0')}</span>
                <b>{s.label}</b>
                <small>{s.caption}</small>
              </li>
            ))}
          </ol>
        </div>
      </div>
    </section>
  );
}
