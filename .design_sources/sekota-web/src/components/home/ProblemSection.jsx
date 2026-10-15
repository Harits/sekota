import SectionHeader from '@/components/ui/SectionHeader';
import icon1 from '@/assets/icons/problem-1.png';
import icon2 from '@/assets/icons/problem-2.png';
import icon3 from '@/assets/icons/problem-3.png';
import './ProblemSection.css';

const problems = [
  {
    icon: icon1,
    tone: 'danger',
    title: 'Data Tidak Terintegrasi',
    text: 'Silo data antar departemen dan sistem yang tidak terhubung menghasilkan insight yang fragmentaris dan tidak dapat diandalkan untuk keputusan level eksekutif.',
  },
  {
    icon: icon2,
    tone: 'warning',
    title: 'AI Tanpa Transparansi',
    text: 'Penggunaan AI yang tidak dapat dijelaskan (black-box) memicu krisis kepercayaan internal dan eksternal, serta risiko kepatuhan regulasi yang semakin ketat.',
  },
  {
    icon: icon3,
    tone: 'info',
    title: 'ESG Hanya Beban Administratif',
    text: 'Pelaporan ESG yang terpisah dari operasional bisnis menjadikannya beban birokrasi, bukan kompas strategis yang mendorong nilai jangka panjang.',
  },
];

export default function ProblemSection() {
  return (
    <section className="section bg-soft problem">
      <div className="container">
        <SectionHeader
          eyebrow="Tantangan 2026"
          title="Data Melimpah. Keputusan Masih Kabur."
          lead="Di tahun 2026, organisasi modern menghadapi paradoks data, volume informasi yang belum pernah sebesar ini, namun kepercayaan terhadap data justru semakin menipis."
          className="problem__header"
        />
        <div className="problem__grid">
          {problems.map((p) => (
            <article key={p.title} className="problem-card">
              <div className={`problem-card__icon problem-card__icon--${p.tone}`}>
                <img src={p.icon} alt="" />
              </div>
              <h3>{p.title}</h3>
              <p>{p.text}</p>
            </article>
          ))}
        </div>
      </div>
    </section>
  );
}
