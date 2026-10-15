import { useMemo, useState } from 'react';
import CatalogLayout, { SidebarBox, FilterGroup, CustomizePromo, CatalogHeader } from '@/components/catalog/CatalogLayout';
import ItemCard from '@/components/catalog/ItemCard';
import { SearchInput, Select, Checkbox } from '@/components/ui/FormControls';
import Pagination from '@/components/ui/Pagination';
import { merchandise, merchCategories } from '@/data/catalog';

const SORT_OPTIONS = [
  { value: 'newest', label: 'Newest' },
  { value: 'rating', label: 'Top Rated' },
];

/** Halaman merchandise — frame "Merchandise" di Figma. */
export default function MerchandisePage() {
  const [query, setQuery] = useState('');
  const [sort, setSort] = useState('newest');
  const [categories, setCategories] = useState([]);
  const [page, setPage] = useState(1);

  const result = useMemo(() => {
    const list = merchandise.filter(
      (m) => m.title.toLowerCase().includes(query.toLowerCase()) && (categories.length === 0 || categories.includes(m.category)),
    );
    return sort === 'rating' ? [...list].sort((a, b) => (b.score || 0) - (a.score || 0)) : list;
  }, [query, sort, categories]);

  const sidebar = (
    <>
      <SidebarBox>
        <SearchInput id="merch-search" value={query} onChange={setQuery} label="Cari merchandise" />
        <FilterGroup title="Sort by">
          <Select id="merch-sort-side" value={sort} onChange={setSort} options={SORT_OPTIONS} label="Urutkan" />
        </FilterGroup>
        <FilterGroup title="Category">
          <div className="filter-group__options">
            <Checkbox id="cat-all" checked={categories.length === 0} onChange={() => setCategories([])}>All</Checkbox>
            {merchCategories.map((c) => (
              <Checkbox
                key={c.id}
                id={`cat-${c.id}`}
                checked={categories.includes(c.id)}
                onChange={(on) => setCategories(on ? [...categories, c.id] : categories.filter((x) => x !== c.id))}
              >
                {c.label}
              </Checkbox>
            ))}
          </div>
        </FilterGroup>
      </SidebarBox>
      <CustomizePromo />
    </>
  );

  return (
    <CatalogLayout sidebar={sidebar}>
      <CatalogHeader
        title="Merchandise"
        descriptionWidth={617}
        description="Di Sekota, setiap instrumen adalah bagian dari strategi. Miliki koleksi eksklusif yang merepresentasikan nilai Trusted Intelligence dan Urban Sinergy di meja kerja atau aktivitas lapangan Anda."
        aside={<Select id="merch-sort" value={sort} onChange={setSort} options={SORT_OPTIONS} prefix="Sort :" label="Urutkan merchandise" />}
      />
      <div className="catalog__grid">
        {result.map((item) => (
          <ItemCard key={item.slug} item={item} to="/merchandise/black-totebag" />
        ))}
        {result.length === 0 && <p className="catalog__empty">Belum ada produk di kategori ini.</p>}
      </div>
      <Pagination page={page} onChange={setPage} />
    </CatalogLayout>
  );
}
