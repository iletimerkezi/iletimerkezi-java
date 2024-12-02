# İleti Merkezi Java SDK

İleti Merkezi SMS API'sini Java uygulamalarınızda kullanmanızı sağlayan resmi SDK.

## Gereksinimler

- Java 11 veya üzeri
- Maven 3.6 veya üzeri

## Kurulum

### Maven ile Kurulum

```xml
<dependency>
<groupId>com.iletimerkezi</groupId>
<artifactId>iletimerkezi-java</artifactId>
<version>1.0.0</version>
</dependency>
```


### Gradle ile Kurulum

```groovy
implementation 'com.iletimerkezi:iletimerkezi-java:1.0.0'
```


## Kullanım

### Client Oluşturma

```java
import com.iletimerkezi.IletiMerkeziClient;
IletiMerkeziClient client = new IletiMerkeziClient(
"API_KEY", // API Anahtarı
"API_HASH", // API Hash
"SENDER" // Varsayılan Gönderici Adı
);
```


### SMS Gönderme

```java
// Tek alıcıya SMS gönderme
client.sms()
.send("+905321234567", "Merhaba Dünya!");
// Çoklu alıcıya SMS gönderme
List<String> recipients = Arrays.asList(
"+905321234567",
"+905371234567"
);
client.sms()
.send(recipients, "Merhaba Dünya!");
// İleri tarihli SMS gönderme
client.sms()
.schedule("2024-12-31 23:59:59")
.send("+905321234567", "Yeni Yıl Mesajı");
```

### Rapor Sorgulama

```java
// Özet rapor alma
client.summary()
.list("2024-01-01", "2024-01-31");
// Sonraki sayfa
client.summary()
.next();
```

### Bakiye Sorgulama

```java
AccountResponse response = client.account().balance();
System.out.println("Bakiye: " + response.getAmount());
System.out.println("SMS Kredisi: " + response.getCredits());
```

### Webhook İşleme

```java
String webhookData = "..."; // POST ile gelen veri
WebhookReport report = client.webhook().process(webhookData);
if (report.isDelivered()) {
System.out.println("SMS iletildi: " + report.getTo());
}
```

### Kara Liste İşlemleri

```java
// Numara ekleme
client.blacklist()
.add("+905321234567");
// Numara çıkarma
client.blacklist()
.remove("+905321234567");
// Liste sorgulama
client.blacklist()
.list(1);
```


## Hata Yönetimi

```java
try {
client.sms().send("+905321234567", "Test mesajı");
} catch (IOException e) {
// Ağ hatası
System.err.println("Ağ hatası: " + e.getMessage());
} catch (Exception e) {
// Diğer hatalar
System.err.println("Hata: " + e.getMessage());
}
```


## Desteklenen Java Versiyonları

- Java 11 (LTS)
- Java 17 (LTS)
- Java 21 (LTS)

## Geliştirme

### Projeyi Build Etme

```bash
mvn clean package
```


## Lisans

Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için [LICENSE](LICENSE.md) dosyasına bakınız.

## Destek

Herhangi bir sorunuz veya sorununuz olduğunda:

- [GitHub Issues](https://github.com/iletimerkezi/iletimerkezi-java/issues)
- E-posta: destek@iletimerkezi.com

## Güvenlik

Güvenlik açığı bildirimi için lütfen destek@iletimerkezi.com adresine e-posta gönderin.