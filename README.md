# Sistem Prediksi Diabetes

Aplikasi berbasis Java dengan machine learning untuk memprediksi diabetes menggunakan Dataset Diabetes Pima Indian.

## Gambaran Umum

Aplikasi ini menggunakan regresi logistik dengan gradient descent untuk memprediksi apakah seorang pasien menderita diabetes berdasarkan pengukuran diagnostik. Dataset berisi informasi tentang wanita Pima Indian berusia 21 tahun ke atas.

## Fitur Utama

- **Model Machine Learning**: Implementasi regresi logistik dari awal
- **Analisis Data**: Analisis dataset komprehensif dan statistik
- **Prediksi Interaktif**: Antarmuka grafis modern untuk membuat prediksi
- **Evaluasi Model**: Metrik performa termasuk akurasi, presisi, recall, dan F1-score
- **Normalisasi Data**: Normalisasi z-score untuk performa model yang lebih baik
- **Mode Gelap/Terang**: Dukungan tema dengan desain modern
- **Animasi Halus**: Transisi dan animasi untuk pengalaman pengguna yang lebih baik

## Fitur Dataset

Model menggunakan 8 fitur diagnostik:
1. **Kehamilan**: Jumlah kali hamil
2. **Glukosa**: Konsentrasi glukosa plasma (mg/dL)
3. **Tekanan Darah**: Tekanan darah diastolik (mm Hg)
4. **Ketebalan Kulit**: Ketebalan lipatan kulit trisep (mm)
5. **Insulin**: Insulin serum 2 jam (mu U/ml)
6. **BMI**: Indeks massa tubuh (berat dalam kg/(tinggi dalam m)^2)
7. **Fungsi Silsilah Diabetes**: Fungsi silsilah diabetes
8. **Usia**: Usia dalam tahun


## Cara Menjalankan

### Prasyarat
- Java 11 atau lebih tinggi
- Maven 3.6 atau lebih tinggi

### Langkah-langkah

1. **Clone atau unduh proyek**

2. **Kompilasi proyek**:
   \`\`\`bash
   mvn clean compile
   \`\`\`

3. **Jalankan aplikasi**:
   \`\`\`bash
   mvn javafx:run
   \`\`\`

4. **Atau build dan jalankan JAR**:
   \`\`\`bash
   mvn clean package
   java -jar target/diabetes-prediction-system-1.0.0.jar
   \`\`\`

### Menjalankan Test
\`\`\`bash
mvn test
\`\`\`

## Penggunaan

Ketika Anda menjalankan aplikasi, aplikasi akan:

1. **Memuat dataset** dari \`data/diabetes.csv\`
2. **Menampilkan statistik dataset** termasuk distribusi kasus diabetes
3. **Melatih model regresi logistik** menggunakan gradient descent
4. **Menampilkan metrik performa model** (akurasi, presisi, recall, F1-score)
5. **Memulai mode prediksi interaktif** dengan antarmuka grafis modern

### Membuat Prediksi

Dalam antarmuka grafis, masukkan data pasien pada form yang tersedia:

\`\`\`
=== Informasi Pasien ===
Jumlah Kehamilan: 2
Tingkat Glukosa: 120
Tekanan Darah: 70
Ketebalan Kulit: 30
Tingkat Insulin: 100
BMI: 25.5
Fungsi Silsilah Diabetes: 0.5
Usia: 35

--- Hasil Prediksi ---
Risiko Diabetes: 45.67%
Prediksi: TIDAK ADA DIABETES
Kepercayaan: 54.33%
\`\`\`

## Fitur Antarmuka

### 🎨 Desain Modern
- **Tema Terang/Gelap**: Toggle antara mode terang dan gelap
- **Animasi Halus**: Transisi dan efek hover yang menarik
- **Desain Responsif**: Menyesuaikan dengan ukuran layar
- **Warna yang Nyaman**: Palet warna yang enak dipandang mata

### 📊 Visualisasi Data
- **Tabel Dataset**: Tampilan data dalam format tabel yang rapi
- **Grafik Distribusi**: Visualisasi distribusi kasus diabetes
- **Statistik Real-time**: Informasi statistik dataset yang komprehensif

### 🔮 Prediksi Interaktif
- **Form Input Modern**: Field input dengan validasi
- **Hasil Visual**: Tampilan hasil dengan warna dan animasi
- **Progress Bar**: Indikator kepercayaan prediksi
- **Status Real-time**: Informasi status aplikasi

## Performa Model

Model regresi logistik biasanya mencapai:
- **Akurasi**: ~75-80%
- **Presisi**: ~70-75%
- **Recall**: ~60-70%
- **F1-Score**: ~65-72%

Performa dapat bervariasi tergantung pada dataset spesifik dan parameter pelatihan.

## Detail Teknis

### Algoritma
- **Regresi Logistik** dengan optimisasi gradient descent
- **Normalisasi Fitur** menggunakan standardisasi z-score
- **Aktivasi Sigmoid** untuk output probabilitas
- **Fungsi Loss Cross-entropy**

### Parameter Pelatihan
- Learning Rate: 0.01
- Iterasi Maksimum: 1000
- Threshold Konvergensi: 1e-6

### Teknologi
- **Java 11+**: Bahasa pemrograman utama
- **JavaFX 17**: Framework antarmuka pengguna
- **Maven**: Build tool dan dependency management
- **CSS3**: Styling untuk antarmuka modern

## Kontribusi

1. Fork repository
2. Buat feature branch
3. Lakukan perubahan
4. Tambahkan test untuk fitur baru
5. Submit pull request

## Lisensi

Proyek ini adalah open source dan tersedia di bawah Lisensi MIT.

## Sumber Dataset

Dataset Diabetes Pima Indian berasal dari National Institute of Diabetes and Digestive and Kidney Diseases.

## Dukungan

Jika Anda mengalami masalah atau memiliki pertanyaan, silakan buat issue di repository ini.

## Changelog

### Versi 1.0.0
- ✅ Implementasi model regresi logistik
- ✅ Antarmuka JavaFX modern dengan tema gelap/terang
- ✅ Analisis dataset komprehensif
- ✅ Sistem prediksi interaktif
- ✅ Animasi dan transisi halus
- ✅ Dukungan Bahasa Indonesia
- ✅ Unit testing
- ✅ Dokumentasi lengkap

## Roadmap

### Versi 1.1.0 (Planned)
- 🔄 Dashboard visualisasi data lanjutan
- 🔄 Perbandingan multiple algoritma ML
- 🔄 Export hasil ke PDF/CSV
- 🔄 Sistem manajemen profil pengguna
- 🔄 Validasi input real-time

### Versi 1.2.0 (Future)
- 🔄 API REST untuk integrasi
- 🔄 Database support
- 🔄 Model ensemble
- 🔄 Deployment ke cloud
  \`\`\`

Saya juga akan memperbarui beberapa komentar dalam kode yang user-facing:

```typescriptreact file="src/main/java/com/diabetes/prediction/model/Patient.java"
[v0-no-op-code-block-prefix]package com.diabetes.prediction.model;

/**
 * Model data pasien yang merepresentasikan fitur dari Dataset Diabetes Pima Indian
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
    private int outcome; // 0 = no diabetes, 1 = diabetes
    
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
    
    // Konversi data pasien ke array fitur untuk model ML
    public double[] getFeatures() {
        return new double[] {
            pregnancies, glucose, bloodPressure, skinThickness,
            insulin, bmi, diabetesPedigreeFunction, age
        };
    }
    
    // Getters and Setters
    public int getPregnancies() { return pregnancies; }
    public void setPregnancies(int pregnancies) { this.pregnancies = pregnancies; }
    
    public double getGlucose() { return glucose; }
    public void setGlucose(double glucose) { this.glucose = glucose; }
    
    public double getBloodPressure() { return bloodPressure; }
    public void setBloodPressure(double bloodPressure) { this.bloodPressure = bloodPressure; }
    
    public double getSkinThickness() { return skinThickness; }
    public void setSkinThickness(double skinThickness) { this.skinThickness = skinThickness; }
    
    public double getInsulin() { return insulin; }
    public void setInsulin(double insulin) { this.insulin = insulin; }
    
    public double getBmi() { return bmi; }
    public void setBmi(double bmi) { this.bmi = bmi; }
    
    public double getDiabetesPedigreeFunction() { return diabetesPedigreeFunction; }
    public void setDiabetesPedigreeFunction(double diabetesPedigreeFunction) { 
        this.diabetesPedigreeFunction = diabetesPedigreeFunction; 
    }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public int getOutcome() { return outcome; }
    public void setOutcome(int outcome) { this.outcome = outcome; }
    
    @Override
    public String toString() {
        return String.format("Pasien{kehamilan=%d, glukosa=%.1f, tekananDarah=%.1f, " +
                           "ketebalanKulit=%.1f, insulin=%.1f, bmi=%.1f, dpf=%.3f, usia=%d, hasil=%d}",
                           pregnancies, glucose, bloodPressure, skinThickness, 
                           insulin, bmi, diabetesPedigreeFunction, age, outcome);
    }
}
