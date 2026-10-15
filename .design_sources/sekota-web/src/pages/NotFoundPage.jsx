import Button from '@/components/ui/Button';

/** Halaman 404 — muncul kalau URL tidak dikenali. */
export default function NotFoundPage() {
  return (
    <section className="section bg-soft">
      <div className="container" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 24, textAlign: 'center' }}>
        <p className="eyebrow grad-text">404</p>
        <h1 className="text-title">Halaman tidak ditemukan.</h1>
        <p className="text-lead">Alamat yang Anda buka tidak tersedia. Kembali ke beranda untuk melanjutkan.</p>
        <Button to="/">Kembali ke Beranda</Button>
      </div>
    </section>
  );
}
