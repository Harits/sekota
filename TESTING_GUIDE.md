# Testing Guide - Sekota Web Sprint 1

This guide provides instructions for verifying the implementation of Sprint 1 components, including the Navbar, Hero Section, Sidebar Filter, and Infrastructure setup.

## 🚀 Running the App Locally

### 1. Using Gradle (Development)
To run the web app in development mode with hot reload:
```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```
The app will be available at `http://localhost:8080` (default port).

### 2. Using Docker/Podman (Isolated Production)
To build and run the containerized app:
```bash
# Build the image
docker build -t sekota-web .

# Run the container
docker run -p 8080:80 sekota-web
```
Alternatively, using Docker Compose:
```bash
docker compose up
```
The app will be available at `http://localhost:8080`.

## 🧪 Manual Verification Steps

### 1. Header / Navigation (Node ID: `15:1992`)
- **Check**: Sticky behavior on scroll.
- **Check**: Glassmorphism effect (background blur and 80% opacity).
- **Check**: Navigation links (Solusi, Produk, E-Book, Mengapa Sekota, Kontak).
- **Check**: "Konsultasi Strategis" button with gradient background.

### 2. Hero Section (Node ID: `15:1509`)
- **Check**: "Trusted Intelligence" heading with Green-Cyan gradient.
- **Check**: Responsive layout of text and CTA buttons.
- **Check**: Montserrat ExtraBold font for headings.

### 3. Sidebar Filter (Node ID: `25:196`)
- **Check**: Search input functionality.
- **Check**: Category checkboxes (T-Shirt, Pin, Sticker, Others).

### 4. Network Client (Shared)
- Open Browser DevTools (F12) -> Console.
- Verify no "Ktor initialization" errors.
- (If implemented) Check the "Network" tab for any initial fetch requests to the base URL defined in `Constants.kt`.

## 🛠️ Troubleshooting
- If the build fails, ensure you have the correct Kotlin and Compose versions installed.
- For Docker issues, ensure the Docker daemon is running and you have necessary permissions.
