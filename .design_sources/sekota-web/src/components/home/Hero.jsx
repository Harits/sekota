import Button from '@/components/ui/Button';
import arrowWhite from '@/assets/icons/arrow-white.png';
import './Hero.css';

export default function Hero() {
  return (
    <section className="hero" id="hero">
      <div className="hero__blur" aria-hidden="true" />
      <div className="container hero__container">
        <div className="hero__grid">
          <div className="hero__text">
            <h1 className="hero__title">
              <span>Trusted</span>
              <span className="grad-text">Intelligence.</span>
            </h1>
            <p className="hero__lead">
              Memberdayakan organisasi melalui intelligence berbasis data terbuka dan AI yang bertata kelola untuk
              keputusan strategis yang berkelanjutan.
            </p>
            <div className="hero__ctas">
              <Button to="/#kontak" icon={arrowWhite}>
                Konsultasi Strategis
              </Button>
              <Button to="/#produk" variant="outline">
                Lihat Produk Kami
              </Button>
            </div>
          </div>

          {/* Kartu metrik melayang ("Hero Floating UI Rhythm" di Figma) */}
          <div className="hero__float" aria-label="Ringkasan metrik">
            <div className="hero__ring" aria-hidden="true" />
            <div className="metric-card">
              <div className="metric-card__head">
                <span>Live Metrics</span>
                <div className="metric-card__dots" aria-hidden="true">
                  <i />
                  <i />
                </div>
              </div>
              <div>
                <div className="metric-card__row">
                  <span>Data Accuracy</span>
                  <span className="grad-text">99.8%</span>
                </div>
                <div className="metric-card__bar">
                  <i style={{ width: '99.8%' }} />
                </div>
                <div className="metric-card__stats">
                  <div>
                    <b>100+</b>
                    <small>Klien &amp; Mitra</small>
                  </div>
                  <div>
                    <b>2021</b>
                    <small>Tahun Berdiri</small>
                  </div>
                </div>
              </div>
            </div>
            <div className="hero__badge">
              <b>4</b>
              <small>
                Intelligence Suite
                <br />
                Products
              </small>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
