# ROADMAP.md

This file says how to build Walenje, phase by phase. `PRODUCT.md` says what each phase delivers and why.

## Phase 1: one complete loop

Scope: journeys J1–J7 in `PRODUCT.md`. On-device only, no network. Release targets: Android and iOS. The JVM
target stays a dev and test target.

### Rules for every step

- Follow `AGENTS.md`: an MVI contract per screen, ViewModels on `BaseViewModel`, Koin `module { viewModelOf(...) }`,
  routes in `app/common/.../navigation/Navigation.kt`.
- Give each new module one convention plugin role (`walenje.library` or `walenje.feature`).
- Put versions only in `gradle/libs.versions.toml`.
- Add tests for business logic in the same change.
- Keep platform APIs in `androidMain`, `iosMain` and `jvmMain` behind `expect`/`actual`. The JVM actual can be a
  stub.

### Build order

| Step | Module or area | Work | Done when |
|---|---|---|---|
| 0 | Spikes (throwaway branches) | (a) Room KMP with KSP on AGP 9 built-in Kotlin, for Android, iOS and JVM. Fallback: SQLDelight. (b) Read eTIMS QR codes from real receipts and record what they hold. (c) Run on-device OCR on 10–20 real Kenyan receipts and record the quality. | [Spike results](#spike-results) has a short result for each spike |
| 1 | `core/receipts` (new) | Pure Kotlin domain: `Receipt`, `LineItem`, `Category`, `ReceiptRepository` interface, rule-based `ReceiptParser`. | Parser tests pass on anonymised OCR text fixtures |
| 2 | `core/database` (new) | Room KMP (bundled SQLite) implementation of `ReceiptRepository`. Store attachments as files in app storage with kotlinx-io. | Repository tests pass on JVM and Android host |
| 3 | `core/scanner` (new) | `expect`/`actual` document scanner, text recognizer and QR reader. Image and PDF picking with FileKit. | Each platform returns text and QR content for a sample receipt |
| 4 | `feature/capture` (new) | Capture sheet, processing state, review screen. | J2–J4 work end to end on a device |
| 5 | `feature/receipts` (new) and `feature/home` | Receipts list, search, filters, detail, edit, delete. Home gets the business-spend hero and a "needs review" tile. | J5 works |
| 6 | `core/export` (new) and `feature/reports` (replaces `feature/analytics`) | CSV in common code. PDF report per platform. Share sheet `expect`/`actual`. | J6 produces a CSV and a PDF that open in a spreadsheet and a PDF viewer |
| 7 | `feature/settings` (replaces `feature/profile`) | Backup archive (data and attachments), restore with a version check, backup reminder. | J7 restores a backup on a fresh install |
| 8 | `feature/onboarding` | Replace "secure your wallet" wording with the receipts story. Add the optional profile step. | J1 matches `PRODUCT.md` |

Steps 1 and 3 do not depend on each other. Steps 4–8 need steps 1 and 2. Step 4 also needs step 3.

### Parser rules (step 1)

| Field | Rule |
|---|---|
| Total | The amount on the last line with "total", "amount due" or "to pay". If that line has two amounts, take the larger one. |
| Amounts | `KES`, `Ksh`, `KSh` or no prefix; thousands separator `,`; two decimals |
| Date | `dd/MM/yyyy`, `dd-MM-yyyy`, `yyyy-MM-dd`, `dd MMM yyyy` |
| KRA PIN | `[AP]\d{9}[A-Z]` |
| VAT | Line with "VAT" or "16%". Check that subtotal + VAT = total. A mismatch lowers confidence. |
| eTIMS | Lines such as "CU Invoice No", "SCU ID", "Receipt Number". Confirm the exact labels in spike 0(b). |
| M-Pesa reference | 10 upper-case letters and digits next to "M-Pesa", "Ref" or "Transaction" |
| Merchant | First non-empty lines before the address or PIN line. Low confidence by default. |

### Platform APIs (step 3)

| Need | Android | iOS | JVM |
|---|---|---|---|
| Document scanner | ML Kit Document Scanner | VisionKit `VNDocumentCameraViewController` | Not available: picker only |
| Text recognition | ML Kit Text Recognition (on-device) | Vision `VNRecognizeTextRequest` | Fixture text |
| QR code | ML Kit Barcode Scanning | Vision `VNDetectBarcodesRequest` | Fixture value |
| PDF to image | `PdfRenderer` | PDFKit | Fixture image |
| Image and PDF picker | FileKit | FileKit | FileKit |
| PDF report | `PdfDocument` | `UIGraphicsPDFRenderer` | Stub |
| Share sheet | `Intent.ACTION_SEND` | `UIActivityViewController` | Stub |

ML Kit Document Scanner needs Google Play services. On devices without them, offer the picker only.

## Later phases (outline)

| Phase | Work |
|---|---|
| 2: M-Pesa | Import a statement PDF. Unlock it on the device with the user's password. Extract the text and parse the lines. Match each line to a receipt by reference, amount and date. Make draft receipts for lines with no match. Older Android API levels have no built-in PDF text extraction: pick a library in a spike. |
| 3: Email | Accept shared emails, `.eml` files and attachments through the share sheet (Android intent filter, iOS share extension). Reuse the PDF and image pipeline. |
| 4: Dictation | Platform speech recognition. Parse the text with the rule parser, or with an on-device LLM where the device has one (Apple Foundation Models, Gemini Nano). Map pin with MapKit on iOS; check Google Maps and Places pricing on Android. |
| Hybrid | A backend for a receipt forwarding address, cloud LLM extraction and sync. Gmail inbox scanning needs Google's restricted-scope security assessment. |

## Spike results

_None yet. Add each result from step 0 here._
