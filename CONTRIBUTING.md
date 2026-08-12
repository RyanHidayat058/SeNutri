# Panduan Kontribusi SeNutri 🍎

Terima kasih telah tertarik untuk berkontribusi pada pengembangan **SeNutri**! Kami menyambut baik kontribusi dari siapa pun berupa perbaikan bug, penambahan fitur, peningkatan dokumentasi, maupun masukan lainnya.

Untuk menjaga kualitas proyek ini tetap tinggi, harap ikuti panduan kontribusi berikut.

---

## Bagaimana Cara Berkontribusi?

### 1. Melaporkan Masalah (Reporting Issues)
Jika Anda menemukan bug, kendala, atau ingin mengusulkan fitur baru, silakan buka *Issue* baru melalui tab **Issues** di GitHub.
- Gunakan **Template Issue** yang telah disediakan (Bug Report atau Feature Request).
- Berikan deskripsi yang jelas, langkah-langkah reproduksi bug, serta tangkapan layar (jika ada).

### 2. Mengajukan Perubahan Kode (Pull Request)
Jika Anda ingin memperbaiki bug atau menambahkan fitur baru secara langsung:
1. **Fork** repositori ini ke akun GitHub Anda.
2. Buat cabang baru (*branch*) untuk fitur atau perbaikan Anda.
   ```bash
   git checkout -b fitur/nama-fitur-baru
   # atau
   git checkout -b bugfix/nama-perbaikan-bug
   ```
3. Lakukan perubahan kode. Pastikan untuk mengikuti gaya penulisan kode Kotlin yang bersih dan konsisten.
4. Lakukan pengujian lokal untuk memastikan kode Anda dapat dibangun (*build*) tanpa kesalahan.
5. Commit perubahan Anda dengan pesan commit yang deskriptif dan jelas:
   ```bash
   git commit -m "feat: menambahkan fitur pencarian makanan"
   ```
6. **Push** cabang tersebut ke repositori fork Anda.
7. Buka **Pull Request (PR)** ke repositori utama SeNutri (cabang `main`).
8. Jelaskan secara singkat apa saja perubahan yang Anda buat di dalam deskripsi Pull Request.

---

## Pedoman Gaya Kode Kotlin

Kami menggunakan standar penulisan kode resmi dari Kotlin. Beberapa aturan dasar:
- Gunakan penamaan variabel/fungsi menggunakan `camelCase`.
- Gunakan penamaan kelas menggunakan `PascalCase`.
- Hapus impor (*import*) yang tidak digunakan sebelum mengirimkan kode.
- Sediakan komentar penjelasan untuk logika-logika yang kompleks.

---

## Lisensi

Dengan berkontribusi pada proyek ini, Anda setuju bahwa kontribusi Anda akan dilisensikan di bawah lisensi **MIT License** yang digunakan oleh proyek ini.
