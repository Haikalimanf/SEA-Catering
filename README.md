# SEA Catering

## Description

SEA Catering adalah sebuah layanan katering harian modern yang menyediakan berbagai pilihan paket makanan sehat, lezat, dan bergizi yang dapat disesuaikan dengan kebutuhan pelanggan. Layanan ini dirancang untuk memudahkan pelanggan dalam memenuhi kebutuhan konsumsi sehari-hari, baik untuk individu, keluarga, maupun perusahaan.

SEA Catering mengusung konsep berlangganan (subscription) dengan fitur fleksibel seperti pilihan jenis makanan, hari pengiriman, hingga jeda langganan. Sistem digitalnya memungkinkan pengguna untuk memesan, mengelola, dan memantau status langganan mereka langsung dari aplikasi, menjadikan pengalaman katering lebih praktis, transparan, dan personal.

## Features

### Pengguna Biasa
- **User Authentication & Authorization** - Login/register dengan email atau Google account

<img src="https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/login_sea_catering.jpg" alt="Deskripsi gambar" height="450" style="margin-right: 10px;"><img src="https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/register_sea_catering.jpg" alt="Deskripsi gambar" height="450">

- **Interactive Navigation** - Antarmuka yang mudah digunakan untuk menjelajahi paket katering

<img src="https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/home_sea_catering.jpg" alt="Deskripsi gambar" height="450" style="margin-right: 10px;"><img src="https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/menu_sea_catering.jpg" alt="Deskripsi gambar" height="450" style="margin-right: 10px;"><img src="https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/subcription_sea_catering.jpg" alt="Deskripsi gambar" height="450">

- **Add Testimonial** - Memberikan ulasan dan rating terhadap layanan yang diterima
<img src="https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/add_review_sea_catering.jpg" alt="Deskripsi gambar" height="450">

- **Create, Pause, and Cancel Subscription** - Fleksibilitas dalam mengelola langganan katering

<img src="https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/subcription_sea_catering.jpg" alt="Deskripsi gambar" height="450" style="margin-right: 10px;"><img src="https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/dashboard_user_have_subs_sea_catering.jpg" alt="Deskripsi gambar" height="450">

### Admin
- **Admin Dashboard** - Monitoring new subscriptions, Monthly Recurring Revenue (MRR), reactivations, dan subscription growth
<img src="https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/admin_dashboard_sea_catering.jpg" alt="Deskripsi gambar" height="450">

- **Create Report PDF** - Pembuatan laporan dalam format PDF

## Architecture & Tech Stack

- **Bahasa Pemrograman**: Kotlin
- **Navigasi**: Jetpack Navigation
- **Authentication & Database**: Firebase Auth & Firestore
- **Dependency Injection**: Dagger Hilt
- **Arsitektur**: MVVM (Model-View-ViewModel)

## Installation & Getting Started

### Prerequisites
- Android Studio (versi terbaru - Koala | 2024.1.1 atau lebih baru)
- JDK 17 atau lebih baru
- Android SDK (minimal SDK 24)
- Perangkat Android atau Emulator dengan minimal API 24

### Clone Repository
```bash
git clone https://github.com/Haikalimanf/SEA-Catering.git
cd SEA-Catering
```

### Firebase Setup, jika tidak tersambung Firebase
1. Buat project baru di [Firebase Console](https://console.firebase.google.com/)
2. Tambahkan aplikasi Android dengan package name yang sesuai
3. Download file `google-services.json` dan tempatkan di direktori `/app`
4. Aktifkan Firebase Authentication (Email/Password dan Google Sign-In)
5. Buat database Firestore dengan aturan keamanan yang sesuai

### Running the App With Android Studio
1. Buka project dengan Android Studio
2. Sync project dengan Gradle files
3. Pilih perangkat atau emulator Android
4. Klik tombol Run

### Use Emulator Setup With Android Studio
1. Di Android Studio, buka Device Manager
2. Pilih "Create Device"
3. Pilih perangkat yang diinginkan (misal: Pixel 6)
4. Pilih sistem Android (rekomendasi: Android 13.0 atau lebih baru)
5. Selesaikan setup dan jalankan emulator

### Download App if you want running the app with Android Studio or use Emulator
- [Link Drive Download App](https://drive.google.com/drive/folders/1GKUBJFreWfvS_PmzY6zampdeVcz676KB?usp=sharing)

## Project Structure
```
app/
├── build.gradle.kts       # Konfigurasi Gradle untuk modul app
├── google-services.json   # Konfigurasi Firebase
├── src/
│   ├── main/
│   │   ├── java/         # Kode sumber Kotlin
│   │   ├── res/          # Resources (layout, drawable, values, dll)
│   │   └── AndroidManifest.xml
│   ├── test/             # Unit tests
│   └── androidTest/      # Instrumented tests
```

## User Interface

Aplikasi ini menyediakan antarmuka pengguna yang intuitif dengan komponen-komponen berikut:
- Halaman register dan login dengan opsi email dan Google Sign-In
- Dashboard untuk melihat dan mengelola langganan
- Form pemesanan langganan dengan berbagai opsi paket
- Panel admin untuk monitoring bisnis

## Contact

Email: haikalimanf@gmail.com
GitHub: [haikalimanf](https://github.com/Haikalimanf)
