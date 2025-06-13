# 🩺 Sistem Prediksi Diabetes

Aplikasi ini adalah sistem berbasis Java yang dibuat untuk memprediksi risiko diabetes menggunakan dataset medis dari wanita Pima Indian. Sistem ini dikembangkan dengan pendekatan machine learning sederhana namun efektif: regresi logistik manual + UI modern JavaFX.

---

## 🔍 Gambaran Umum

Saat ini, penyakit diabetes semakin banyak ditemukan di masyarakat. Aplikasi ini mencoba membantu dalam proses skrining awal dengan membuat prediksi risiko diabetes berdasarkan data medis pasien.

Dataset yang digunakan berasal dari **Pima Indian Diabetes Dataset**, yang biasa dipakai sebagai benchmark dalam penelitian klasifikasi medis.  
Dengan menggunakan metode regresi logistik dan antarmuka grafis JavaFX, aplikasi ini tidak hanya bisa memprediksi, tapi juga menampilkan informasi statistik dan visualisasi yang informatif.

---

## 🧠 Fitur Utama

Apa saja fitur utama dari aplikasi ini?

- **Model Regresi Logistik**: Dibuat dari nol tanpa library ML eksternal
- **Analisis Data**: Statistik dasar, distribusi, dan korelasi antar-fitur
- **Antarmuka Pengguna (UI)**: Antarmuka interaktif berbasis JavaFX
- **Evaluasi Model**: Akurasi, presisi, recall, F1-score
- **Normalisasi Z-Score**: Menyamakan skala tiap fitur agar model lebih stabil
- **Tema Mode Terang/Gelap**: Desain UI yang nyaman dan segar
- **Animasi Halus**: Transisi dan efek visual untuk pengalaman pengguna yang lebih baik

---

## 📋 Informasi Dataset

Setiap pasien dalam dataset memiliki 8 fitur penting:

| Fitur | Deskripsi |
|-------|-----------|
| Jumlah Kehamilan | Berapa kali wanita tersebut pernah hamil |
| Glukosa | Kadar gula darah plasma (mg/dL) |
| Tekanan Darah | Tekanan darah diastolik (mm Hg) |
| Ketebalan Kulit | Ketebalan lipatan kulit triceps (mm) |
| Insulin | Serum insulin 2 jam setelah tes (mu U/ml) |
| BMI | Indeks massa tubuh (kg/m²) |
| Fungsi Silsilah | Skor genetik riwayat diabetes keluarga |
| Usia | Usia pasien dalam tahun |

> Setiap fitur ini punya kontribusi berbeda dalam memprediksi apakah seorang pasien menderita diabetes atau tidak.

---

## ⚙️ Cara Kerja Aplikasi

### Alur Aplikasi:
```
[Muat Dataset] → [Hitung Rata-rata & Deviasi Standar] → [Train Model Regresi Logistik] → [Prediksi Pasien Baru]
```

#### Tahapan Detail:
1. **Preprocessing**
   - Semua fitur dinormalisasi dengan Z-Score
   - Nilai 0 (invalid) di beberapa kolom diabaikan saat pelatihan

2. **Pelatihan Model**
   - Menggunakan algoritma gradient descent
   - Output: bobot dan bias model yang siap digunakan

3. **Prediksi**
   - Masukkan data pasien baru
   - Model menghitung probabilitas diabetes
   - Jika > 50%, prediksi "DIABETES DETECTED"

4. **Visualisasi**
   - Tabel dataset lengkap
   - Grafik distribusi diabetes
   - Hasil prediksi real-time

---

## 🎯 Contoh Prediksi

Ketika aplikasi dijalankan, Anda bisa langsung memasukkan data pasien baru untuk melihat apakah ia berisiko menderita diabetes.

Contoh input:

```
Jumlah Kehamilan     : 2
Glukosa               : 120
Tekanan Darah         : 70
Ketebalan Kulit       : 30
Insulin               : 100
BMI                   : 25.5
Fungsi Silsilah       : 0.5
Usia                  : 35
```

Output prediksi:
```
Risiko Diabetes: 45.67%
Prediksi: TIDAK ADA DIABETES
Kepercayaan: 54.33%
```

➡️ Ini berarti pasien cenderung sehat, tetapi masih ada risiko ~45% jika faktor lain berubah.

---

## 🖥️ Tampilan Antarmuka

### Desain UI
- Warna biru lembut dan putih memberikan nuansa profesional
- Font Segoe UI / Arial untuk tampilan yang bersih
- Tombol interaktif dan progress bar untuk confidence level

### Tab Aplikasi
1. **Prediction**: Form input pasien dan hasil prediksi
2. **About**: Informasi tentang dataset dan model
3. **View Dataset**: Tabel pasien dan grafik distribusi diabetes

---

## 📊 Performa Model

Hasil evaluasi model menunjukkan akurasi rata-rata:

| Metrik | Nilai |
|--------|-------|
| Akurasi | ±75–80% |
| Presisi | ±70–75% |
| Recall | ±60–70% |
| F1-Score | ±65–72% |

➡️ Angka ini cukup baik untuk model regresi logistik yang dilatih secara manual.

---

## 🛠 Teknologi & Tools

- **Java 11+** – Bahasa pemrograman utama
- **JavaFX 17** – Untuk antarmuka pengguna
- **Maven** – Manajemen dependensi dan build
- **CSS3** – Styling UI
- **Apache Commons Math (opsional)** – Bantuan komputasi matematika

---

## 🧪 Parameter Pelatihan Model

Model dilatih dengan parameter berikut:

| Nama | Nilai |
|------|-------|
| Learning Rate | 0.01 |
| Iterasi Maksimum | 1000 |
| Threshold Konvergensi | 1e-6 |

Fitur normalisasi Z-Score digunakan untuk menyamakan skala semua variabel sebelum masuk ke model.

---



File penting:
- `DiabetesPredictor.java`: Inti dari model prediksi
- `Patient.java`: Model data pasien
- `DataLoader.java`: Muat dataset CSV
- `ModelTrainer.java`: Latih model regresi logistik
- `DiabetesPredictionUI.java`: Antarmuka pengguna JavaFX

---

## 📈 Statistik Dataset

Dataset berisi ±768 record pasien wanita Pima Indian. Dari jumlah itu:
- Sekitar 34.9% pasien menderita diabetes
- Rata-rata kadar glukosa pada pasien diabetes = 141.28 mg/dL
- Rata-rata usia pasien diabetes = 37 tahun

➡️ Korelasi tertinggi dengan diabetes adalah kadar gula darah (`Glucose`) dengan nilai ~0.467 (korelasi moderat)

---

## 🧩 Bagaimana Proses Normalisasi Dilakukan?

Sebelum model bisa memproses data, kita lakukan **normalisasi Z-Score**:

```text
Z = (X - Rata-Rata) / Deviasi Standar
```

Contoh:
- Glukosa pasien = 148 mg/dL
- Rata-rata glukosa = 121.7 mg/dL
- Deviasi standar = 30.51 mg/dL

```text
Z = (148 - 121.7) / 30.51 ≈ 0.86
```

➡️ Nilai ini kemudian dimasukkan ke dalam model regresi logistik.

---

## 🧮 Rumus Prediksi

Model menggunakan rumus regresi logistik:

```
z = w₀×f₀ + w₁×f₁ + ... + w₇×f₇ + bias
probabilitas_diabetes = 1 / (1 + e^(-z))
```

Jika probabilitas ≥ 0.5 → prediksi: **DIABETES DETECTED**

---


## 📦 Sumber Dataset

Dataset asli berasal dari National Institute of Diabetes and Digestive and Kidney Diseases (NIDDK). 
Berikut Link Kaggle Dataset Pima Indians Diabetes Database 
https://www.kaggle.com/datasets/uciml/pima-indians-diabetes-database

---




## 🗂️ File: Patient.java

Berikut adalah salah satu bagian kode yang merepresentasikan objek pasien:

```java
package com.diabetes.prediction.model;

/**
 * Model data pasien yang merepresentasikan 
 * satu baris dari dataset Pima Indian
 */
public class Patient {
    private int pregnancies;
    private double glucose;
    private double bloodPressure;
    private double skinThickness;
    private double insulin;
    private double bmi;
    private double diabetesPedigreeFunction;
    private int age;
    private int outcome; // 0 = tidak, 1 = ya

    public Patient(int pregnancies, double glucose, double bloodPressure, 
                   double skinThickness, double insulin, double bmi, 
                   double diabetesPedigreeFunction, int age, int outcome) {
        this.pregnancies = pregnancies;
        this.glucose = glucose;
        this.bloodPressure = bloodPressure;
        this.skinThickness = skinThickness;
        this.insulin = insulin;
        this.bmi = bmi;
        this.diabetesPedigreeFunction = diabetesPedigreeFunction;
        this.age = age;
        this.outcome = outcome;
    }

    /**
     * Mengembalikan semua fitur pasien dalam bentuk array
     * Digunakan untuk pelatihan dan prediksi model
     */
    public double[] getFeatures() {
        return new double[] {
            pregnancies, glucose, bloodPressure, skinThickness,
            insulin, bmi, diabetesPedigreeFunction, age
        };
    }
    
    // Getter dan setter untuk setiap field
}
```

➡️ Kelas ini menjadi fondasi dari seluruh operasi prediksi dan pelatihan.

---

## 🧪 Unit Testing

Aplikasi ini sudah dilengkapi unit testing untuk memastikan model bekerja dengan benar:
- Memastikan prediksi konsisten
- Menjamin akurasi output
- Memvalidasi bahwa preprocessing berjalan lancar

---

## 📅 Changelog

### v1.0.0
- ✅ Regresi logistik manual
- ✅ Antarmuka JavaFX modern
- ✅ Analisis dataset
- ✅ Prediksi interaktif
- ✅ Animasi UI
- ✅ Tema gelap dan terang
- ✅ Unit test dan dokumentasi

---

##  Kesimpulan

Aplikasi ini adalah proyek pembelajaran yang membantu mahasiswa memahami cara kerja model regresi logistik secara manual. Meskipun implementasinya sederhana, aplikasi ini cukup efektif untuk memprediksi risiko diabetes dan memberikan insight medis yang berguna.

Selain itu, antarmuka JavaFX memberikan pengalaman pengguna yang modern dan responsif, sehingga aplikasi ini bisa digunakan oleh dokter atau tenaga kesehatan untuk skrining awal.
