import puppeteer from "puppeteer-core";

const CHROME = "C:/Program Files/Google/Chrome/Application/chrome.exe";
const URL = "http://127.0.0.1:8765/";
const OUT = "D:/Sekota/2026/SekotaWithNewDesign/build/verify";

const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

async function shoot(page, name) {
  await page.screenshot({ path: `${OUT}/${name}.png`, fullPage: true });
  console.log("saved", name);
}

async function scrollBy(page, delta) {
  const { width, height } = page.viewport();
  await page.mouse.move(width / 2, height / 2);
  await page.mouse.wheel({ deltaY: delta });
  await sleep(1200);
}

const browser = await puppeteer.launch({
  executablePath: CHROME,
  headless: "new",
  args: [
    "--no-sandbox",
    "--disable-gpu",
    "--enable-unsafe-swiftshader",
    "--hide-scrollbars",
    "--force-device-scale-factor=1",
  ],
});

await mkdir(OUT, { recursive: true });

// 1440 - Product hover
const p1440 = await browser.newPage();
await p1440.setViewport({ width: 1440, height: 1000, deviceScaleFactor: 1 });
await p1440.goto(URL + "#/id/product/clarivio", { waitUntil: "networkidle0", timeout: 60000 });
await sleep(2000);

// Hover first feature card
await p1440.evaluate(() => {
  const card = document.querySelector('.product-feat');
  if (card) {
    card.dispatchEvent(new MouseEvent('mouseenter', { bubbles: true }));
  }
});
await sleep(500);
await shoot(p1440, "product-1440-hover-feature");

// Hover tab
await p1440.evaluate(() => {
  const tab = document.querySelector('.product-tab');
  if (tab) {
    tab.dispatchEvent(new MouseEvent('mouseenter', { bubbles: true }));
  }
});
await sleep(500);
await shoot(p1440, "product-1440-hover-tab");

await p1440.close();

// 390 - Product mobile
const p390 = await browser.newPage();
await p390.setViewport({ width: 390, height: 844, deviceScaleFactor: 2 });
await p390.goto(URL + "#/id/product/clarivio", { waitUntil: "networkidle0", timeout: 60000 });
await sleep(2000);
await shoot(p390, "product-390-mobile");
await p390.close();

// 1024 - Legal pages
const l1024 = await browser.newPage();
await l1024.setViewport({ width: 1024, height: 1000, deviceScaleFactor: 1 });
await l1024.goto(URL + "#/id/privacy", { waitUntil: "networkidle0", timeout: 60000 });
await sleep(2000);
await shoot(l1024, "legal-privacy-1024");

await scrollBy(l1024, 500);
await sleep(1000);
await shoot(l1024, "legal-privacy-1024-scrolled");

await l1024.close();

// 390 - Legal mobile
const l390 = await browser.newPage();
await l390.setViewport({ width: 390, height: 844, deviceScaleFactor: 2 });
await l390.goto(URL + "#/id/privacy", { waitUntil: "networkidle0", timeout: 60000 });
await sleep(2000);
await shoot(l390, "legal-privacy-390-mobile");
await l390.close();

// English legal
const lEN = await browser.newPage();
await lEN.setViewport({ width: 1440, height: 1000, deviceScaleFactor: 1 });
await lEN.goto(URL + "#/en/privacy", { waitUntil: "networkidle0", timeout: 60000 });
await sleep(2000);
await shoot(lEN, "legal-en-privacy");
await lEN.close();

// Terms
const t1024 = await browser.newPage();
await t1024.setViewport({ width: 1024, height: 1000, deviceScaleFactor: 1 });
await t1024.goto(URL + "#/id/terms", { waitUntil: "networkidle0", timeout: 60000 });
await sleep(2000);
await shoot(t1024, "legal-terms-1024");
await t1024.close();

// Contact
const c1440 = await browser.newPage();
await c1440.setViewport({ width: 1440, height: 1000, deviceScaleFactor: 1 });
await c1440.goto(URL + "#/id/contact", { waitUntil: "networkidle0", timeout: 60000 });
await sleep(2000);
await shoot(c1440, "contact-1440");
await c1440.close();

// 1024 - Books
const b1024 = await browser.newPage();
await b1024.setViewport({ width: 1024, height: 1000, deviceScaleFactor: 1 });
await b1024.goto(URL + "#/id/books", { waitUntil: "networkidle0", timeout: 60000 });
await sleep(2000);
await shoot(b1024, "books-1024");
await b1024.close();

// 1024 - Other platform cards
const op1024 = await browser.newPage();
await op1024.setViewport({ width: 1024, height: 1000, deviceScaleFactor: 1 });
await op1024.goto(URL + "#/id/product/clarivio", { waitUntil: "networkidle0", timeout: 60000 });
await sleep(2000);
await scrollBy(op1024, 300);
await sleep(1000);
await shoot(op1024, "other-platform-1024");
await op1024.close();

// 1440 - Other platform cards
const op1440 = await browser.newPage();
await op1440.setViewport({ width: 1440, height: 1000, deviceScaleFactor: 1 });
await op1440.goto(URL + "#/id/product/clarivio", { waitUntil: "networkidle0", timeout: 60000 });
await sleep(2000);
await scrollBy(op1440, 300);
await sleep(1000);
await shoot(op1440, "other-platform-1440");
await op1440.close();

// 1440 - Related link hover
const rl1440 = await browser.newPage();
await rl1440.setViewport({ width: 1440, height: 1000, deviceScaleFactor: 1 });
await rl1440.goto(URL + "#/id/product/clarivio", { waitUntil: "networkidle0", timeout: 60000 });
await sleep(2000);
await scrollBy(rl1440, 400);
await sleep(1000);
await shoot(rl1440, "related-link-1440");

// Hover the "Lihat semua layanan" link
await rl1440.evaluate(() => {
  const link = document.querySelector('a[href*="layanan"]');
  if (link) {
    link.dispatchEvent(new MouseEvent('mouseenter', { bubbles: true }));
  }
});
await sleep(500);
await shoot(rl1440, "related-link-hover-1440");
await rl1440.close();

await browser.close();

import { mkdir } from "node:fs/promises";
