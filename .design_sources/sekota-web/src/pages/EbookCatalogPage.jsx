import { useMemo, useState } from 'react';
import CatalogLayout, { SidebarBox, FilterGroup, CustomizePromo, MerchPromo, CatalogHeader } from '@/components/catalog/CatalogLayout';
import ItemCard from '@/components/catalog/ItemCard';
import { SearchInput, Select, Checkbox } from '@/components/ui/FormControls';
import Pagination from '@/components/ui/Pagination';
import { books, bookGenres, bookYears } from '@/data/catalog';

const SORT_OPTIONS = [
  { value: 'newest', label: 'Newest' },
  { value: 'rating', label: 'Top Rated' },
  { value: 'az', label: 'A–Z' },
];

/** Halaman katalog e-book — frame "eBook Catalog" di Figma. */
export default function EbookCatalogPage() {
  const [query, setQuery] = useState('');
  const [sort, setSort] = useState('newest');
  const [genres, setGenres] = useState([]); // [] artinya "All"
  const [years, setYears] = useState([]);
  const [page, setPage] = useState(1);

  const toggle = (list, setList, id, on) => setList(on ? [...list, id] : list.filter((x) => x !== id));

  const result = useMemo(() => {
    let list = books.filter(
      (b) =>
        b.title.toLowerCase().includes(query.toLowerCase()) &&
        (genres.length === 0 || genres.includes(b.genre)) &&
        (years.length === 0 || years.includes(b.year)),
    );
    if (sort === 'az') list = [...list].sort((a, b) => a.title.localeCompare(b.title));
    else if (sort === 'rating') list = [...list].sort((a, b) => (b.score || 0) - (a.score || 0));
    else list = [...list].sort((a, b) => a.order - b.order);
    return list;
  }, [query, sort, genres, years]);

  const sidebar = (
    <>
      <SidebarBox>
        <SearchInput id="ebook-search" value={query} onChange={setQuery} label="Cari e-book" />
        <FilterGroup title="Sort by">
          <Select id="ebook-sort" value={sort} onChange={setSort} options={SORT_OPTIONS} label="Urutkan" />
        </FilterGroup>
        <FilterGroup title="Genre">
          <div className="filter-group__options">
            <Checkbox id="genre-all" checked={genres.length === 0} onChange={() => setGenres([])}>All</Checkbox>
            {bookGenres.map((g) => (
              <Checkbox key={g.id} id={`genre-${g.id}`} checked={genres.includes(g.id)} onChange={(on) => toggle(genres, setGenres, g.id, on)}>
                {g.label}
              </Checkbox>
            ))}
          </div>
        </FilterGroup>
        <FilterGroup title="Year">
          <div className="filter-group__options">
            {bookYears.map((y) => (
              <Checkbox key={y} id={`year-${y}`} checked={years.includes(y)} onChange={(on) => toggle(years, setYears, y, on)}>
                {y}
              </Checkbox>
            ))}
          </div>
        </FilterGroup>
      </SidebarBox>
      <CustomizePromo />
      <MerchPromo />
    </>
  );

  return (
    <CatalogLayout sidebar={sidebar}>
      <CatalogHeader
        title="eBook Catalogue"
        description="Membawa semangat sinergi ke dalam genggaman. E-book katalog ini adalah kurasi instrumen taktis yang dirancang untuk memperkuat konektivitas tim dan mitra strategis Anda. Mari orkestrasi identitas profesional bersama Sekota."
      />
      <div className="catalog__grid">
        {result.map((book) => (
          <ItemCard key={book.slug} item={book} to="/ebook/blind-spot-radar" />
        ))}
        {result.length === 0 && <p className="catalog__empty">Tidak ada e-book yang cocok dengan filter ini.</p>}
      </div>
      <Pagination page={page} onChange={setPage} />
    </CatalogLayout>
  );
}
