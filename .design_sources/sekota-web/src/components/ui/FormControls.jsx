import searchIcon from '@/assets/icons/search.png';
import caretDown from '@/assets/icons/caret-down.png';
import './FormControls.css';

/** Input pencarian abu-abu dengan ikon kaca pembesar. */
export function SearchInput({ id, value, onChange, placeholder = 'Search..', label = 'Cari' }) {
  return (
    <label className="search-input" htmlFor={id}>
      <img src={searchIcon} alt="" />
      <input
        id={id}
        type="search"
        value={value}
        placeholder={placeholder}
        aria-label={label}
        onChange={(e) => onChange(e.target.value)}
      />
    </label>
  );
}

/** Dropdown dengan ikon caret. options: [{ value, label }] */
export function Select({ id, value, onChange, options, prefix, label = 'Pilih' }) {
  return (
    <div className={`select ${prefix ? 'select--prefix' : ''}`}>
      {prefix && <span className="select__prefix">{prefix}</span>}
      <select id={id} value={value} aria-label={label} onChange={(e) => onChange(e.target.value)}>
        {options.map((o) => (
          <option key={o.value} value={o.value}>
            {o.label}
          </option>
        ))}
      </select>
      <img src={caretDown} alt="" />
    </div>
  );
}

/** Checkbox bergaya Sekota (centang gradien). */
export function Checkbox({ id, checked, onChange, children }) {
  return (
    <label className="checkbox" htmlFor={id}>
      <input id={id} type="checkbox" checked={checked} onChange={(e) => onChange(e.target.checked)} />
      <i aria-hidden="true" />
      {children}
    </label>
  );
}
