# Design System: Sekota Web

## 1. Visual Theme & Atmosphere
A calm, authoritative, editorial B2B/B2G interface for a data and AI intelligence consultancy. The atmosphere is "Art Gallery Airy" (Density 3) paired with "Predictable Symmetric" precision (Variance 3). It relies on a high-contrast interaction between a near-white surface and a single deep-navy ink, punctuated by restrained, sophisticated typography. Motion is deliberate and minimal, emphasizing trust and stability over flash.

## 2. Color Palette & Roles
- **Ink Navy** (`#0D1F2D`) — Primary text, Display typography, primary buttons, and dark section backgrounds. The defining color of the brand.
- **Ink Secondary** (`rgba(20, 50, 68, 0.6)`) — Body text base and lead paragraphs. Used at 40-60% opacity on light surfaces.
- **Surface Pure** (`#FFFFFF`) — Clean background for hero sections, forms, and cards.
- **Surface Alt** (`#F7FAFB`) — Alternating background color to establish banded rhythm across sections.
- **Brand Teal** (`#00B5C8`) — The primary accent hue. Used exclusively for eyebrow labels, accent text, and specific secondary CTAs.
- **Brand Green** (`#60BD65`) — Secondary accent hue, used sparingly alongside Teal for gradients.
- **Whisper Hairline** (`rgba(13, 31, 45, 0.1)`) — 1px structural lines, card borders, dividers.
- **Banned:** Pure black (`#000000`), neon outer glows, and generic "AI Purple" styling.

## 3. Typography Rules
- **Display/Headlines:** `Montserrat` — Track-tight (-3px letter spacing on Display), used exclusively for Hero H1 (ExtraBold) and Section H2s. Hierarchy is driven by extreme weight contrast.
- **UI Workhorse & Body:** `DM Sans` — The core functional font. Relaxed leading (1.5x) for body. Bold, uppercase, and generously tracked (+2px) for eyebrow labels and small caps stat labels.
- **Dashboard Mocks:** `Inter` — *Only* used inside decorative floating dashboard mockups to simulate generic UI; never used for the actual application interface.
- **Banned:** Generic system fonts and standard serifs.

## 4. Component Stylings
* **Buttons (Pills):** Fully rounded (`border-radius: 9999px`). 
  * *Primary:* Ink Navy fill, white `DM Sans` Bold text. Sizes range from 40px to 66px tall based on context.
  * *Outline:* Transparent fill, 1px Whisper Hairline border, Ink Navy text.
* **Cards:** Sharp or subtly rounded (radius 24px-40px depending on scale). 1px Whisper Hairline border. Shadows are virtually non-existent (`0 1px 2px rgba(0,0,0,0.05)`) unless floating in the Hero section.
* **Inputs:** Invisible fields with a 1px bottom Whisper Hairline. No surrounding box. Placeholder text is Ink Navy at 20% opacity. Labels sit above, using 10px `DM Sans` Bold with +1px tracking.
* **Loaders:** Minimalist, linear indeterminate bars in Brand Teal. No circular spinners.

## 5. Layout Principles
- **Grid Architecture:** Strict max-width containment (1120px content within a 1280px frame).
- **Rhythm:** Sections alternate backgrounds between Surface Pure and Surface Alt. Generous vertical padding (120px) creates distinct, breathable zones.
- **Mobile Collapse:** Strict single-column collapse below 768px.
- **Spacing:** CSS Grid over Flexbox math. 32px standard gutter between row groups and cards.

## 6. Motion & Interaction
- **Restraint:** Motion is minimal and purposeful. 
- **Transitions:** Hover states on cards elevate the shadow slightly and transition the hairline border to Ink Navy.
- **Feedback:** Tactile response on buttons (subtle scale down `0.98`) rather than color shifts.

## 7. Anti-Patterns (Banned)
- No emojis anywhere.
- No `Inter` font in the main UI (restricted to decorative mockups only).
- No pure black (`#000000`).
- No neon/outer glow shadows.
- No overlapping elements — clean spatial separation always.
- No AI copywriting clichés ("Elevate", "Seamless", "Next-Gen").
- No fabricated data or statistics.
