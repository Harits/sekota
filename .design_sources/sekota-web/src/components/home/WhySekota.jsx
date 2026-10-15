import Button from '@/components/ui/Button';
import icon1 from '@/assets/icons/why-1.png';
import icon2 from '@/assets/icons/why-2.png';
import './WhySekota.css';

const reasons = [
  { icon: icon1, w: 24, h: 14, title: 'Lebih Strategis dari Vendor IT', text: 'Memberikan insight yang dapat langsung ditindaklanjuti untuk kebijakan.' },
  { icon: icon2, w: 21, h: 21, title: 'Lebih Teknis dari Konsultan', text: 'Implementasi data real-time, bukan sekadar slide deck rekomendasi.' },
];

export default function WhySekota() {
  return (
    <section className="section why" id="mengapa">
      <div className="container why__grid">
        <div className="why__left">
          <p className="eyebrow grad-text">Mengapa Sekota</p>
          <div className="why__copy">
            <h2>Sekota Bukan Sekadar Vendor, Bukan Sekadar Konsultan.</h2>
            <p>Kami berada di persimpangan unik antara teknologi, konteks lokal, dan strategic foresight.</p>
          </div>
          <Button to="/#kontak" variant="gradient" className="why__cta">
            Mulai Konsultasi Strategis
          </Button>
        </div>
        <div className="why__cards">
          {reasons.map((r) => (
            <div key={r.title} className="why-card">
              <img src={r.icon} width={r.w} height={r.h} alt="" />
              <div>
                <b>{r.title}</b>
                <p>{r.text}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
