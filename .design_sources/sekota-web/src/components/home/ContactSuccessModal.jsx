import { Link, useNavigate } from 'react-router-dom';
import Modal from '@/components/ui/Modal';
import Button from '@/components/ui/Button';
import './ContactSuccessModal.css';

/** Overlay "Pesan Terkirim!" (frame "Overlay - Konfirmasi Kontak" di Figma). */
export default function ContactSuccessModal({ data, onClose }) {
  const navigate = useNavigate();

  return (
    <Modal open={!!data} onClose={onClose} labelledBy="contact-success-title">
      {data && (
        <>
          <div className="success__badge" aria-hidden="true">
            <svg width="36" height="36" viewBox="0 0 24 24" fill="none">
              <path d="M5 12.5l4.5 4.5L19 7.5" stroke="#fff" strokeWidth="2.6" strokeLinecap="round" strokeLinejoin="round" />
            </svg>
          </div>
          <h2 id="contact-success-title" className="success__title">Pesan Terkirim!</h2>
          <p className="success__text">
            Terima kasih, {data.firstName}. Tim strategis Sekota akan menghubungi Anda melalui {data.email} dalam 1×24 jam kerja.
          </p>
          <div className="success__ticket">
            <small>NOMOR TIKET</small>
            <b>{data.ticket}</b>
          </div>
          <Button
            className="success__home"
            onClick={() => {
              onClose();
              navigate('/#hero');
            }}
          >
            Kembali ke Beranda
          </Button>
          <Link to="/ebook" className="success__link" onClick={onClose}>
            Jelajahi E-Book Strategis →
          </Link>
        </>
      )}
    </Modal>
  );
}
