# PRODUCT.md

What Walenje is for, who uses it and how it grows. `ROADMAP.md` has the phase 1 build plan. `DESIGN.md` has the
visual direction.

## Offering

**Capture every business receipt. Get clean records for KRA and your accountant.**

Walenje turns receipts into bookkeeping records. The user captures a receipt, the app extracts the data, the user
checks it, and the app files it. At the end of a month or year, the user exports a report.

"Walenje" is a working name. See [Open decisions](#open-decisions).

## User

The first user is a **freelancer or sole trader in Kenya**.

| Trait | What it means for the product |
|---|---|
| Pays by M-Pesa, cash and card | Receipts come from many sources. M-Pesa is the most common. |
| Gets KRA eTIMS receipts, paper slips and email invoices | Receipt formats vary a lot. Some have a QR code with structured data. |
| Files one income tax return a year (tax year = calendar year, due 30 June) | Reports cover a month, a quarter or a tax year. |
| May be VAT-registered | VAT amounts must be captured where the receipt shows them. |
| Mixes business and personal spending | Each receipt is marked business or personal. |
| Works for several clients | Receipts can be tagged with a client or project. |
| Has limited time for admin | Capture must take seconds. Review must show only what needs attention. |

Later users (not now): small businesses with several people, and users outside East Africa.

## Product principles

1. **Local first.** Data stays on the device. The app needs no account and no server.
2. **Never trust extraction blindly.** Every receipt passes a review step. The app marks low-confidence fields.
3. **Keep the evidence.** Each record keeps its original image or PDF.
4. **One pipeline.** Every capture source feeds the same extract, review, tag and store steps.
5. **Records, not tax advice.** Categories help bookkeeping. The app does not tell the user what is deductible.
6. **Free for now.** The app has no billing yet. Features that could become paid later are marked in this file.

## Core loop

```
Capture → Extract → Review → Tag → Store → Export
```

Each phase adds a capture source. The rest of the loop stays the same.

| Phase | Capture | Extract | Review, Tag, Store, Export |
|---|---|---|---|
| 1 | Scan, image, PDF | eTIMS QR code, on-device OCR, rule-based parser | Built in phase 1 |
| 2 | M-Pesa statement PDF | Statement parser, match to receipts | Reused |
| 3 | Email (shared into the app) | Email text and attachment parser | Reused |
| 4 | Dictation + map pin | Speech to text, then parser (optional on-device LLM) | Reused |

## Receipt data model

| Field | Notes |
|---|---|
| Merchant name | |
| Merchant KRA PIN | Format `A123456789Z` (letter, nine digits, letter) |
| Date and time | |
| Currency | Default KES |
| Subtotal, VAT, total | VAT at 16 % where shown. Total is required. |
| Payment method | Cash, M-Pesa, card, bank |
| M-Pesa reference | For example `SLK4H7XYZ2`. Links a receipt to a statement line in phase 2. |
| eTIMS invoice number | From the QR code or the printed receipt |
| Line items | Description, quantity, unit price, amount. Best effort. |
| Category | See [Categories](#categories) |
| Business or personal | Default: business |
| Client or project | Optional free tag |
| Notes | |
| Source | Scan, image, PDF, M-Pesa, email, voice |
| Attachments | Original images or PDF |
| Status | Needs review, confirmed |
| Field confidence | Per extracted field. Drives the review marks. |
| Location | Phase 4 |

## Categories

Default set for freelancers. The user can add, rename and hide categories.

| Category | Examples |
|---|---|
| Internet and airtime | Home fibre, mobile data bundles |
| Equipment and software | Laptop, phone, subscriptions |
| Transport and fuel | Fuel, ride-hailing, matatu, parking |
| Meals and client entertainment | Client lunch, team meal |
| Office and coworking | Desk rent, office supplies |
| Utilities | Electricity tokens, water |
| Professional fees | Accountant, lawyer, registration fees |
| Bank and M-Pesa charges | Transaction fees |
| Training | Courses, books, conferences |
| Marketing | Ads, printing, domain names |
| Personal | Not business. Kept for completeness, left out of business totals. |

## Journeys

### J1 Onboarding

| Step | Screen | System action | Edge cases |
|---|---|---|---|
| 1 | Welcome | Shows the value proposition | |
| 2 | Secure your receipts | Explains the PIN and local storage | |
| 3 | Set PIN | Saves the PIN | PIN mismatch on confirm |
| 4 | Biometrics | Turns on biometric unlock | Device has no biometrics: skip |
| 5 | Profile (optional) | Saves name, KRA PIN, VAT-registered yes/no | User skips: ask again at first export |

### J2 Capture

| Step | Screen | System action | Edge cases |
|---|---|---|---|
| 1 | Home | User taps Capture | |
| 2 | Capture sheet | Offers Scan, Image, PDF | |
| 3a | System document scanner | Detects edges, crops, lets the user add pages | Camera permission denied: offer Image and PDF |
| 3b | Image or file picker | Imports the file | Unsupported file type |
| 4 | Processing | Starts J3 | |

### J3 Extract

| Step | Screen | System action | Edge cases |
|---|---|---|---|
| 1 | Processing | Looks for an eTIMS QR code | No QR code: go to step 2 |
| 2 | Processing | Runs on-device OCR on each page | Blurry or dark image: warn and offer a rescan |
| 3 | Processing | Parses fields and gives each a confidence | No total found: mark as needs review |
| 4 | Processing | Suggests a category from the merchant and past choices | |
| 5 | Processing | Flags a possible duplicate (same merchant, date and total) | |

### J4 Review

| Step | Screen | System action | Edge cases |
|---|---|---|---|
| 1 | Review | Shows the image above and the fields below. Marks low-confidence fields. | |
| 2 | Review | User fixes fields, sets category, business/personal and client | |
| 3 | Review | User saves | Required field (date, total) empty: block save |
| 4 | Review | User saves for later | Status stays "needs review" |

### J5 Browse

| Step | Screen | System action | Edge cases |
|---|---|---|---|
| 1 | Home | Hero: this month's business spend. Tile: receipts that need review. | No receipts: empty state with Capture |
| 2 | Receipts | List grouped by month | |
| 3 | Receipts | Search by merchant, note or amount. Filter by category, period, business/personal, status. | No results |
| 4 | Receipt detail | Shows fields and attachments. User edits or deletes. | Delete asks for confirmation |

### J6 Export

| Step | Screen | System action | Edge cases |
|---|---|---|---|
| 1 | Reports | User picks a period (month, quarter, tax year, custom) | No receipts in period |
| 2 | Reports | Shows totals by category, business only | Receipts still need review: warn, allow export |
| 3 | Reports | Makes a CSV and a PDF (summary, receipt list, images) | Large image set: show progress |
| 4 | System share sheet | User sends or saves the files | |

Possible paid feature later: PDF report with images.

### J7 Backup and restore

| Step | Screen | System action | Edge cases |
|---|---|---|---|
| 1 | Settings | User taps Back up | |
| 2 | System share sheet | App makes one archive (data and attachments). User saves it to Files, Google Drive or iCloud. | Low storage |
| 3 | Settings | User taps Restore and picks an archive | Archive from a newer app version: refuse with a message |
| 4 | Settings | App replaces local data after confirmation | |

The app reminds the user to back up, because local-only data is lost with the phone. Possible paid feature later:
automatic backup.

### Later journeys

| Journey | Phase | Summary |
|---|---|---|
| J8 M-Pesa import | 2 | User imports an M-Pesa statement PDF. The app reads the lines, matches them to receipts by reference, amount and date, and turns the rest into draft records. Fees go to "Bank and M-Pesa charges". |
| J9 Email share | 3 | User shares an email or attachment into the app from the mail app. The app parses it like a PDF or image. |
| J10 Dictation | 4 | User says what they bought, where and for how much. The app turns it into a record with line items and a map pin. For cash purchases with no receipt. |

## Roadmap

| Phase | Goal | Features | Exit criteria |
|---|---|---|---|
| 1 | One complete loop for paper and PDF receipts | J1–J7 | A user can capture, review, browse, export and back up receipts on Android and iOS, with no network |
| 2 | Cover M-Pesa payments | J8 | A real statement imports with correct lines and matches |
| 3 | Cover email receipts | J9 | Common invoice emails (airlines, utilities, software) parse |
| 4 | Cover cash purchases with no receipt | J10 | A spoken purchase becomes a correct record in one try for simple cases |
| Hybrid | Better extraction and sync, when the app pays for itself | Cloud LLM extraction, a receipt forwarding address, inbox scanning, sync across devices | Decided later |

To verify before each phase, not facts yet:

- What the eTIMS QR code holds, and whether reading it needs a network call (phase 1).
- How M-Pesa statement PDFs are protected and laid out (phase 2).
- Google Play's restricted SMS permission policy, if SMS reading is ever considered (after phase 2).
- Gmail API restricted scopes need a yearly third-party security assessment (CASA) for public apps (hybrid).
- Map and place search pricing: MapKit on iOS, Google Maps and Places on Android (phase 4).

## Open decisions

| Decision | Options | When |
|---|---|---|
| Name | Risiti (Swahili: receipt), Stakabadhi (formal Swahili: receipt), Kept, Tally, keep Walenje. Check app stores and trademarks first. | Before the first public release |
| Revenue | Freemium (receipt limit, reports, automatic backup), one-time purchase, subscription. Payment by M-Pesa and app stores. | After phase 1 has users |
| Hybrid | When cloud costs are covered by revenue | After revenue |
| Income tracking | Freelancers also need income records (M-Pesa receipts in phase 2 show income) | After phase 2 |
