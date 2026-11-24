# Spring AI Chapter 1

REST API sederhana untuk mencoba Spring AI dan mengirim pertanyaan ke provider
OpenAI-compatible Nararouter menggunakan model `gpt-5.6-luna`.

## Teknologi

- Java 23
- Spring Boot 4.1.3
- Spring AI 2.0.1
- Gradle Wrapper
- WireMock untuk automated test tanpa API key dan tanpa akses internet

## Struktur utama

```text
src/main/java/com/example/testSpringAi/
├── controller/AskController.java
├── model/Question.java
├── model/Answer.java
└── service/
    ├── BoardGameService.java
    └── SpringAiBoardGameService.java
```

`AskController` menerima request `POST /ask`. `SpringAiBoardGameService`
menggunakan Spring AI `ChatClient` untuk mengirim prompt, kemudian hasilnya
dikembalikan sebagai `Answer`.

## Persiapan

Pastikan Java 23 tersedia:

```powershell
java -version
```

API key tidak disimpan di source code. Set environment variable hanya pada
terminal lokal:

```powershell
$env:NARAROUTER_API_KEY="API_KEY_ANDA"
$env:SPRING_AI_OPENAI_BASE_URL="https://router.bynara.id/v1"
$env:SPRING_AI_OPENAI_CHAT_MODEL="gpt-5.6-luna"
```

Jangan commit atau membagikan nilai API key.

## Menjalankan aplikasi

```powershell
.\gradlew.bat bootRun
```

Aplikasi berjalan pada `http://localhost:8080`.

Kirim pertanyaan:

```powershell
curl.exe -X POST "http://localhost:8080/ask" `
  -H "Content-Type: application/json" `
  -d '{"question":"What is the goal of a board game?"}'
```

Response:

```json
{
  "answer": "..."
}
```

## Menjalankan test

Automated test menggunakan WireMock pada endpoint lokal. Test tidak memanggil
Nararouter dan tidak membutuhkan API key:

```powershell
.\gradlew.bat test
```

Build lengkap:

```powershell
.\gradlew.bat build
```

## Dokumentasi Chapter 1

Dokumentasi teori, konfigurasi, UML, cara menjalankan aplikasi, dan cara test
tersedia pada [CHAPTER1.md](CHAPTER1.md).
