import { createHash } from "node:crypto";
import { readFile, mkdir } from "node:fs/promises";
import puppeteer from "puppeteer-core";

const CHROME = "C:/Program Files/Google/Chrome/Application/chrome.exe";
const BASE = (process.env.URL ?? "http://127.0.0.1:8765/").replace(/#.*$/, "").replace(/\/?$/, "/");
const OUT = process.env.OUT ?? "D:/Sekota/2026/SekotaWithNewDesign/docs/screenshots";
const sleep = (ms) => new Promise((r) => setTimeout(r, ms));
const sha = async (p) => createHash("sha256").update(await readFile(p)).digest("hex");

const browser = await puppeteer.launch({
  executablePath: CHROME,
  headless: "new",
  args: ["--no-sandbox", "--disable-gpu", "--enable-unsafe-swiftshader", "--hide-scrollbars", "--force-device-scale-factor=1"],
});
await mkdir(OUT, { recursive: true });
const page = await browser.newPage();
await page.setViewport({ width: 1440, height: 1000, deviceScaleFactor: 1 });
// Freeze marquee/highlight/reveal so a screenshot diff proves scrolling.
await page.emulateMediaFeatures([{ name: "prefers-reduced-motion", value: "reduce" }]);

async function open(hash) {
  await page.goto(BASE + hash, { waitUntil: "domcontentloaded", timeout: 120000 });
  await page.waitForFunction(
    (expected) => document.documentElement.getAttribute("data-route") === expected,
    { timeout: 45000 },
    hash,
  );
  await sleep(3000);
}

await open("#/id");
const before = `${OUT}/_a11y-before.png`;
const after = `${OUT}/_a11y-after.png`;
await page.screenshot({ path: before });
await page.keyboard.press("PageDown");
await sleep(1500);
await page.screenshot({ path: after });
if ((await sha(before)) === (await sha(after))) {
  throw new Error("Keyboard PageDown did not scroll the page");
}
console.log("keyboard scrolling: OK");

const langId = await page.evaluate(() => document.documentElement.lang);
if (langId !== "id") throw new Error(`document lang expected id, got ${langId}`);
await open("#/en");
const langEn = await page.evaluate(() => document.documentElement.lang);
if (langEn !== "en") throw new Error(`document lang expected en, got ${langEn}`);
console.log("document language: OK");

await open("#/id");
await page.evaluate(() => { window.location.hash = "#/id/product/clarivio"; });
await page.waitForFunction(
  () => document.documentElement.getAttribute("data-route") === "#/id/product/clarivio",
  { timeout: 45000 },
);
await page.goBack();
await page.waitForFunction(
  () => document.documentElement.getAttribute("data-route") === "#/id",
  { timeout: 45000 },
);
console.log("browser hash back/forward: OK");

await page.screenshot({ path: `${OUT}/a11y-reduced-motion.png` });
await browser.close();
console.log("A11y checks passed.");
