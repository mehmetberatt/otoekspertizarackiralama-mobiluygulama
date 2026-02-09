🚗 BiTurVer - Yapay Zeka Destekli Akıllı Araç Kiralama ve Hasar Tespit Platformu

BiTurVer, geleneksel araç kiralama süreçlerindeki güven ve şeffafiyet sorunlarını çözmek amacıyla geliştirilmiş, yapay zeka tabanlı yenilikçi bir platformdur. Kullanıcılar mobil uygulama ve web arayüzü üzerinden araç kiralayabilir, kiralama öncesi ve sonrası araç durumunu AI destekli görüntü işleme teknolojisi ile anlık olarak analiz edebilirler.

🌟 Proje Özellikleri
🧠 Yapay Zeka Destekli Hasar Analizi

Otomatik Hasar Tespiti: Kullanıcıların yüklediği araç fotoğrafları, MobileNetV2 tabanlı derin öğrenme modeli ile analiz edilir.

Anlık Raporlama: Hasarın türü, konumu ve tahmini onarım maliyeti saniyeler içinde raporlanır.

Transfer Learning: Model, geniş veri seti üzerinde transfer öğrenme yöntemiyle eğitilmiştir.

📱 Mobil Uygulama (Android)

Modern ve sezgisel kullanıcı arayüzü

Yapay zeka destekli chatbot

OCR teknolojisi ile ehliyet doğrulama

Kiralama ve hasar geçmişi görüntüleme

🌐 Web Yönetim Paneli

Responsive tasarım

Filo yönetimi ve araç takibi

TensorFlow.js ile tarayıcı üzerinde AI model çalıştırma

📸 Ekran Görüntüleri ve Arayüz

Bu bölümde uygulamanın ve web arayüzünün ekran görüntüleri yer almaktadır.

📱 Mobil Uygulama
Ana Ekran	Hasar Tespiti	Profil Sayfası
<img src="https://github.com/user-attachments/assets/b912e43c-90c3-4fc1-81ed-1d236a1b809a" width="220">	<img src="https://github.com/user-attachments/assets/089c6187-c568-4371-97a4-a544b82de85f" width="220">	<img src="https://github.com/user-attachments/assets/59bf36e0-39bb-439f-a100-4ebc1bd05949" width="220">
💻 Web Arayüzü
<img src="https://github.com/user-attachments/assets/d9c942af-37b7-4477-9b11-63b5ce73f46d" width="100%">
🏗️ Teknik Mimari ve Kullanılan Teknolojiler
Alan	Teknolojiler
Mobil (Android)	Kotlin, MVVM Architecture, Retrofit, Room Database, Coroutines, Jetpack Navigation
Yapay Zeka (AI)	Python, TensorFlow, Keras, TensorFlow Lite, Transfer Learning (MobileNetV2)
Web (Frontend)	HTML5, CSS3, JavaScript, TensorFlow.js
📊 Yapay Zeka Model Performansı ve Veri Seti

Model, farklı açılardan çekilmiş binlerce hasarlı ve hasarsız araç görüntüsü ile eğitilmiştir. Eğitim sürecine ait performans grafikleri aşağıdadır.

Performans Grafikleri
Doğruluk (Accuracy)	Kayıp (Loss)	Confusion Matrix
<img src="https://github.com/user-attachments/assets/899c4a95-c0f9-4c60-826a-1d7d11de2f01" width="300">	<img src="https://github.com/user-attachments/assets/ce8f2dde-d527-4977-959e-fbc67210ec60" width="300">	<img src="https://github.com/user-attachments/assets/0d328ba1-e704-4794-8780-d1e7de6c8aa7" width="300">
📂 Model ve Veri Seti İndirme Linki

https://drive.google.com/drive/folders/1c0G4guP3AFVjn_A9nhBHCEuXA-Jv3jVW?usp=sharing

🚀 Kurulum ve Çalıştırma
Android Uygulaması

Bu depoyu klonlayın: git clone ...

Android_Uygulama klasörünü Android Studio ile açın.

Gerekli SDK ve bağımlılıkların yüklenmesini bekleyin.

Emulator veya fiziksel Android cihaz üzerinde Run ile başlatın.

Web Sitesi

Web_Sitesi klasörüne gidin.

index.html dosyasını tarayıcıda açın.

Daha iyi performans için VS Code Live Server kullanılabilir.

Yapay Zeka Eğitimi (Opsiyonel)

python -m venv venv
Windows: venv\Scripts\activate
Linux/Mac: source venv/bin/activate
pip install -r requirements.txt
python egitim.py

📞 İletişim

Email: mehmetberattrn@gmail.com

LinkedIn: https://www.linkedin.com/in/mehmet-berat-turan/

© 2025 BiTurVer. Tüm hakları saklıdır.
