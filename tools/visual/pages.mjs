import { mkdir } from "node:fs/promises";
import puppeteer from "puppeteer-core";

const CHROME = "C:/Program Files/Google/Chrome/Application/chrome.exe";
const BASE = (process.env.URL ?? "http://127.0.0.1:8765/").replace(/#.*$/, "").replace(/\/?$/, "/");
const OUT = process.env.OUT ?? "D:/Sekota/2026/SekotaWithNewDesign/docs/screenshots";
const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

const browser = await puppeteer.launch({
  executablePath: CHROME,
  headless: "new",
  args: ["--no-sandbox", "--disable-gpu", "--enable-unsafe-swiftshader", "--hide-scrollbars", "--force-device-scale-factor=1"],
});
await mkdir(OUT, { recursive: true });

async function open(page, hash) {
  await page.goto(BASE + hash, { waitUntil: "domcontentloaded", timeout: 120000 });
  // The app writes this attribute only after it has processed the hash.
  await page.waitForFunction(
    (expected) => document.documentElement.getAttribute("data-route") === expected,
    { timeout: 45000 },
    hash,
  );
  const overflow = await page.evaluate(
    () => document.documentElement.scrollWidth > window.innerWidth + 1,
  );
  if (overflow) throw new Error(`Horizontal overflow on ${hash}`);
  await sleep(2500);
}

async function shot(page, name) {
  await page.screenshot({ path: `${OUT}/${name}.png` });
  console.log("saved", name);
}

async function wheel(page, delta) {
  const { width, height } = page.viewport();
  await page.mouse.move(width / 2, height / 2);
  await page.mouse.wheel({ deltaY: delta });
  await sleep(900);
}

const desktop = await browser.newPage();
await desktop.setViewport({ width: 1440, height: 1000, deviceScaleFactor: 1 });

await open(desktop, "#/id/product/clarivio");
await shot(desktop, "product-clarivio-top");
for (const [i, d] of [760, 760, 760, 760, 760, 760].entries()) {
  await wheel(desktop, d);
  await shot(desktop, `product-clarivio-s${i + 1}`);
}

await open(desktop, "#/id/contact");
await shot(desktop, "contact-top");
for (const [i, d] of [700, 700].entries()) {
  await wheel(desktop, d);
  await shot(desktop, `contact-s${i + 1}`);
}

await open(desktop, "#/id/contact/demo-clarivio");
await shot(desktop, "contact-preset-demo");

await open(desktop, "#/id/contact/mitra-awal");
await shot(desktop, "contact-preset-mitra");

await open(desktop, "#/id/contact/buku-blind-spot-radar");
await shot(desktop, "contact-preset-book");

await open(desktop, "#/id/home/layanan");
await shot(desktop, "home-anchor-layanan");

// Product -> product must start at the new hero, not keep the old scroll.
await open(desktop, "#/id/product/clarivio");
await wheel(desktop, 3000);
await desktop.evaluate(() => { window.location.hash = "#/id/product/ascendio"; });
await desktop.waitForFunction(
  () => document.documentElement.getAttribute("data-route") === "#/id/product/ascendio",
  { timeout: 45000 },
);
await sleep(1500);
await shot(desktop, "product-ascendio-after-switch");

await open(desktop, "#/id/books");
await shot(desktop, "books-index-top");
await wheel(desktop, 700);
await shot(desktop, "books-index-s1");

await open(desktop, "#/id/books/forgetting-enterprise");
await shot(desktop, "books-top");
for (const [i, d] of [760, 760, 760, 760, 760, 760].entries()) {
  await wheel(desktop, d);
  await shot(desktop, `books-s${i + 1}`);
}

await open(desktop, "#/id/privacy");
await shot(desktop, "privacy-top");
await wheel(desktop, 800);
await shot(desktop, "privacy-s1");
await wheel(desktop, 6000);
await wheel(desktop, 6000);
await shot(desktop, "privacy-bottom");

await open(desktop, "#/id/terms");
await shot(desktop, "terms-top");
await wheel(desktop, 800);
await shot(desktop, "terms-s1");
await wheel(desktop, 6000);
await wheel(desktop, 6000);
await shot(desktop, "terms-bottom");

await open(desktop, "#/en/privacy");
await shot(desktop, "privacy-en-top");

await open(desktop, "#/en/terms");
await shot(desktop, "terms-en-top");

await open(desktop, "#/en");
await shot(desktop, "home-en-top");

const mobile = await browser.newPage();
await mobile.setViewport({ width: 390, height: 844, deviceScaleFactor: 2 });
await open(mobile, "#/id/product/clarivio");
await shot(mobile, "product-clarivio-390-top");
await open(mobile, "#/id/contact");
await shot(mobile, "contact-390-top");
await open(mobile, "#/id/books/forgetting-enterprise");
await shot(mobile, "books-390-top");
await open(mobile, "#/id/privacy");
await shot(mobile, "privacy-390-top");
await open(mobile, "#/en");
await shot(mobile, "home-en-390-top");
await open(mobile, "#/en/product/clarivio");
await shot(mobile, "product-en-390-top");

await browser.close();
console.log("All routed captures verified via data-route.");
