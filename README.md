# Multithread Flight Reservation System
Bu proje, çoklu iş parçacığı ile çalışan bir havayolu rezervasyon sistemi örneğidir. Proje, veri bütünlüğünü sağlamak için senkronizasyon mekanizmaları kullanılarak geliştirilmiştir. Aynı veri tabanına erişen birden fazla okuyucu (reader) ve yazıcı (writer) thread'lerini yönetir.

## İçindekiler
- [Kurulum](#kurulum)
- [Proje Mimarisi](#proje-mimarisi)
- [Sınıflar ve Açıklamaları](#sınıflar-ve-açıklamaları)
- [Çalıştırma](#çalıştırma)

## Kurulum

1. Bu projeyi klonlayın:
    ```sh
    git clone https://github.com/kullanici/Multithread-Flight-Reservation-System.git
    cd Multithread-Flight-Reservation-System
    ```

2. Gerekli bağımlılıkları yükleyin:
    - Java JDK (11 veya üstü)
    - Bir IDE (IntelliJ IDEA, Eclipse vb.)

## Proje Mimarisi

Klasör/Dosya Açıklama:
- **entities/**: Uygulamanın temel varlık sınıflarını içerir.
  - **Flight.java**: Uçuş bilgilerini tutan sınıf.
  - **Seat.java**: Koltuk bilgilerini tutan sınıf.
- **database/**: Rezervasyon veritabanını yöneten sınıfı içerir.
  - **ReservationDatabase.java**: Rezervasyon veritabanını yöneten sınıf.
- **threads/**: Reader ve Writer işlemlerini gerçekleştiren thread sınıflarını içerir.
  - **ReaderThread.java**: Rezervasyon sorgulama işlemi yapan reader thread sınıfı.
  - **WriterThread.java**: Koltuk rezervasyonu yapma ve iptal etme işlemleri yapan writer thread sınıfı.
- **gui/**: Kullanıcı arayüzünü sağlayan sınıfları içerir.
  - **GUI.java**: Kullanıcı arayüzünün ana sınıfı.
  - **ReservationPanel.java**: Rezervasyon panelini içeren sınıf.
- **Main.java**: Uygulamanın ana sınıfı. Programı başlatan sınıftır.

## Sınıflar ve Açıklamaları

### Seat.java
Seat sınıfı, uçuşta bulunan koltukların bilgilerini yönetmek için oluşturulmuştur. Bu sınıf, her bir koltuk için benzersiz bir kimlik numarası (seatId), koltuğun rezerve edilip edilmediğini belirten bir durum (reserved) ve koltuğu rezerve eden müşterinin kimlik numarasını (customerId) içerir.

### Flight.java
Flight sınıfı, bir uçuşun bilgilerini ve ilgili koltuk rezervasyonlarını yönetmek için oluşturulmuştur. Bu sınıf, uçuş ID'si, uçuş başlığı, kalkış zamanı ve koltuk bilgilerini içeren bir yapı sağlar.

### ReservationDatabase.java
ReservationDatabase sınıfı, tüm uçuşları ve koltuklarını yöneten merkezi veritabanı olarak işlev görür. Bu sınıf, uçuşların ve koltukların durumlarını saklar ve bu verilere erişim sağlar.

### ReaderThread.java
ReaderThread sınıfı, çoklu iş parçacığı ile çalışan uçak rezervasyon sisteminde koltuk durumlarını sorgulamak için kullanılan iş parçacığını temsil eder. Bu sınıf, belirli bir uçuşun koltuk durumlarını görüntüler.

### WriterThread.java
WriterThread sınıfı, çoklu iş parçacığı ile çalışan uçak rezervasyon sisteminde koltuk rezervasyonu yapmak için kullanılan iş parçacığını temsil eder. Bu sınıf, belirli bir uçuşa ve koltuklara rezervasyon yapma işlemlerini gerçekleştirir.

### ReservationPanel.java ve GUI.java
ReservationPanel sınıfı, uçak rezervasyon sisteminin kullanıcı arayüzünde kullanılan bir paneldir. ReservationPanel, veritabanı ve kilit nesnelerini alır. Kullanıcı ve uçuş seçimlerini yapacak bileşenleri başlatır ve düzenler. GUI sınıfı ise proje çalıştırıldığında iki paneli yan yana görmemizi sağlar.

## Çalıştırma

1. IDE'nizde projeyi açın ve ana sınıf olan `Main.java` dosyasını çalıştırın.
2. Uygulama başlatıldığında, kullanıcı arayüzü açılacaktır. Bu arayüz üzerinden uçuşlar ve koltuklarla ilgili işlemleri gerçekleştirebilirsiniz.

