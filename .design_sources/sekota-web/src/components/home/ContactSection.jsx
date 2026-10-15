import { useState } from 'react';
import SectionHeader from '@/components/ui/SectionHeader';
import Button from '@/components/ui/Button';
import ContactSuccessModal from './ContactSuccessModal';
import mapPin from '@/assets/icons/mappin.png';
import envelope from '@/assets/icons/envelope.png';
import globe from '@/assets/icons/globe.png';
import { contact } from '@/data/site';
import './ContactSection.css';

const EMPTY = { name: '', email: '', message: '' };

export default function ContactSection() {
  const [form, setForm] = useState(EMPTY);
  const [sent, setSent] = useState(null); // berisi data pengirim setelah submit

  const update = (field) => (e) => setForm((f) => ({ ...f, [field]: e.target.value }));

  const handleSubmit = (e) => {
    e.preventDefault();
    // TODO (developer): kirim `form` ke API/backend di sini, contoh:
    // await fetch('/api/contact', { method: 'POST', body: JSON.stringify(form) })
    setSent({
      firstName: (form.name.trim() || 'Budi Santoso').split(' ')[0],
      email: form.email.trim() || 'budi@instansi.go.id',
      ticket: `SKT-2026-10-${String(Math.floor(100 + Math.random() * 900)).padStart(4, '0')}`,
    });
    setForm(EMPTY);
  };

  return (
    <section className="section bg-soft" id="kontak">
      <div className="container contact__grid">
        <div className="contact__info">
          <SectionHeader
            title="Mulai Percakapan Strategis."
            lead="Tim kami akan merespons dalam 1×24 jam kerja untuk mendiskusikan kebutuhan Anda."
            className="contact__header"
          />
          <ul className="contact__list">
            <li className="contact__item contact__item--top">
              <span className="contact__icon"><img src={mapPin} alt="" /></span>
              <span>
                {contact.address[0]}
                <br />
                {contact.address[1]}
              </span>
            </li>
            <li className="contact__item">
              <span className="contact__icon"><img src={envelope} alt="" /></span>
              <span>{contact.email}</span>
            </li>
            <li className="contact__item">
              <span className="contact__icon"><img src={globe} alt="" /></span>
              <span>{contact.website}</span>
            </li>
          </ul>
        </div>

        <div className="contact__card">
          <form className="contact-form" onSubmit={handleSubmit} noValidate>
            <div className="contact-form__row">
              <div className="field">
                <label htmlFor="contact-name">Nama Lengkap</label>
                <input id="contact-name" type="text" placeholder="Budi Santoso" autoComplete="name" value={form.name} onChange={update('name')} />
              </div>
              <div className="field">
                <label htmlFor="contact-email">Email Bisnis</label>
                <input id="contact-email" type="email" placeholder="budi@instansi.go.id" autoComplete="email" value={form.email} onChange={update('email')} />
              </div>
            </div>
            <div className="field">
              <label htmlFor="contact-message">Pesan / Kebutuhan Strategis</label>
              <textarea id="contact-message" placeholder="Ceritakan tantangan Anda..." value={form.message} onChange={update('message')} />
            </div>
            <Button type="submit" className="contact-form__submit">
              Kirim &amp; Mulai Konsultasi
            </Button>
          </form>
        </div>
      </div>

      <ContactSuccessModal data={sent} onClose={() => setSent(null)} />
    </section>
  );
}
