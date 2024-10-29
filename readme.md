# Relasi Antar Entity

Aplikasi ini memiliki beberapa entity dengan relasi yang saling terkait. Berikut adalah contoh relasi antar entity yang digunakan dalam sistem ini:

## 1. User - Post (One-to-Many)
   - **User** dapat memiliki banyak **Post**.
   - Setiap **Post** hanya dimiliki oleh satu **User**.

## 2. Post - Comment (One-to-Many)
   - **Post** dapat memiliki banyak **Comment**.
   - Setiap **Comment** hanya terkait dengan satu **Post**.

## 3. Post - Tag & Category (Many-to-Many)
   - **Post** dapat memiliki banyak **Tag** dan **Category**.
   - **Tag** atau **Category** yang sama dapat terhubung ke banyak **Post**.

## 4. Post - Like (One-to-Many)
   - **Post** dapat memiliki banyak **Like** dari berbagai pengguna.
   - Setiap **Like** terkait hanya dengan satu **Post**.

## 5. Comment - Like (One-to-Many)
   - **Comment** juga dapat memiliki banyak **Like** dari berbagai pengguna.
   - Setiap **Like** pada **Comment** hanya terkait dengan satu **Comment**.

---

Relasi ini memungkinkan fleksibilitas dalam mengelola **Post**, **Comment**, **Tag**, **Category**, dan **Like** dalam aplikasi ini.

# Langkah-langkah run
```
make run
```

# Langkah-langkah build 
Cek apakah maven sudah terinstal
```
mvn -v

```

Build Proyek Maven
```
mvn clean install

```
Perintah ini akan melakukan dua hal utama:
clean: Menghapus build sebelumnya.
install: Membuat JAR atau WAR file (sesuai konfigurasi) di direktori target dan menginstalnya ke local repository Maven. Menghasilkan File JAR atau WAR

Menjalakan file JAR
```
java -jar target/nama-aplikasi-versi.jar

```

