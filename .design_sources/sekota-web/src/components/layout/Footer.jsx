import { Link } from 'react-router-dom';
import logo from '@/assets/logos/logo-sekota.png';
import ebdeskIcon from '@/assets/icons/ebdesk.png';
import { footerColumns, socialLinks } from '@/data/site';
import './Footer.css';

export default function Footer() {
  return (
    <footer className="footer">
      <div className="container">
        <div className="footer__top">
          <div className="footer__brand">
            <img className="footer__logo" src={logo} alt="Sekota Sinergi Indonesia" />
            <p className="footer__about">
              Trusted Intelligence for Sustainable Impact. Memberdayakan keputusan strategis melalui data.
            </p>
            <div className="footer__affil">
              <img src={ebdeskIcon} alt="" />
              <div>
                <b>eBdesk Group Affiliation</b>
                <small>Nationwide Partner</small>
              </div>
            </div>
            <div className="footer__social">
              {socialLinks.map((s) => (
                <a key={s.label} href={s.href} target="_blank" rel="noopener noreferrer">
                  {s.label}
                </a>
              ))}
            </div>
          </div>

          <div className="footer__cols">
            {footerColumns.map((col) => (
              <div key={col.title}>
                <h5>{col.title}</h5>
                <ul>
                  {col.links.map((l) => (
                    <li key={l.label}>{l.to ? <Link to={l.to}>{l.label}</Link> : <span>{l.label}</span>}</li>
                  ))}
                </ul>
              </div>
            ))}
          </div>
        </div>

        <div className="footer__bottom">
          <span>© 2025 PT. Sekota Sinergi Indonesia. Bagian dari eBdesk Group.</span>
          <div className="footer__langs">
            <span>English</span>
            <span className="is-active">Bahasa Indonesia</span>
          </div>
        </div>
      </div>
    </footer>
  );
}
