# eBook Catalog Implementation Requirements

## 🏛️ Data Architecture & Governance
- **Data Source**: Fetches books from master inventory (`bookinteractiontool`) merged with local CMS enriched web metadata.
- **Dynamic Routing**: Clicking any book card passes its unique `id` to the parent navigation coordinator (`App.kt`), opening the corresponding dynamic `BookDetailsScreen`.

## 🧱 Component Breakdown

### 1. Header
- Title: "eBook Catalogue".
- Subtitle: Editorial description emphasizing strategic leadership and thought leadership instruments.

### 2. Sidebar Filter
- **Search**: Search filter input for book title or author.
- **Sort by**: Sorting selector (Newest, Rating, Alphabetical).
- **Genre / Category**: Multi-select tags (`SMART CITY`, `ESG`, `INTELLIGENCE`, `GOVERNANCE`).
- **CTA Cards**:
  - "Customize Solution" with "Contact Us" routing.
  - "Executive Presence" with "Get Merchandise" routing.

### 3. Grid View
- 3 columns on desktop.
- **ProductCard**:
  - Book Cover Image (custom base64 upload or dynamic brand palette background).
  - Rating (Dynamic Stars + Decimal rating, e.g., 4.9).
  - Title & Author.
  - Click Action: Passes `book.id` to route directly to `BookDetailsScreen`.

### 4. Pagination
- Numeric pagination (1, 2, 3 ... 10).

### 5. Verification & Previews
- Desktop Preview: `@Preview(device = DESKTOP)` wrapped in `MaterialTheme`.
