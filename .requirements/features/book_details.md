# eBook Details Implementation Requirements

## 🏛️ Data Architecture & Governance
- **Master Inventory Source (`bookinteractiontool`)**:
  - Authoritative repository providing canonical `id`, `title`, `author`, `isbn`, and base cover image via `/api/v1/admin/books`.
- **Sekota CMS Web Metadata Enrichment**:
  - Backoffice operators manage web-specific presentation attributes (`category`, `description`, `rating`, `ratingCount`, `pdfUrl`, `readingTime`, `pages`, `coverImage`).
- **Dynamic Web Presentation**:
  - `BookDetailsScreen` receives `bookId: String?` selected from `CatalogScreen` (defaulting to canonical or first book), resolving real attributes via `GetBookByIdUseCase`.

## 🧱 Component Breakdown

### 1. Hero / Info Section
- **Left**: Dynamic Book Cover image (custom upload or gradient placeholder with bold title).
- **Right**:
  - Dynamic Category Eyebrow badge (e.g., `SMART CITY`, `ESG`, `INTELLIGENCE`, `GOVERNANCE`).
  - Title: Dynamic `book.title`.
  - Author: Dynamic `by ${book.author}`.
  - Reader Rating: Dynamic star icons (`RatingStars`) + `(${book.rating}/5 from ${book.ratingCount} readers)`.
  - Synopsis / Description paragraph: Dynamic `book.description`.
  - Digital Edition Badge: Indicates high-res PDF manuscript availability when `pdfUrl` is set.
  - Action Buttons:
    - **Read Now / Unduh PDF**: Bound to `book.pdfUrl`. Protected by inline modal `AuthGateDialog`.
    - **Add to Library**: Adds book to reader's personal library state.

### 2. "What's Inside" Section
- Heading: "What's Inside".
- 4 Cards with tactical thought leadership highlights:
  - The Attention Economy
  - Deep Work Protocols
  - Mindful Tech Integration
  - Cognitive Recovery
- Reading Time Indicator: Displays dynamic `book.readingTime` (e.g. `3H 45M`) with a stylized progress bar.

### 3. Metadata Sidebar / Card
- Published: "Nov 2025".
- Pages: Dynamic `${book.pages} Hal`.
- Language: "Indonesia".
- Format: Dynamic `eBook, PDF` (if `pdfUrl` is available) or `eBook`.

### 4. Verification & Previews
- Desktop Preview: `@Preview(device = DESKTOP)` wrapped in `MaterialTheme`.
