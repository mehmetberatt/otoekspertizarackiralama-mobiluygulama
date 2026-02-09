import os
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
import tensorflow as tf
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.applications import MobileNetV2
from tensorflow.keras.layers import Dense, GlobalAveragePooling2D
from tensorflow.keras.models import Model
from sklearn.metrics import confusion_matrix


DATA_PATH = r'C:\Users\mehme\OneDrive\Desktop\BiTurVer_Proje\Yapay_Zeka\data'
IMG_SIZE = (224, 224)
BATCH_SIZE = 32
EPOCHS = 5

print("1. Veriler Hazırlanıyor...")
train_datagen = ImageDataGenerator(
    rescale=1./255,
    validation_split=0.2,
    rotation_range=20,      
    zoom_range=0.2,         
    horizontal_flip=True    
)

# Eğitim Seti (Training)
train_generator = train_datagen.flow_from_directory(
    DATA_PATH,
    target_size=IMG_SIZE,
    batch_size=BATCH_SIZE,
    class_mode='categorical',
    subset='training'
)

# Doğrulama Seti (Validation) 
val_generator = train_datagen.flow_from_directory(
    DATA_PATH,
    target_size=IMG_SIZE,
    batch_size=BATCH_SIZE,
    class_mode='categorical',
    subset='validation'
)


labels = (train_generator.class_indices)
print(f"Tespit edilecek sınıflar: {labels}")
with open("labels.txt", "w") as f:
    f.write(str(labels))

print("2. Model Kuruluyor (MobileNetV2 - Hızlı ve Hafif)...")


base_model = MobileNetV2(weights='imagenet', include_top=False, input_shape=(224, 224, 3))
base_model.trainable = False 

x = base_model.output
x = GlobalAveragePooling2D()(x)
x = Dense(128, activation='relu')(x)
predictions = Dense(len(labels), activation='softmax')(x)

model = Model(inputs=base_model.input, outputs=predictions)
model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

print("3. Eğitim Başlıyor...")


history = model.fit(
    train_generator,
    steps_per_epoch=train_generator.samples // BATCH_SIZE,
    validation_data=val_generator,
    validation_steps=val_generator.samples // BATCH_SIZE,
    epochs=EPOCHS
)

print("4. Rapor İçin Grafikler Çiziliyor...")

# Doğruluk Grafiği
plt.figure(figsize=(10, 5))
plt.plot(history.history['accuracy'], label='Eğitim Başarısı')
plt.plot(history.history['val_accuracy'], label='Test Başarısı')
plt.title('Model Doğruluğu (Accuracy)')
plt.ylabel('Doğruluk')
plt.xlabel('Epoch')
plt.legend(loc='lower right')
plt.grid(True)
plt.savefig('grafik_dogruluk.png')
plt.close()

# Kayıp Grafiği
plt.figure(figsize=(10, 5))
plt.plot(history.history['loss'], label='Eğitim Kaybı')
plt.plot(history.history['val_loss'], label='Test Kaybı')
plt.title('Model Kaybı (Loss)')
plt.ylabel('Loss')
plt.xlabel('Epoch')
plt.legend(loc='upper right')
plt.grid(True)
plt.savefig('grafik_loss.png')
plt.close()

print("Grafikler kaydedildi.")

print("5. Model Android İçin Dönüştürülüyor (.tflite)...")
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()

with open('model.tflite', 'wb') as f:
    f.write(tflite_model)

print("Ekstra: Confusion Matrix (Doğruluk Tablosu) Çiziliyor...")


test_generator = train_datagen.flow_from_directory(
    DATA_PATH,
    target_size=IMG_SIZE,
    batch_size=BATCH_SIZE,
    class_mode='categorical',
    subset='validation',
    shuffle=False 
)

# 2. Tahminler
predictions = model.predict(test_generator)
predicted_classes = np.argmax(predictions, axis=1) 
true_classes = test_generator.classes              
class_labels = list(test_generator.class_indices.keys()) 

# 3. Matrisi Hesapla
cm = confusion_matrix(true_classes, predicted_classes)

# 4. Çiz ve Kaydet
plt.figure(figsize=(8, 6))
sns.heatmap(cm, annot=True, fmt='d', cmap='Blues',
            xticklabels=class_labels,
            yticklabels=class_labels)
plt.title('Confusion Matrix (Doğruluk Tablosu)')
plt.ylabel('Gerçek Durum')
plt.xlabel('Yapay Zekanın Tahmini')
plt.savefig('grafik_confusion_matrix.png')
plt.close()

print("Confusion Matrix kaydedildi.")
print("✅ İŞLEM TAMAM! Tüm dosyalar oluşturuldu.")