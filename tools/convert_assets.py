"""Convert design WebP assets to Compose Multiplatform resource images.

Usage: python -I tools/convert_assets.py <source-assets-dir> <dest-drawable-dir>

Logos keep transparency as PNG; screenshots and covers become JPEG to keep the
web bundle small. Source files are read as data only.
"""
import os
import sys

from PIL import Image

LOGOS = {
    "sekota-light.webp": "logo_sekota_light.png",
    "sekota-dark.webp": "logo_sekota_dark.png",
    "clarivio-light.webp": "logo_clarivio_light.png",
    "clarivio-dark.webp": "logo_clarivio_dark.png",
    "ascendio-light.webp": "logo_ascendio_light.png",
    "ascendio-dark.webp": "logo_ascendio_dark.png",
    "sociara-light.webp": "logo_sociara_light.png",
    "sociara-dark.webp": "logo_sociara_dark.png",
    "evidara-light.webp": "logo_evidara_light.png",
    "evidara-dark.webp": "logo_evidara_dark.png",
}

PHOTOS = {
    "dash-clarivio.webp": "dash_clarivio.jpg",
    "dash-ascendio.webp": "dash_ascendio.jpg",
    "dash-sociara.webp": "dash_sociara.jpg",
    "dash-evidara.webp": "dash_evidara.jpg",
    "cover1.webp": "cover_1.jpg",
    "cover2.webp": "cover_2.jpg",
    "cover3.webp": "cover_3.jpg",
    "cover4.webp": "cover_4.jpg",
    "cover5.webp": "cover_5.jpg",
    "cover6.webp": "cover_6.jpg",
    "products/clarivio-1.webp": "shot_clarivio_1.jpg",
    "products/clarivio-2.webp": "shot_clarivio_2.jpg",
    "products/ascendio-1.webp": "shot_ascendio_1.jpg",
    "products/ascendio-2.webp": "shot_ascendio_2.jpg",
    "products/sociara-1.webp": "shot_sociara_1.jpg",
    "products/sociara-2.webp": "shot_sociara_2.jpg",
    "products/sociara-3.webp": "shot_sociara_3.jpg",
    "products/evidara-1.webp": "shot_evidara_1.jpg",
    "products/evidara-2.webp": "shot_evidara_2.jpg",
}


def main():
    src, dest = sys.argv[1], sys.argv[2]
    os.makedirs(dest, exist_ok=True)
    for rel, out in LOGOS.items():
        with Image.open(os.path.join(src, rel)) as img:
            img = img.convert("RGBA")
            print(f"{rel}: {img.width}x{img.height} -> {out}")
            img.save(os.path.join(dest, out), "PNG", optimize=True)
    for rel, out in PHOTOS.items():
        with Image.open(os.path.join(src, rel)) as img:
            img = img.convert("RGB")
            print(f"{rel}: {img.width}x{img.height} -> {out}")
            img.save(os.path.join(dest, out), "JPEG", quality=90, optimize=True,
                     progressive=True)


if __name__ == "__main__":
    main()
