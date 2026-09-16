# Sekota — Landing Page Design Spec

> Source: `Sekota Landing Page.fig` → `/Page-1/Landing-Page` (node `15:1465`)
> Canvas: **1280 × 6516 px**, single column, top-aligned. Indonesian copy.
> One-line read: a calm, editorial B2B/B2G site for a data + AI intelligence consultancy ("Trusted Intelligence"), built on a near-white surface with a teal→green brand gradient and a single deep-navy ink.

---

## 1. Brand & voice

- **Name:** Sekota (logo lockup is a wordmark + gradient mark, 133 × 42 px in the nav).
- **Tagline / hero:** *Trusted Intelligence.* (Montserrat ExtraBold, 88 px, two lines, letter-spacing −3 px.)
- **Positioning:** "Memberdayakan organisasi melalui intelligence berbasis data terbuka dan AI yang bertata kelola untuk keputusan strategis yang berkelanjutan."
- **Tone:** authoritative, restrained, government/enterprise-facing. Mixes Indonesian body copy with English strategic vocabulary ("Intelligence Suite", "Strategic Value Loop", "Social License to Operate").
- **Affiliation badge in footer:** *eBdesk Group Affiliation · NATIONWIDE PARTNER.*

---

## 2. Color system

| Role | Token | Value | Notes |
|---|---|---|---|
| Ink / primary text | `--ink` | `rgb(13, 31, 45)` | Almost-black navy. Used for headlines, primary buttons, dark "Why" section bg. |
| Ink secondary | `--ink-2` | `rgb(20, 50, 68)` | Body text base; usually rendered at 40–60 % opacity for paragraph copy. |
| Surface | `--surface` | `rgb(255, 255, 255)` | Hero, Solution, E-Book and form card. |
| Surface alt | `--surface-alt` | `rgb(247, 250, 251)` | Problem, Product, Contact sections — gives the page its alternating rhythm. |
| Brand teal | `--brand-teal` | `rgb(0, 181, 200)` | Eyebrow labels, accent text, "Mengapa Sekota" CTA. |
| Brand teal alt | — | `rgb(2, 182, 207)` | End of the brand gradient (essentially the same teal). |
| Brand green | `--brand-green` | `rgb(96, 189, 101)` | Start of the brand gradient. |
| Brand gradient | `--brand-grad` | `linear-gradient(rgb(96,189,101) 0%, rgb(2,182,207) 100%)` | Logo mark, floating "4 / Intelligence Suite Products" pill in hero, accents on product cards. |
| Hairline | `--hairline` | `rgba(13, 31, 45, 0.05)` → `0.1` | Card borders, dividers, nav border. |
| Glass | — | `rgba(255, 255, 255, 0.8)` + `backdrop-filter: blur(20px)` | Sticky top nav. |
| Shadow | — | `0 25px 50px -12px rgba(0,0,0,0.15)` | Hero floating card. Otherwise shadows are barely visible (`0 1px 2px rgba(0,0,0,0.05)`). |

The Problem-card category accents introduce four mutually-coordinated hues (each appears once, as a 11 px uppercase eyebrow on a product card):
`rgb(53,161,144)` teal-green · `rgb(76,117,186)` slate-blue · `rgb(24,125,97)` forest · `rgb(55,138,221)` azure.

Opacity is the workhorse: instead of new greys, body copy is `rgba(20,50,68, 0.4–0.6)` on light surfaces and `rgba(255,255,255, 0.4–0.5)` on the dark "Why" section.

---

## 3. Typography

Three families, used with very specific roles:

| Family | Weights used | Role |
|---|---|---|
| **Montserrat** | ExtraBold (88, 48, 36 px), Bold (48, 24 px), Regular (16 px) | Display only — the hero H1 and the section H2s. |
| **DM Sans** | Bold (10, 11, 12, 14, 16, 20, 24, 30 px), Medium (14 px), Light (20 px), Regular (14, 16 px) | UI workhorse: nav, buttons, eyebrows, stat numbers, body. |
| **Inter** | Bold (12, 16, 20 px), Regular (14, 16 px), SemiBold (16 px) | Used inside the floating product/dashboard mockups for that "Figma-y dashboard UI" feel. |

Recurring text styles to standardise:

- **Hero H1** — Montserrat 700 / 88 px / line-height 88 / letter-spacing −3 px, `--ink`.
- **Section H2** — Montserrat ExtraBold 48 px (one line) or 36 px (two lines), tight tracking, `--ink` (white on the dark section).
- **Eyebrow label** — DM Sans Bold 14 / 16.5 / tracking +2.2 px, `--brand-teal`, ALL CAPS (e.g. `EKOSISTEM PRODUK`, `MENGAPA SEKOTA`, `PERPUSTAKAAN DIGITAL`).
- **Lead paragraph** — DM Sans Light 20 / 34, `rgba(20,50,68,0.6)`.
- **Body** — DM Sans Regular 16 / 24, `rgba(20,50,68,0.4–0.5)`.
- **Small caps stat label** — DM Sans Bold 10 / 15, tracking +0.5–1 px, `rgba(20,50,68,0.5)`.
- **Button label** — DM Sans Bold 14 or 16, line-height 20/24, never a system font.

---

## 4. Layout grid

- **Frame width:** 1280 px (desktop only in this artwork).
- **Section padding:** 80 px horizontal, 96 px (hero) or 120 px (everything else) vertical.
- **Content max-width:** 1120 px (the 1280 − 2×80 column).
- **Section gutter:** 32 px between row groups inside a section.
- **Card gap:** 32 px (problem cards), product cards are paired 540 px wide on a 2-col grid.
- **Sections alternate background** white → `--surface-alt` → white → `--surface-alt` → `--ink` (dark) → `--surface-alt` — giving the page its banded rhythm.

---

## 5. Page architecture

Top to bottom, with exact y-positions (px) and intent.

### 5.1 Nav — sticky top (0 → 80, height 80)
Glass surface (white @ 0.8 + 20 px blur) with a hairline bottom border. 80 px tall.
- **Left** (80 px from edge): Sekota wordmark + gradient mark (133 × 42).
- **Center**: nav links — *Produk · E-Book · Mengapa Sekota · Kontak* — DM Sans Medium 14, 40 px gap.
- **Right** (80 px from edge): primary pill button **"Konsultasi Strategis"** — `--ink` background, white DM Sans Bold 14, 40 px tall, fully rounded (border-radius 9999).

### 5.2 Hero — "Trusted Intelligence." (0 → 764, white)
- 96 px top/bottom padding. Background has a soft 600 × 600 `rgba(0,181,200,0.05)` blur disc in the top-right and a 50 % `--surface-alt` overlay on the right half (subtle vertical split).
- **Left column** (~ 633 wide):
  - H1 "Trusted\nIntelligence." (Montserrat ExtraBold 88).
  - Lead paragraph (DM Sans Light 20, 3 lines).
  - Two pill CTAs: **"Konsultasi Strategis →"** (filled `--ink`, white text, 66 px tall, 40 px h-pad, 12 px gap to arrow icon) + **"Lihat Produk Kami"** (outline pill, 1 px `--hairline`).
- **Right column — Hero floating UI cluster** at top-right:
  - A 389 × 238 white card, radius 24, 32 px pad, big drop-shadow. Inside: a `LIVE METRICS · ● ●` row, a *Data Accuracy 99.8 %* progress bar in `--brand-teal`, and a 2-up stat grid divided by a hairline (`100+` KLIEN & MITRA / `2021` TAHUN BERDIRI).
  - A 192 × 118 gradient pill anchored at bottom-left of the card (offset −69, +198): big white `4` over `INTELLIGENCE SUITE PRODUCTS` micro-label, radius 24, the brand green→teal gradient.
  - A small 84 × 84 white-bordered ring at top-left of the card (offset −42, −42) — a placeholder for a circular avatar/icon.

### 5.3 Problem section — "Data Melimpah. Keputusan Masih Kabur." (764 → 1639, `--surface-alt`)
- Eyebrow `TANTANGAN 2026`, then H2 (two lines) + a one-paragraph lead, left-aligned, full-width.
- Three problem cards in a 3-column grid (gap 32), each ~ 354 × 394, white, rounded, hairline border. Card contents:
  1. *Data Melimpah. Keputusan Masih Kabur.* (default)
  2. **AI Tanpa Transparansi** — "Penggunaan AI yang tidak dapat dijelaskan (black-box) memicu krisis kepercayaan internal dan eksternal, serta risiko kepatuhan regulasi yang semakin ketat."
  3. **ESG Hanya Beban Administratif** — "Pelaporan ESG yang terpisah dari operasional bisnis menjadikannya beban birokrasi, bukan kompas strategis yang mendorong nilai jangka panjang."

### 5.4 Solution — "Strategic Value Loop" (1639 → 2251, white)
- Centered eyebrow `SOLUSI KAMI`, H2 "Strategic Value Loop", two-line lead.
- A horizontal **5-step process strip** under a 1 px hairline divider — each step is a small rounded badge with a number `01–05` and a label:
  - 01 *(default)* · **02 Implementasi Data** · **03 Penciptaan Nilai** · **04 Audit Dampak** · **05 Optimasi Berkelanjutan**.
- The numbers sit on a faint surface chip; titles are DM Sans Bold ink.

### 5.5 Product ecosystem — "Intelligence Suite." (2251 → 3641, `--surface-alt`)
- Header row: eyebrow `EKOSISTEM PRODUK` (teal) + Montserrat H2 "Intelligence Suite." on the left, a 2-line tagline on the right ("Dirancang untuk saling terhubung dalam satu ekosistem data yang kohesif.").
- **2 × 2 grid of product cards** (540 × 475, white, radius 32, 48 px pad, hairline border, very faint shadow):
  1. **VERIDIA** — `BI DASHBOARD` *(teal-green eyebrow)* — flagship analytics platform.
  2. **ASCENDIO** — `COMMUNITY` — Capacity & Competency Assessment · Community Engagement Tracking · Field Activity Reporting.
  3. **SOCIARA** — `SOCIAL` — Social License to Operate (SLO) Monitoring · CSR Impact Measurement & Attribution · Stakeholder Perception Mapping.
  4. **ECOFLOW** — `SUSTAINABILITY` — Automated ESG Audit Readiness · Carbon Footprint Calculator (MRV) · Compliance Status Monitoring.
- Each card: small uppercase product-category eyebrow (11 px, +1.1 px tracking, one of the four accent hues) → product name H3 → paragraph description → 3-item check-list of features.

### 5.6 E-Book catalog — "E-Book & Panduan Strategis." (3641 → 4546, white)
- Eyebrow `PERPUSTAKAAN DIGITAL`, H2 left.
- Right-aligned **filter pill group**: an `rgba(13,31,45,0.03)` track with a segmented pill control — *(default tab)* · `Smart City` · `ESG`. Each pill is 40 px tall, rounded.
- **3-card editorial grid** beneath (~ 343 px tall):
  1. *(default cover — illustrative tile)*
  2. **Strategi Pemilu Berbasis Data Analytics** — category `POLITIK` — "Panduan untuk caleg dan tim sukses memanfaatkan…"
  3. **Implementasi Satu Data Kab/Kota** — category `DATA` — "Langkah teknis membangun datawarehouse lintas OPD…"
  4. **OSINT & Data Intelligence Modern** — category `INTELLIGENCE` — "Metodologi Open Source Intelligence untuk…"
  - Each card: top image area with a category chip overlay (uppercase 12 px), then a 3-line title (Montserrat Bold, ~ 22–24 px), a 2-line excerpt.
- Centered outline pill **"Lihat Katalog"** below the grid (184 × 66).

### 5.7 Why Sekota — dark interlude (4546 → 5143, `rgb(13,31,45)` ink)
A single dark band with white text:
- Left column (~ 518 wide): eyebrow `MENGAPA SEKOTA` in teal, H2 "Sekota Bukan Sekadar Vendor, Bukan Sekadar Konsultan." (Montserrat, white), supporting line at white@40 %, and a teal CTA pill **"MULAI KONSULTASI STRATEGIS"** (12 px tracking, uppercase, `--brand-teal` background, white text).
- Right column: two **glass cards** stacked (518 × 138, radius 16, `rgba(255,255,255,0.05)` fill, hairline border), each with a teal icon, a bold white H3 and a white@50 % subtitle:
  1. **Lebih Strategis dari Vendor IT** — "Memberikan insight yang dapat langsung ditindaklanjuti untuk kebijakan."
  2. **Lebih Teknis dari Konsultan** — "Implementasi data real-time, bukan sekadar slide deck rekomendasi."

### 5.8 Contact — "Mulai Percakapan Strategis." (5143 → 5839, `--surface-alt`)
Two-column split:
- **Left (512 wide):** H2 "Mulai Percakapan Strategis.", supporting line ("Tim kami akan merespons dalam 1×24 jam kerja…"), and a vertical list of contact rows (pin/icon + label):
  - `sales@sekota.id`
  - `www.sekota.id`
- **Right (512 wide):** a white form card, radius 40, 56 × 56 × 72 × 56 padding, hairline border. Fields:
  - Row of two — **NAMA LENGKAP** (placeholder "Budi Santoso") + **EMAIL BISNIS** (placeholder "budi@instansi.go.id"). Labels are 10 px DM Sans Bold +1 px tracking, ink@70 %.
  - **PESAN / KEBUTUHAN STRATEGIS** textarea (placeholder "Ceritakan tantangan Anda…").
  - Full-width primary pill **"Kirim & Mulai Konsultasi"** — `--ink` background, white DM Sans Bold 16, 64 px tall.
- Input style: invisible field with a 1 px bottom hairline; placeholder text in ink @ 20 %, DM Sans Medium 14.

### 5.9 Footer (5839 → 6436, dark)
Six logical groups (read from the FooterFOOTER frame):
- Brand lockup + tagline + `eBdesk Group Affiliation` + `NATIONWIDE PARTNER` badge.
- Social row: `LINKEDIN`, `TWITTER` (uppercase pill labels).
- Three-column link grid: **PRODUK** · **PERUSAHAAN** · **LEGAL** (Privasi, Syarat & Ketentuan).
- Language toggle at the bottom: `ENGLISH` / `BAHASA INDONESIA`.

---

## 6. Component patterns

- **Pill button** — `border-radius: 9999`, always; three variants:
  - *Primary* — `--ink` fill, white DM Sans Bold label, optional 8.75 × 8.75 chevron-right icon at 12 px gap. Sizes 40 px (nav), 48 px (dark section CTA), 66 px (hero/CTA), 64 px (form submit).
  - *Outline* — transparent fill, 1 px `--hairline` border, `--ink` label.
  - *Brand* — `--brand-teal` fill, white uppercase label with +1.2 px tracking (used only in the "Why" section).
- **Card** — radius 24 (UI mock, glass cards) or 32 (product cards) or 40 (form card); always 1 px `--hairline` border; shadow either none, `0 1px 2px rgba(0,0,0,0.05)`, or the deep hero shadow.
- **Eyebrow label** — DM Sans Bold, uppercase, +1–2.2 px letter-spacing, teal `rgb(0,181,200)`. Always sits 24 px above the section H2.
- **Stat block** — large DM Sans Bold 24 px ink number, 10 px uppercase label at ink@50 %, divided by a 1 px hairline.
- **Process step chip** — circular numeric badge (`01–05`) on a `--surface-alt` ground, sitting on a horizontal divider line; label below.
- **Feature checklist row** (product cards) — small bullet/check icon + DM Sans 14 line.
- **Form input** — no box; just a 10 px tracking label and a bottom-hairline 45 px tall field, placeholder at ink @ 20 %.
- **Glass card (dark bg)** — `rgba(255,255,255,0.05)` fill, 1 px `rgba(255,255,255,0.05)` border, radius 16, 32 px pad, teal icon + white title + white@50 % body.

---

## 7. Iconography

- Custom flat strokes drawn as SVG (see `Format=Stroke, Weight=Regular` external components). 24 px box.
- A single 8.75 px chevron-right is used inline inside the primary CTA arrow.
- Brand gradient (`--brand-grad`) is reserved for the logo mark and the floating "Intelligence Suite Products" stat pill — *do not* recolor icons with it.

---

## 8. Imagery

- All raster slots in this artboard are placeholders (large PNG IDs in `METADATA.md`, ranging 250 KB → 22 MB) — e-book covers, hero floating UI, and possibly a product hero. When implementing, treat them as user-supplied photography/UI screenshots; keep the 24 px-radius framing.

---

## 9. Implementation notes

- **Indonesian + English mix** is intentional; keep English for product names and strategic terms ("Intelligence Suite", "Strategic Value Loop", "Live Metrics", "SLO"), Indonesian everywhere else.
- The artwork is **desktop-only at 1280**. There is no tablet/mobile variant in the file — design responsive rules from scratch, but preserve the 80 px gutter / 1120 px content rail concept down to ~ 960 and collapse to a single column under that.
- Section heights (765, 875, 612, 1390, 905, 597, 696, 598) intentionally vary; do not try to standardise them.
- The five process steps in §5.5 only ship four labels (02–05); step 01 is the unbranded default — you'll need to author the missing "01" label (likely *Diagnosis Strategis* or similar) when handing this to engineering.
- The hero floating card and product mock-ups are **decorative**, not real product UI — they exist to telegraph "we build dashboards." Keep them as static SVG/PNG on the marketing site rather than wiring them up.
- Accessibility: body copy lives at ink@40–60 % opacity on white; that's roughly `#5C7281`-equivalent which sits *just* above WCAG AA on 16 px text. If you bump font sizes down, raise opacity to 70 %.

---

## 10. Pickup checklist for engineering

- [ ] Pull SVGs out of `/Page-1/Landing-Page/**/*.svg` (logo, icons, dividers) with `fig_copy_files` — do not redraw.
- [ ] Confirm Indonesian copy with stakeholder; some strings are partial in the source (`"…"` truncations on e-book excerpts).
- [ ] Decide what the missing "Step 01" label is.
- [ ] Source real product screenshots for the hero floating card and the 4 product slots.
- [ ] Define the Tablet (≥ 768) and Mobile (< 768) breakpoints — file does not specify.
