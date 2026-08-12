# SeNutri 🍎✨

![SeNutri Banner](https://github.com/user-attachments/assets/6a14f861-6c06-4390-9369-73ca0353d0b8)

> **Smart Eating, Smart Tracking**  
> *Pengembangan Aplikasi Mobile Gizi untuk Pemantauan dan Perencanaan Pola Makan Sehat Berbasis Android.*  
> *Nutrition Mobile Application Development for Healthy Diet Monitoring and Planning on Android.*

---

[![Android Build Status](https://github.com/RyanHidayat058/SeNutri/actions/workflows/android.yml/badge.svg)](https://github.com/RyanHidayat058/SeNutri/actions/workflows/android.yml)
[![Kotlin Version](https://img.shields.io/badge/Kotlin-1.9.24-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Gradle Version](https://img.shields.io/badge/Gradle-8.4-green.svg?logo=gradle)](https://gradle.org)
[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84.svg?logo=android&logoColor=white)](https://developer.android.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 🇮🇩 Tentang SeNutri
**SeNutri** adalah aplikasi Android berbasis gizi dan kesehatan yang dirancang untuk mempermudah pemantauan, perencanaan, dan pelacakan pola makan sehat sehari-hari. Dengan antarmuka yang modern dan interaktif, SeNutri membantu pengguna mengelola asupan kalori dan nutrisi mereka secara cerdas guna mencapai gaya hidup yang lebih sehat.

## 🇬🇧 About SeNutri
**SeNutri** is a nutrition and health-focused Android application designed to simplify daily meal tracking, planning, and healthy eating patterns. Featuring a modern and interactive UI, SeNutri helps users manage their calorie and nutrient intake smartly to achieve a healthier lifestyle.

---

## ✨ Fitur Utama / Key Features

*   **🔐 Autentikasi Keamanan (Secure Authentication):** Registrasi dan masuk akun yang aman menggunakan alamat email (dilengkapi verifikasi email) serta dukungan integrasi Google Sign-In.
*   **📋 Kuesioner Gizi Personal (Nutritional Assessment):** Kuesioner komprehensif pada awal penggunaan untuk menganalisis kebutuhan nutrisi (BMR & TDEE) secara personal berdasarkan data tubuh pengguna.
*   **🍽️ Logger Makanan Harian (Daily Meal Logger):** Catat asupan makanan harian (Sarapan, Makan Siang, Makan Malam, Camilan) untuk melacak total konsumsi kalori secara berkala.
*   **📊 Riwayat & Tren Nutrisi (Nutrition History & Trends):** Visualisasi data riwayat makan yang rapi untuk memantau perkembangan pola diet Anda dari hari ke hari.
*   **🔄 Sinkronisasi Otomatis (Background Sync):** Menggunakan Jetpack WorkManager untuk melakukan sinkronisasi data asupan secara otomatis ke cloud pada tengah malam secara berkala.
*   **⚙️ Manajemen Profil & Password (Profile & Password Management):** Perbarui informasi profil pengguna dan ubah kata sandi dengan aman secara langsung dari dalam aplikasi.

---

## 🛠️ Teknologi & Pustaka / Tech Stack

*   **Language:** [Kotlin](https://kotlinlang.org) — Bahasa pemrograman modern utama untuk pengembangan aplikasi Android.
*   **UI Architecture:** MVVM (Model-View-ViewModel) dengan [ViewBinding](https://developer.android.com/topic/libraries/view-binding) & [DataBinding](https://developer.android.com/topic/libraries/data-binding) untuk kode antarmuka yang bersih dan terstruktur.
*   **Local Storage:** SharedPreferences (Session Management) untuk penyimpanan status login pengguna secara instan.
*   **Networking:** [Retrofit 2](https://square.github.io/retrofit/) & [Gson](https://github.com/google/gson) — Untuk memuat dan mengurai data menu makanan statis dari API/CDN.
*   **Image Loading:** [Glide](https://github.com/bumptech/glide) — Pemuatan gambar makanan dan profil secara asinkronus dan efisien.
*   **Background Processing:** [Jetpack WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) — Untuk eksekusi tugas background yang andal dan terjadwal (sinkronisasi volume harian).
*   **Database & Backend:**
    *   [Firebase Auth](https://firebase.google.com/docs/auth) — Layanan autentikasi pengguna.
    *   [Firebase Firestore](https://firebase.google.com/docs/firestore) — Database NoSQL real-time untuk menyimpan data profil gizi dan riwayat makan.
    *   [Firebase Storage](https://firebase.google.com/docs/storage) — Penyimpanan media (foto profil).
*   **UI Components:** [CurvedBottomNavigation](https://github.com/qamarelsafadi/CurvedBottomNavigation) — Komponen navigasi bawah berbentuk melengkung yang estetik dan interaktif.

---

## 📂 Struktur Proyek / Project Structure

```text
SeNutri/
├── .github/workflows/          # Konfigurasi CI/CD (GitHub Actions)
├── app/                        # Modul aplikasi utama
│   ├── src/main/
│   │   ├── java/com/example/covary/
│   │   │   ├── about/          # Aktivitas informasi tim
│   │   │   ├── adapterHistory/ # Adapter untuk riwayat asupan makan
│   │   │   ├── adapterMakanan/ # Adapter untuk daftar makanan
│   │   │   ├── assessment/     # Kuesioner kalkulator gizi BMR/TDEE
│   │   │   ├── fragments/      # Fragment halaman utama & menu
│   │   │   ├── imgurapi/       # Integrasi opsional API Imgur
│   │   │   ├── kuisioner/      # Pertanyaan dasar kuisioner aktivitas fisik
│   │   │   ├── login/          # Alur login, verifikasi, dan SessionManager
│   │   │   ├── myaplikasi/     # Konfigurasi inisialisasi awal aplikasi
│   │   │   ├── register/       # Alur pembuatan akun baru
│   │   │   ├── retrofit/       # Pengaturan API makanan eksternal
│   │   │   ├── slider/         # Adapter slider untuk info kesehatan
│   │   │   ├── splashscreen/   # Splash screen aplikasi
│   │   │   └── worker/         # WorkManager untuk penyimpanan harian otomatis
│   │   └── res/                # Aset tata letak, gambar, nilai string & tema
│   └── build.gradle.kts        # Konfigurasi build level modul
├── gradle/                     # Berkas pembungkus Gradle (libs.versions.toml)
├── build.gradle.kts            # Konfigurasi build tingkat proyek
└── settings.gradle.kts         # Definisi subproyek gradle
```

---

## 🚀 Setup & Instalasi Lokal / Local Setup

Ikuti langkah berikut untuk menjalankan aplikasi SeNutri di komputer lokal Anda:

### 1. Prasyarat (Prerequisites)
*   [Android Studio (Koala atau versi lebih baru)](https://developer.android.com/studio)
*   Java Development Kit (JDK) 17
*   Perangkat fisik Android atau Emulator Android dengan API Level 24 (Android 7.0 Nougat) ke atas.

### 2. Kloning Repositori (Clone Repository)
```bash
git clone https://github.com/RyanHidayat058/SeNutri.git
cd SeNutri
```

### 3. Konfigurasi Firebase (Firebase Setup)
1. Buat proyek baru di [Firebase Console](https://console.firebase.google.com/).
2. Daftarkan aplikasi Android Anda dengan nama paket (*package name*) `com.example.covary`.
3. Unduh file `google-services.json` dan letakkan di dalam folder `app/` proyek Anda:
   ```text
   SeNutri/app/google-services.json
   ```
4. Aktifkan layanan **Authentication (Email/Password & Google)**, **Cloud Firestore Database**, dan **Cloud Storage** pada konsol Firebase Anda.

### 4. Konfigurasi Kredensial Keystore (Release Keystore Config)
Untuk keamanan, jangan menulis kata sandi keystore secara langsung di `app/build.gradle.kts`.  
1. Salin berkas `local.properties.example` menjadi `local.properties`:
   ```bash
   cp local.properties.example local.properties
   ```
2. Buka berkas `local.properties` yang baru dibuat dan isi kredensial keystore release Anda di sana:
   ```properties
   RELEASE_KEYSTORE_FILE=release.keystore
   RELEASE_KEYSTORE_PASSWORD=password_keystore_anda
   RELEASE_KEY_ALIAS=covary-release
   RELEASE_KEY_PASSWORD=password_key_anda
   ```

### 5. Build dan Jalankan Aplikasi (Build & Run)
*   Buka Android Studio, pilih **Open** dan arahkan ke direktori root `SeNutri`.
*   Biarkan Gradle mensinkronisasi dependensi proyek secara otomatis.
*   Pilih perangkat target Anda (emulator atau HP fisik) lalu klik tombol **Run** (Ikon Segitiga Hijau) di toolbar Android Studio, atau jalankan melalui terminal:
    ```bash
    ./gradlew installDebug
    ```

---

## 👥 Tim Pengembang / The Team

Aplikasi ini dikembangkan sebagai bagian dari proyek perkuliahan/kolaborasi oleh tim kami:

| NPM | Nama / Name | Peran / Role | GitHub Profile |
| :--- | :--- | :--- | :--- |
| **1402021058** | **Ryan Hidayat** | Project Lead & Developer | [@RyanHidayat058](https://github.com/RyanHidayat058) |
| **1402021017** | **Dikco Agung Prasetyo** | Core Android Developer | [@dikcoap](https://github.com/dikcoap) |
| **1402021033** | **Muhamad Ivan Fadillah** | Backend Integration & API Developer | [@ivanfdillah](https://github.com/ivanfdillah) |

---

## 📄 Lisensi / License
Proyek ini dilisensikan di bawah **MIT License**. Lihat berkas [LICENSE](LICENSE) untuk informasi aturan hukum penggunaan kode lebih lanjut.
