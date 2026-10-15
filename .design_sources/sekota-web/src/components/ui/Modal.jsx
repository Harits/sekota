import { useEffect, useRef } from 'react';
import './Modal.css';

/**
 * Modal / overlay di tengah layar.
 * Tutup dengan tombol ✕, klik di luar kotak, atau tombol Escape.
 */
export default function Modal({ open, onClose, labelledBy, children }) {
  const closeRef = useRef(null);

  useEffect(() => {
    if (!open) return undefined;
    closeRef.current?.focus();
    const onKey = (e) => e.key === 'Escape' && onClose();
    document.addEventListener('keydown', onKey);
    return () => document.removeEventListener('keydown', onKey);
  }, [open, onClose]);

  if (!open) return null;

  return (
    <div
      className="modal-overlay"
      role="dialog"
      aria-modal="true"
      aria-labelledby={labelledBy}
      onClick={(e) => e.target === e.currentTarget && onClose()}
    >
      <div className="modal">
        <button ref={closeRef} className="modal__close" onClick={onClose} aria-label="Tutup">
          ✕
        </button>
        {children}
      </div>
    </div>
  );
}
