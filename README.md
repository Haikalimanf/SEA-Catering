# SEA Catering

## Deskripsi

SEA Catering adalah sebuah layanan katering harian modern yang menyediakan berbagai pilihan paket makanan sehat, lezat, dan bergizi yang dapat disesuaikan dengan kebutuhan pelanggan. Layanan ini dirancang untuk memudahkan pelanggan dalam memenuhi kebutuhan konsumsi sehari-hari, baik untuk individu, keluarga, maupun perusahaan.

SEA Catering mengusung konsep berlangganan (subscription) dengan fitur fleksibel seperti pilihan jenis makanan, hari pengiriman, hingga jeda langganan. Sistem digitalnya memungkinkan pengguna untuk memesan, mengelola, dan memantau status langganan mereka langsung dari aplikasi, menjadikan pengalaman katering lebih praktis, transparan, dan personal.

## Fitur

### Selamat Datang

**Splash Screen & Onboarding**  
Membuat kesan pertama yang baik dan memperkenalkan pengunjung kepada siapa kami dan apa yang kami lakukan.

| Splash | Onboarding 1 | Onboarding 2 | Onboarding 3 |
|--------|---------------|---------------|---------------|
| ![](https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/splash_screen_sea_catering.jpg) | ![](https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/onboarding_page1_sea_catering.jpg) | ![](https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/onboarding_page2_sea_catering.jpg) | ![](https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/onboarding_page3_sea_catering.jpg) |


### 👤 Pengguna Biasa

#### 🔐 User Authentication & Authorization  
Login/register dengan email atau akun Google.

| Login | Register |
|-------|----------|
| ![](https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/login_sea_catering.jpg) | ![](https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/register_sea_catering.jpg) |

#### 🧭 Interactive Navigation  
Antarmuka intuitif untuk menjelajahi paket katering.

| Home | Menu | Subscription Form |
|------|------|-------------------|
| ![](https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/home_sea_catering.jpg) | ![](https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/menu_sea_catering.jpg) | ![](https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/subcription_sea_catering.jpg) |

#### 📝 Add Testimonial  
Memberikan ulasan dan rating terhadap layanan.

<img src="https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/add_review_sea_catering.jpg" width="300">

#### 📆 User Can Subscribe  
Form untuk memilih paket, hari pengiriman, dan data pribadi pengguna.

<img src="https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/subcription_sea_catering.jpg" width="300">

#### 📆 View, Pause & Cancel Subscription  
Fleksibilitas dalam mengelola langganan aktif langsung dari dashboard.

<img src="https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/dashboard_user_have_subs_sea_catering.jpg" width="300">

### 🛠️ Admin

#### 📊 Admin Dashboard  
Melihat statistik seperti new subscriptions, MRR, growth, dan reaktivasi.

<img src="https://raw.githubusercontent.com/Haikalimanf/AssetFotoIconSalon/refs/heads/main/admin_dashboard_sea_catering.jpg" width="300">

#### 📄 Generate Report PDF  
Fitur untuk membuat laporan bulanan dalam format PDF.


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

### Download App
- Link Drive Download App

### 🔐 Admin & User Login Credentials
#### 👤 Akun Pengguna Biasa
- Email: `user@gmail.com`
- Password: `User123!`

#### 🛠️ Akun Admin
- Email: `admin@gmail.com`
- Password: `Admin123!`

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

## Contact

Email: haikalimanf@gmail.com
GitHub: [haikalimanf](https://github.com/Haikalimanf)
