# Landing Page Implementation Requirements

This document specifies the requirements for the Sekota Landing Page implementation, derived from the Figma design `oZqXuSsN7u7S73vfjWXQIN`.

## 🎨 Visual Identity
- **Primary Font**: Montserrat (ExtraBold) for headings.
- **Secondary Font**: DM Sans for body and interactive elements.
- **Color Palette**:
  - Primary: Gradient from Green (#60BD65) to Cyan (#02B6CF).
  - Background: Clean white/off-white with subtle tonal shifts.
  - Overlay: 50% opacity solid on hero background.

## 🧱 Component Breakdown

### 1. Hero Section (Node ID: `15:1509`)
- **Heading**: "Trusted Intelligence."
  - Style: Montserrat ExtraBold, 88px, Gradient Fill (Green to Cyan).
- **Subtext**: "Memberdayakan organisasi melalui intelligence berbasis data terbuka dan AI..."
  - Style: DM Sans Light, 20px, 60% opacity.
- **CTA Button**: "Konsultasi Strategis"
  - Style: DM Sans Bold, 16px, Pill shape (rounded-full).
  - Background: Green-Cyan Gradient.
- **Visual Effects**: Layer blur (140.0 radius) on decorative overlays.

### 2. Header / Navigation (Node ID: `25:191`)
- **Type**: Sticky/Fixed header.
- **Effect**: Glassmorphism with background blur (20px) and 80% opacity white background.
- **Items**: Sekota Logo, Nav Links (Produk, Solusi, Tentang Kami, Kontak), CTA Button.

### 3. Sidebar Filter (Node ID: `25:196`)
- **Functionality**: Search and category filtering.
- **Categories**: T-Shirt, Pin, Sticker, Others.

## 🛠️ Technical Specifications
- **Module**: `composeApp`
- **Framework**: Compose HTML / Wasm
- **Asset Management**: Logos and icons should be exported as SVGs from Figma.
- **Responsiveness**: Use fluid layout containers (e.g., `Container` frame in Figma translates to a responsive Box/Column).
