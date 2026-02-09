# 🚗 BiTurVer - Yapay Zeka Destekli Akıllı Araç Kiralama ve Hasar Tespit Platformu

BiTurVer, geleneksel araç kiralama süreçlerindeki güven ve şeffafiyet sorunlarını çözmek amacıyla geliştirilmiş, yapay zeka tabanlı yenilikçi bir platformdur. Kullanıcılar, mobil uygulama ve web arayüzü üzerinden araç kiralayabilir, kiralama öncesi ve sonrası araç durumunu AI destekli görüntü işleme teknolojisi ile anlık olarak analiz edebilirler.
## 🌟 Proje Özellikleri
### 🧠 Yapay Zeka Destekli Hasar Analizi
*   **Otomatik Hasar Tespiti:** Kullanıcıların yüklediği araç fotoğrafları, MobileNetV2 tabanlı derin öğrenme modelimiz ile analiz edilir.
*   **Anlık Raporlama:** Hasarın türü, konumu ve tahmini onarım maliyeti saniyeler içinde raporlanır.
*   **Transfer Learning:** Model, yüksek doğruluk oranı için geniş bir veri seti üzerinde transfer öğrenme yöntemiyle eğitilmiştir.
### 📱 Mobil Uygulama (Android)
*   **Kullanıcı Dostu Arayüz:** Modern ve sezgisel tasarım ile kolay araç kiralama deneyimi.
*   **Sanal Asistan (Chatbot):** 7/24 hizmet veren yapay zeka asistanı ile sorularınıza anında yanıt.
*   **Güvenli Kimlik Doğrulama:** OCR teknolojisi ile ehliyet tarama ve doğrulama işlemleri.
*   **Geçmiş İşlemler:** Kiralama ve hasar geçmişine kolay erişim.
### 🌐 Web Yönetim Paneli
*   **Responsive Tasarım:** Masaüstü ve mobil cihazlarla tam uyumlu modern web arayüzü.
*   **Filo Yönetimi:** Araçların doluluk durumu, bakımları ve kiralama süreçlerinin takibi.
*   **AI Entegrasyonu:** TensorFlow.js ile tarayıcı üzerinde model çalıştırma yeteneği.
---
## 📸 Ekran Görüntüleri ve Arayüz
Bu bölümde uygulamanın ve web sitesinin arayüzünden görseller yer almaktadır.
### 📱 Mobil Uygulama
| Ana Ekran | Hasar Tespiti | Profil Sayfası |
| :---: | :---: | :---: |
<img width="590" height="1314" alt="image" src="https://github.com/user-attachments/assets/b912e43c-90c3-4fc1-81ed-1d236a1b809a" />
<img src="https://via.placeholder.com/200x400?text=Ana+Ekran" alt="Ana Ekran" width="200"/> | <img width="573" height="1301" alt="image" src="https://github.com/user-attachments/assets/089c6187-c568-4371-97a4-a544b82de85f" />
 <img src="https://via.placeholder.com/200x400?text=Hasar+Tespiti" alt="Hasar Tespiti" width="200"/> | <img width="575" height="1314" alt="image" src="https://github.com/user-attachments/assets/59bf36e0-39bb-439f-a100-4ebc1bd05949" />
 <img src="https://via.placeholder.com/200x400?text=Profil" alt="Profil" width="200"/> |



### 💻 Web Arayüzü
<img width="2543" height="1283" alt="image" src="https://github.com/user-attachments/assets/d9c942af-37b7-4477-9b11-63b5ce73f46d" />

## 🏗️ Teknik Mimari ve Kullanılan Teknolojiler
Proje, modern yazılım mimarileri ve güncel teknolojiler kullanılarak geliştirilmiştir.
| Alan | Teknolojiler |
| :--- | :--- |
| **Mobil (Android)** | Kotlin, MVVM Architecture, Retrofit, Room Database, Coroutines, Jetpack Navigation |
| **Yapay Zeka (AI)** | Python, TensorFlow, Keras, TensorFlow Lite, Transfer Learning (MobileNetV2) |
| **Web (Frontend)** | HTML5, CSS3, JavaScript, TensorFlow.js |
---
## 📊 Yapay Zeka Model Performansı ve Veri Seti
Modelimiz, çeşitli açılardan çekilmiş binlerce hasarlı ve hasarsız araç görüntüsü ile eğitilmiştir. Eğitim sürecine ait performans grafikleri aşağıdadır.
### Performans Grafikleri
| Doğruluk (Accuracy) | Kayıp (Loss) | Karmaşıklık Matrisi (Confusion Matrix) |
| :---: | :---: | :---: |
| ![Doğruluk](Yapay_Zeka/grafik_dogruluk.png) | ![Kayıp](Yapay_Zeka/grafik_loss.png) | ![Confusion Matrix](Yapay_Zeka/grafik_confusion_matrix.png) |
### 📂 Model ve Veri Seti İndirme Linki
Eğitilmiş model dosyalarına (`.tflite`, `.h5`) ve kullanılan veri setine aşağıdaki Google Drive linkinden ulaşabilirsiniz:
👉 **https://drive.google.com/drive/folders/1c0G4guP3AFVjn_A9nhBHCEuXA-Jv3jVW?usp=sharing**  

## 🚀 Kurulum ve Çalıştırma
Projeyi yerel ortamınızda çalıştırmak için aşağıdaki adımları izleyebilirsiniz.
### 1. Android Uygulaması
1.  Bu depoyu klonlayın (`git clone ...`).
2.  `Android_Uygulama` klasörünü **Android Studio** ile açın.
3.  Gerekli SDK ve bağımlılıkların yüklenmesini bekleyin.
4.  Sanal cihaz (Emulator) veya fiziksel bir Android cihaz üzerinde `Run` tuşuna basarak başlatın.
### 2. Web Sitesi
1.  `Web_Sitesi` klasörüne gidin.
2.  `index.html` dosyasını favori tarayıcınızda açın.
3.  Daha iyi bir performans için `Live Server` eklentisi (VS Code) kullanmanız önerilir.
### 3. Yapay Zeka Eğitimi (Opsiyonel)
Modeli yeniden eğitmek isterseniz:
1.  `Yapay_Zeka` klasörüne gidin.
2.  Sanal ortam oluşturun ve aktif edin:
    ```bash
    python -m venv venv
    source venv/bin/activate  # Windows: venv\Scripts\activate
    ```
3.  Bağımlılıkları yükleyin:
    ```bash
    pip install -r requirements.txt
    ```
4.  Eğitimi başlatın:
    ```bash
    python egitim.py
    ```
---
## 📞 İletişim
Proje ile ilgili sorularınız veya geri bildirimleriniz için:
*   Email: mehmetberattrn@gmail.com
*   LinkedIn: https://www.linkedin.com/in/mehmet-berat-turan/
---
© 2025 **BiTurVer**. Tüm hakları saklıdır.
