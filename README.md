# Doc Scanner Pro

> A private, offline-first document scanner for Android.

Doc Scanner Pro turns paper documents into organized digital files without requiring an account, cloud storage or an Internet connection. Capture documents with the camera or import existing images, enhance them on-device, run local OCR, and export the result in the format you need.

All source code, document processing and storage are designed around a local-first workflow. Data leaves the device only when you explicitly use an Android share, export or print action.

## Highlights

- **Private by default** — no account, cloud backend, analytics SDK or advertising SDK
- **Offline OCR** — local Tesseract OCR with bundled English and Hindi language data
- **Document-aware capture** — automatic and manual capture with guidance for common document types
- **Book scanning** — detect an open spread, correct perspective and split it into two pages
- **Powerful processing** — crop, rotate, enhance, adjust contrast and brightness, convert to grayscale or black and white, and reduce shadows
- **Flexible exports** — searchable PDFs, image-based PDFs, text, Markdown, DOCX, CSV and JSON
- **Local organization** — folders, tags, favorites, sorting, filtering and full-text search
- **Privacy-focused storage** — local metadata in Room and a private vault backed by Android Keystore

## Features

### Capture and import

- Single-page and multi-page document sessions
- Automatic and manual document capture
- Import through Android Photo Picker and document providers
- Workflows for documents, books, receipts, ID cards and business cards
- QR code and barcode recognition
- Flash control and capture guidance

### Processing and OCR

- Four-corner crop and perspective correction
- Rotation, grayscale and black-and-white conversion
- Contrast, brightness and document enhancement
- Shadow reduction
- Non-destructive editing where supported
- Local OCR powered by Tesseract 5 through Tesseract4Android
- Bundled English (`eng`) and Hindi (`hin`) trained data

### PDF and export

- Single-page and multi-page PDF creation
- Searchable PDF output
- PDF page reordering, merging, splitting and page extraction
- OCR text export
- Text and Markdown export
- DOCX generation
- CSV and JSON export paths

### Organization

- Recent documents
- Folders, tags and favorites
- List and grid views
- Sorting and filtering
- Local full-text search
- Contextual document actions
- Private local vault

## Privacy

Doc Scanner Pro does not require an account or Internet access. The application does not include a cloud service, document upload service, analytics SDK or advertising SDK.

Document images, OCR results, notes and metadata are processed and stored locally. The app requests:

- **Camera** — to capture documents
- **Notifications** — for Android notification-based scanner actions where supported

Sharing, exporting or printing through Android may transfer a document to another application or destination. Those actions are always initiated by you.

## Screenshots

Screenshots will be added as the public release builds are published.

## Installation

### GitHub Releases

Install the APK from the project's **Releases** page when a release is available. For the widest device compatibility, use the universal APK. ABI-specific APKs are also published when available.

### Build from source

#### Requirements

- Android Studio with Android SDK support
- JDK 17
- Android SDK 36
- Android NDK `27.2.12479018` for the Tesseract4Android source build
- A device or emulator running Android 7.0 (API 24) or newer

The project includes the Gradle wrapper, so Gradle does not need to be installed separately.

#### Build a debug APK

```bash
cd DocScannerPro-main
./gradlew assembleDebug
```

On Windows:

```powershell
.\gradlew.bat assembleDebug
```

The debug APK is generated under `app/build/outputs/apk/`.

#### Build a release APK

```bash
./gradlew assembleRelease
```

The release build is configured for local development and testing. Configure production signing outside the repository before distributing a release build.

#### Tesseract4Android source dependency

Normal Android Studio builds resolve Tesseract4Android from the configured dependency repositories. For a fully source-built setup, keep the pinned Tesseract4Android project at `third_party/Tesseract4Android` and publish it locally first:

```bash
cd third_party/Tesseract4Android
./gradlew :tesseract4android:publishToMavenLocal
cd ../..
./gradlew assembleDebug
```

The official F-Droid build uses the source-library configuration in [`fdroid/com.rebelroot.docscannerpro.yml`](fdroid/com.rebelroot.docscannerpro.yml).

## Development

Run unit tests:

```bash
./gradlew test
```

Run Android instrumentation checks on a connected device or emulator:

```bash
./gradlew connectedCheck
```

Run lint:

```bash
./gradlew lint
```

Before submitting changes that affect packaging, permissions, native libraries or document processing, also build and test the release variant on a physical device.

## Architecture

```text
app
├── core
│   ├── cv
│   ├── database
│   ├── export
│   ├── model
│   ├── ocr
│   └── security
└── ui
    ├── navigation
    ├── screens
    ├── theme
    └── viewmodel
```

The primary processing path is:

```text
CameraX
  -> frame analysis
  -> document geometry detection
  -> perspective correction
  -> image enhancement
  -> OCR
  -> local document storage
  -> PDF / document export
```

The OCR integration is isolated behind `OcrEngine`, allowing additional language packs and processing strategies without coupling the document system to a single OCR implementation.

## Technology

- Kotlin
- Jetpack Compose and Material 3
- CameraX
- OpenCV
- Tesseract 5 and Tesseract4Android
- ZXing Core
- Room
- DataStore
- Android Keystore
- Kotlin Coroutines
- Apache PDFBox for Android

Third-party license information is available in [`docs/THIRD_PARTY_LICENSES.md`](docs/THIRD_PARTY_LICENSES.md).

## F-Droid

The project is prepared for an upstream F-Droid submission. The F-Droid metadata builds Tesseract4Android from source through the existing F-Droid source-library mechanism; no prebuilt Tesseract AAR is required in the application source tree.

Official inclusion is subject to F-Droid's review, policy checks, isolated build and reproducibility verification. Packaging files are maintained in [`fdroid/`](fdroid/).

Read the current status in [`docs/F-DROID_STATUS.md`](docs/F-DROID_STATUS.md).

## Contributing

Contributions are welcome. Please read [`CONTRIBUTING.md`](CONTRIBUTING.md) before opening an issue or pull request.

When proposing a change:

1. Explain the user-facing outcome and implementation scope.
2. Keep presentation, processing, persistence and export responsibilities separated.
3. Include or update regression tests for behavior changes.
4. Review dependency licenses when adding a library.
5. Avoid introducing network dependencies for functionality that can run locally.

## Support

Doc Scanner Pro is free and open-source software. If it is useful to you, you can support continued development at [Ko-fi](https://ko-fi.com/rebelroot). Support is optional, and the app remains fully functional without an account, subscription or donation.

## Security

Please do not publish sensitive vulnerability details in a public issue. See [`SECURITY.md`](SECURITY.md) for the reporting process and security principles.

## License

Doc Scanner Pro is released under the [MIT License](LICENSE). Third-party components retain their respective licenses.
