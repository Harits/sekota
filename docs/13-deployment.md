# 13 — Deployment (Docker Compose / Podman Compose)

Runbook untuk tim Technical Services. Satu image menjalankan **Ktor** yang menyajikan build Wasm di `/` dan `POST /api/contact` di origin yang sama, sehingga tidak ada masalah CORS pada deployment normal.

Semua perintah dijalankan dari root repo (`D:\Sekota\2026\SekotaWithNewDesign` di Windows, atau hasil ekstrak repo di server Linux).

---

## 1. Arsitektur singkat

```
browser ──80/443──> Caddy (opsional, profil "proxy")
                        │
                        └──8080 (internal)──> sekota (Ktor + Netty)
                                                 ├── GET  /health
                                                 ├── POST /api/contact
                                                 └── GET  /*  (Wasm build)
```

- Image runtime: `eclipse-temurin:21-jre-jammy`, user non-root UID/GID `10001`.
- Aplikasi **stateless**: tidak ada database atau volume data aplikasi.
- Log aplikasi ke stdout (dibaca driver log container).
- `SEKOTA_WEB_DIR=/opt/sekota/web` sudah di-set di image.
- Service `sekota` tidak mempublikasikan port di `compose.yaml`; Caddy
  mengaksesnya lewat network Compose. Staging/produksi mempublikasikan port
  loopback (`127.0.0.1:8080`) untuk ingress eksternal. Development memakai
  host networking (lihat §5).

## 2. Prasyarat

- Docker Engine 24+ dengan Compose v2 (`docker compose`), **atau**
  Podman 4.9+ dengan provider Compose.
- Untuk Podman, pastikan provider tersedia:

  ```bash
  podman compose version
  # jika gagal: pipx install podman-compose
  # atau pasang docker-compose v2 lalu set PODMAN_COMPOSE_PROVIDER=docker-compose
  ```

- Build image membutuhkan sekitar **6 GB RAM** (disarankan **8 GB**), **5 GB
  disk**, dan akses internet (Gradle, Kotlin/Wasm, Node/Yarn diunduh saat build
  pertama).
- Domain dan DNS (staging/produksi), kredensial SMTP, serta akses port 80/443
  bila memakai Caddy.
- **Android SDK tidak diperlukan** untuk build image. Hanya task
  `:server:installDist` dan `:app:webApp:wasmJsBrowserDistribution` yang
  dijalankan; `local.properties` sengaja tidak dikirim ke build context.

## 3. File deployment

| File | Fungsi |
|---|---|
| `Dockerfile` | Multi-stage: build JVM + Wasm, runtime JRE non-root |
| `compose.yaml` | Service dasar `sekota` + Caddy opsional (profil `proxy`) |
| `compose.development.yaml` | Override lokal: tanpa TLS, restart mati |
| `compose.staging.yaml` | Override staging: limit resource |
| `compose.production.yaml` | Override produksi: restart always, limit + log rotation |
| `.env.example` | Referensi semua variabel |
| `.env.development.example` | Env lokal (port 8090, FormSubmit) |
| `.env.staging.example` | Env staging (domain staging, TLS) |
| `.env.production.example` | Env produksi (SMTP wajib) |
| `Caddyfile` | Reverse proxy + HTTPS otomatis |
| `.dockerignore` | Mengecualikan build output, secret, dokumen, tools |

## 4. Matriks environment

| Variabel / aspek | Development | Staging | Production |
|---|---|---|---|
| URL | `http://localhost:8090` | `https://staging.sekota.id` | `https://sekota.id` |
| `COMPOSE_PROJECT_NAME` | `sekota-dev` | `sekota-staging` | `sekota` |
| `CONTAINER_NAME` | `sekota-dev` | `sekota-staging` | `sekota` |
| `PORT` | `8090` (listen) | `8080` (publish) | `8080` (publish) |
| `BIND_ADDR` | – (host net) | `127.0.0.1` | `127.0.0.1` |
| `SEKOTA_ALLOWED_ORIGINS` | localhost:8090/8765 | `https://staging.sekota.id` | `https://sekota.id` |
| Mailer | FormSubmit | FormSubmit **atau** SMTP staging | **SMTP wajib** |
| TLS | tidak ada | Caddy / ingress | Caddy / ingress |
| `restart` | `no` | `unless-stopped` | `always` |
| Resource | 1 GB / 1 CPU | 1 GB / 1 CPU | 1.5 GB / 2 CPU |
| Log rotation | default | default | 10 MB x 5 |

Gunakan `COMPOSE_PROJECT_NAME`, `CONTAINER_NAME`, dan `PORT` berbeda bila
beberapa stack berjalan di host yang sama. `--env-file` sudah membawa
`COMPOSE_PROJECT_NAME`; bila versi Compose tidak membacanya, tambahkan
`-p sekota-staging` (atau nama project lain) pada perintah. Staging dan
produksi tidak bisa berbagi port 80/443 pada satu host — pakai host terpisah,
ingress eksternal, atau `HTTP_PORT`/`HTTPS_PORT` lain (perhatikan tantangan
ACME, lihat §8).

## 5. Development (lokal, tanpa TLS)

```bash
cp .env.development.example .env.development
# edit SEKOTA_FORMSUBMIT_TO ke mailbox yang bisa dibuka untuk aktivasi

docker compose -f compose.yaml -f compose.development.yaml \
  --env-file .env.development up -d --build

curl -fsS http://localhost:8090/health   # ok
```

Podman: ganti `docker compose` dengan `podman compose`.

- Buka `http://localhost:8090`.
- FormSubmit hanya untuk uji; penerima harus mengklik email aktivasi sekali.
  Biarkan `SEKOTA_FORMSUBMIT_ORIGIN=https://sekota.id` agar form yang sudah
  diaktivasi dipakai ulang; mengubah origin memicu aktivasi baru.
- Tidak ada SMTP dan tidak ada TLS pada stack ini.
- Override development memakai **host networking** (`network_mode: host`,
  `PORT=8090`), jadi port 8090 harus bebas di host. Ini juga jalur yang bekerja
  di Windows/WSL, tempat kernel WSL tidak mendukung ekspresi nftables `fib`
  yang dibutuhkan netavark untuk port publishing.
- Untuk iterasi UI tanpa rebuild image, buka komentar `volumes:` di
  `compose.development.yaml` setelah menjalankan
  `./gradlew :app:webApp:wasmJsBrowserDistribution`.

### Windows/WSL: mengakses stack development

Dengan Podman machine WSL, port yang dipublikasikan tidak dapat dijangkau dari
Windows (netavark gagal menerapkan ruleset: `nft ... fib`). Stack tetap berjalan
dan sehat di dalam VM. Buat tunnel SSH sekali jalan:

```powershell
# port SSH dari: podman machine inspect --format '{{.SSHConfig.Port}}'
ssh -i "$env:USERPROFILE\.local\share\containers\podman\machine\machine" `
    -p 60555 -N -L 8090:127.0.0.1:8090 user@127.0.0.1
```

Lalu buka `http://127.0.0.1:8090`. Untuk deployment nyata gunakan host Linux
(Docker/Podman) — batasan ini hanya milik kernel WSL.

## 6. Staging (domain staging, TLS)

```bash
cp .env.staging.example .env.staging
# edit DOMAIN, SEKOTA_ALLOWED_ORIGINS, lalu pilih FormSubmit atau SMTP

# Dengan Caddy otomatis (butuh port 80/443 dan DNS staging mengarah ke host):
docker compose -f compose.yaml -f compose.staging.yaml \
  --env-file .env.staging --profile proxy up -d --build
```

Tanpa Caddy (pakai ingress/load balancer eksternal):

```bash
docker compose -f compose.yaml -f compose.staging.yaml \
  --env-file .env.staging up -d --build
# ingress meneruskan HTTPS ke 127.0.0.1:8080
```

Staging bukan untuk trafik pelanggan. Jangan memakai kredensial SMTP produksi;
gunakan mailbox/SMTP staging atau FormSubmit.

## 7. Produksi

```bash
cp .env.production.example .env.production
chmod 600 .env.production
# isi SMTP, DOMAIN, IMAGE_TAG. Pastikan SEKOTA_FORMSUBMIT_TO kosong.

docker compose -f compose.yaml -f compose.production.yaml \
  --env-file .env.production --profile proxy up -d --build
```

Validasi konfigurasi sebelum start:

```bash
docker compose -f compose.yaml -f compose.production.yaml \
  --env-file .env.production config >/dev/null
```

Tanpa Caddy (ingress eksternal): hilangkan `--profile proxy`; ingress
meneruskan ke `127.0.0.1:8080` sesuai `BIND_ADDR`.

Catatan produksi:

- `SEKOTA_SMTP_HOST` **wajib**. Tanpa SMTP/FormSubmit, endpoint mengembalikan
  `503 not_configured`; UI menampilkan gagal dan email langsung, bukan sukses palsu.
- `SEKOTA_ALLOWED_ORIGINS` hanya origin produksi. Same-origin sebenarnya tidak
  membutuhkan CORS; daftar ini untuk pengamanan bila UI dipisah origin.
- Jangan membuka port 8080 ke publik. `BIND_ADDR=127.0.0.1` sudah di-set.
- Rate limiting tidak dibangun di aplikasi. Untuk trafik publik, tambahkan
  rate limit/WAF di ingress atau penyedia CDN.

## 8. TLS dan reverse proxy

### Opsi A — Caddy dalam Compose (profil `proxy`)

`Caddyfile` memakai `DOMAIN` dari env dan `reverse_proxy sekota:8080`.
Untuk domain publik, Caddy menerbitkan sertifikat Let's Encrypt otomatis
(HTTP-01, butuh port 80 terbuka). DNS harus sudah mengarah ke host.

- Ganti `DOMAIN=localhost` hanya untuk lokal; browser akan memakai CA internal
  Caddy (peringatan kecuali CA dipercaya).
- Untuk email notifikasi kedaluwarsa sertifikat, uncomment baris `email` di
  blok global `Caddyfile`.
- `ACME_CA` membedakan staging dan produksi. Staging memakai Let's Encrypt
  staging CA (sertifikat tidak dipercaya browser, tetapi menghindari rate limit
  produksi); produksi memakai CA produksi.
- Sertifikat dan akun disimpan di volume `caddy_data` / `caddy_config`.
- `Caddyfile` mengirim HSTS, `X-Content-Type-Options`, `X-Frame-Options DENY`,
  dan `Referrer-Policy`. HSTS memakai `includeSubDomains`; jangan pakai profil
  proxy dengan `DOMAIN=localhost` kecuali siap memaksa HTTPS di localhost.
- Untuk staging dan produksi di host yang sama, gunakan satu instance Caddy
  yang melayani dua domain, atau pisahkan host. Mengubah `HTTP_PORT` membuat
  tantangan HTTP-01 gagal.
- Untuk domain `www`, isi `DOMAIN=sekota.id, www.sekota.id` (Caddy menerima
  daftar alamat dipisah koma) dan tambahkan kedua origin ke
  `SEKOTA_ALLOWED_ORIGINS` bila UI diakses dari keduanya.

### Opsi B — Ingress eksternal

Terminasi TLS di nginx/Traefik/load balancer, lalu proxy ke
`http://127.0.0.1:8080`. Jangan aktifkan profil `proxy` supaya tidak ada dua
penerminasi TLS.

### Rootless Podman dan port < 1024

Rootless Podman tidak boleh bind port 80/443 secara default. Pilihan:

```bash
# izinkan port rendah untuk user saat ini (hilang setelah reboot):
sudo sysctl -w net.ipv4.ip_unprivileged_port_start=80

# permanen:
echo 'net.ipv4.ip_unprivileged_port_start=80' | \
  sudo tee /etc/sysctl.d/99-podman-ports.conf && sudo sysctl --system
```

Alternatif: Podman rootful, atau ingress eksternal (Opsi B). Untuk development
port 8090 tidak terpengaruh.

## 9. Healthcheck dan smoke test

Image dan Compose memakai `GET /health` (respons teks `ok`).

```bash
curl -fsS http://localhost:8090/health          # development
curl -fsS https://sekota.id/health              # produksi
# Staging memakai Let's Encrypt staging CA (tidak dipercaya browser):
curl -kfsS https://staging.sekota.id/health     # staging (uji internal)

docker compose -f compose.yaml -f compose.production.yaml \
  --env-file .env.production ps          # STATUS menampilkan (healthy)
# Nama container sesuai CONTAINER_NAME: sekota-dev / sekota-staging / sekota
docker inspect --format '{{.State.Health.Status}}' sekota
```

Smoke test pengiriman email (mengirim email nyata; gunakan alamat uji):

```bash
curl -sS -w '\nHTTP %{http_code}\n' -X POST https://sekota.id/api/contact \
  --data-urlencode "name=Deploy Check" \
  --data-urlencode "email=it@example.com" \
  --data-urlencode "topic=Uji deploy" \
  --data-urlencode "body=Smoke test setelah deployment" \
  --data-urlencode "language=id" \
  --data-urlencode "page=/deploy-check"
```

Respons yang diharapkan: HTTP `202` dengan `{"status":"sent"}`.
`503` = mailer belum dikonfigurasi; `502` = pengiriman gagal (lihat log).

## 10. Log dan operasi harian

```bash
docker compose -f compose.yaml -f compose.production.yaml \
  --env-file .env.production logs -f --tail=200 sekota

docker compose -f compose.yaml -f compose.production.yaml \
  --env-file .env.production logs --tail=200 caddy
```

Log rotasi produksi memakai opsi driver default Docker (`json-file`,
10 MB x 5). Jika provider Podman mengabaikan `logging`, gunakan journald
atau opsi log Podman. Konfigurasi `logback.xml` aplikasi memakai root level
`trace`; rotasi driver membatasi ukuran, tetapi untuk volume log lebih kecil
perlu penyesuaian konfigurasi log di source (bukan bagian deployment ini).

## 11. Update dan rollback

Update (ubah `IMAGE_TAG` di `.env.production` terlebih dahulu):

```bash
docker compose -f compose.yaml -f compose.production.yaml \
  --env-file .env.production build --pull
docker compose -f compose.yaml -f compose.production.yaml \
  --env-file .env.production up -d --no-build
```

Rollback: set `IMAGE_TAG` ke tag lama, lalu `up -d --no-build`. Image lama harus
masih ada secara lokal (jangan prune) atau di registry:

```bash
# jika memakai registry:
docker compose -f compose.yaml -f compose.production.yaml \
  --env-file .env.production pull
```

Cek kesehatan setelah update: `/health`, status healthcheck, lalu smoke test.

## 12. Backup

Aplikasi stateless; tidak ada database yang perlu di-backup.

- Backup `.env.*` (berisi kredensial) di secret store, bukan di repo.
- Backup volume Caddy bila tidak ingin menerbitkan ulang sertifikat:
  `caddy_data` menyimpan sertifikat dan kunci akun ACME.
  Nama volume mengikuti project, mis. `sekota_caddy_data`.
- Jangan jalankan `down -v` di produksi kecuali memang ingin menghapus
  sertifikat Caddy.

## 13. Troubleshooting

### Build image gagal: "SDK location not found"

Seharusnya tidak terjadi. Dockerfile hanya meminta dua task non-Android dan
`.dockerignore` membuang `local.properties`. Pastikan tidak ada task Android
yang ditambahkan ke perintah Gradle di Dockerfile. Jika suatu saat
`:app:androidApp` ikut dibangun, Android SDK wajib tersedia di builder.

### Build Wasm gagal / OOM / unduhan macet

- Beri Docker/Podman minimal 6 GB RAM (disarankan 8 GB).
- Build pertama mengunduh Gradle, Kotlin, Node, dan Yarn; butuh internet stabil.
- Jika konfigurasi build bermasalah, perintah di Dockerfile sudah memakai
  `--no-configuration-cache`.
- Di balik proxy, lewatkan `HTTPS_PROXY`/`NO_PROXY` sebagai build arg.
- `node: error while loading shared libraries: libatomic.so.1` → image builder
  sudah memasang `libatomic1`; jangan hapus baris `apt-get` tersebut.

### Podman WSL: "nftables error ... fib" saat start container

Kernel WSL tidak menyediakan modul `nft_fib_inet`, sehingga netavark gagal
menerapkan ruleset saat mempublikasikan port. Stack development memakai host
networking dan tunnel SSH (lihat §5). Untuk staging/produksi gunakan host Linux.
Jangan mengganti `firewall_driver` ke `iptables`; netavark 2.x hanya mendukung
nftables.

### Port sudah dipakai

- Development: ganti `PORT` di `.env.development` (8090 → port lain > 1024).
- Staging/produksi: hanya satu stack yang boleh memakai 80/443 per host.
- Jangan memakai `PORT` lokal selain 8080/8090 bila membuka situs lewat
  `localhost`/`127.0.0.1` (lihat catatan same-origin di bawah).

### Form kontak gagal / CORS

- Same-origin: build web memakai `""` sebagai base URL untuk host non-local,
  sehingga POST ke `/api/contact` di origin yang sama. Ini jalur normal.
- **Perilaku klien saat ini:** bila diakses lewat `localhost`/`127.0.0.1`
  dengan port selain 8080/8090, klien menembak `http://127.0.0.1:8090`.
  Karena itu development memakai 8090. Untuk domain staging/produksi tidak
  terpengaruh. Perbaikan jangka panjang ada di lead engineer.
- CORS hanya relevan bila UI dan API beda origin. Daftarkan origin persis
  (skema + host + port), tanpa wildcard.
- `503`: tidak ada mailer. `502`: SMTP/FormSubmit gagal; cek log dan kredensial.

### Email masuk Spam / tidak terkirim

- FormSubmit **hanya untuk uji**: sering masuk Spam, isi melewati pihak ketiga,
  dan perlu aktivasi sekali.
- Produksi: pakai SMTP resmi dengan SPF, DKIM, dan DMARC yang benar.
  `SEKOTA_MAIL_FROM` harus sejalan dengan domain pengirim.
- Gunakan port **587 + STARTTLS**. Port 465 (implicit TLS) belum didukung kode
  server saat ini; perlu perubahan aplikasi.
- Gunakan app password khusus, bukan password akun utama.
- Uji dengan mail-tester atau header `Authentication-Results` di email.

### Podman rootless tidak bisa bind 80/443

Lihat §8 (`net.ipv4.ip_unprivileged_port_start`) atau pakai ingress eksternal.

### Caddy: permission denied pada Caddyfile (SELinux)

Bind mount sudah memakai `:ro,Z`. Jika masih gagal, pastikan path host dapat
dibaca dan SELinux tidak memblokir volume lain.

### Container read-only error

Root filesystem sengaja `read_only`; hanya `/tmp` yang writable (tmpfs).
Jika menambah file/config, mount volume eksplisit ke path yang dibutuhkan.

### `podman compose` gagal: provider tidak ditemukan

Pasang `podman-compose` atau `docker-compose` v2 dan set
`PODMAN_COMPOSE_PROVIDER`.

## 14. Checklist keamanan

- [ ] `.env.production` hanya di server/secret store, permission `600`, tidak
      pernah di-commit. `.env`, `.env.development`, `.env.staging`, dan
      `.env.production` sudah masuk `.gitignore`; hanya `*.example` yang
      di-commit.
- [ ] Tidak ada kredensial di Dockerfile, image, atau `compose*.yaml`.
- [ ] Container berjalan sebagai UID 10001, `read_only`, `cap_drop: ALL`,
      `no-new-privileges`.
- [ ] Hanya 80/443 terekspos; port aplikasi terikat `127.0.0.1`.
- [ ] HTTPS aktif dan redirect HTTP → HTTPS (Caddy otomatis).
- [ ] CORS hanya origin yang benar; tanpa wildcard.
- [ ] SMTP memakai app password; SPF/DKIM/DMARC valid.
- [ ] FormSubmit nonaktif di produksi.
- [ ] Rate limit/WAF di ingress untuk endpoint publik.
- [ ] Image di-update berkala; pertimbangkan pin digest base image.
- [ ] Log tidak memuat password/token; retensi log sesuai kebijakan.
- [ ] Backup `.env.production` dan (opsional) volume `caddy_data`.

## 15. Catatan build image

- Base image: `eclipse-temurin:21-jdk-jammy` (builder) dan
  `eclipse-temurin:21-jre-jammy` (runtime), `caddy:2.10.2-alpine` untuk proxy.
  Tag major/patch ini dapat berubah; untuk reproduksibilitas ketat, pin digest
  `@sha256:...` setelah verifikasi.
- `gradle/gradle-daemon-jvm.properties` dihapus di builder agar Gradle memakai
  JDK 21 dari image, bukan mengunduh JDK Azul tambahan. Ini tidak mengubah
  toolchain kompilasi (tetap JDK 21).
- Builder memasang `libatomic1` + `ca-certificates`: Node yang diunduh Kotlin
  untuk build Wasm membutuhkan `libatomic.so.1`, yang tidak ada di image jammy.
- Runtime hanya berisi `installDist` server, build Wasm, dan `curl` untuk
  healthcheck — tanpa Gradle, source, Android SDK, atau Node.

## 16. Verifikasi yang sudah dilakukan

- Dry-run Gradle tanpa `local.properties`/`ANDROID_HOME`: task
  `:server:installDist` dan `:app:webApp:wasmJsBrowserDistribution` berhasil
  dikonfigurasi; task Android gagal seperti yang diharapkan (memastikan task
  Android tidak masuk graph).
- **`podman compose` development berjalan nyata** di mesin ini
  (`sekota-dev`, status `healthy`): image `localhost/sekota-web:dev` dibangun
  penuh di dalam Podman (Gradle + Kotlin/Wasm + Node), lalu stack dijalankan.
  Verifikasi lewat tunnel SSH: `/health` = `ok`, `/` = `200 text/html`,
  `*.wasm` = `200 application/wasm`, halaman landing & privasi render
  (`data-route` cocok), dan `POST /api/contact` = `202 {"status":"sent"}`
  melalui FormSubmit.
- Distribusi server (`server/build/install/server`) dijalankan lokal dengan
  JDK 21 dan `SEKOTA_WEB_DIR` diarahkan ke build Wasm:
  - `GET /health` → `200 ok`
  - `GET /` → `200 text/html`
  - `GET/*.wasm` → `200 application/wasm`
  - `POST /api/contact` tanpa mailer → `503 {"status":"not_configured"}`
- Semua `compose*.yaml` lolos parse YAML dan interpolasi variabel per
  environment; `docker compose config` (Compose v5.8.0 di container) berhasil
  untuk development, staging, dan production.
- `Caddyfile` lolos `caddy validate` memakai image `caddy:2.10.2-alpine`.
- **Belum dieksekusi:** `docker build`, `docker compose`, stack staging, dan
  stack produksi (butuh host Linux, domain, dan kredensial SMTP). Port
  publishing tidak bisa diuji di Windows/WSL karena keterbatasan kernel;
  jalankan smoke test §9 di server staging.
