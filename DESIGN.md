# DESIGN.md

This file sets how Walenje looks. Read it before you design or build a screen or a `core/designsystem` component.
`AGENTS.md` > Architecture rules decides where UI code lives. `PRODUCT.md` decides what each screen is for.

The design specimen shows these rules on four phase 1 screens:
<https://claude.ai/artifact/5cMjtzhzWKswLpAwqfPCA9> (private to the owner). Where the specimen and this file
disagree, this file wins.

## Direction

The base is **International Typographic Style** (ITS, Swiss style). Some accents come from Slava Kornilov's work
for Geex Arts (<https://dribbble.com/slavakornilov>). Take the principles, not his layouts.

| From ITS | From Kornilov | Not used |
|---|---|---|
| Modular grid, flush-left type, one sans family, flat colour, rules between content, asymmetric layouts | Bento tiles, one hero number per screen, two-tone headings, controls in the thumb zone, round ✕ ✓ ↗ buttons | Gradient cards, hardware-like "objects", a second numeric face, category colours, large radii |

## Principles

1. **Grid first.** Every element snaps to the 4-column grid and the 4dp baseline. Text is flush left, ragged right.
2. **Hierarchy comes from type.** One family, few sizes, big jumps. Headings are two-tone: a title in ink and a
   muted second line at the same size ("Business spend" / "November 2026").
3. **Neutral first, one accent.** Neutrals carry the layout. Aqua marks the one primary action on a screen. A
   screen must still read in greyscale.
4. **One hero number per screen.** Show it in the Display style. Back it with a plain graphic (a tick ruler, a
   bar), not a paragraph.
5. **One idea per tile.** A tile holds one idea and at most one hero element.
6. **Rules, not shadows.** Thin lines separate rows and groups. Do not use shadows or borders to make elements
   float.
7. **Controls sit in the thumb zone.** Primary actions live at the bottom of the screen.
8. **Never colour alone.** Money direction always has a `+` or `−` sign. A status always has a word.
9. **Real formats.** Examples and empty states use Kenyan formats: KES, `dd MMM yyyy`, KRA PIN, eTIMS numbers.

## Foundations

Tokens live in `core/designsystem/theme/`. Feature code never hard-codes them (see `AGENTS.md` > UI).

### Grid and spacing

| Token | Value |
|---|---|
| Columns | 4 |
| Side margin | 16dp |
| Gutter | 8dp |
| Baseline | 4dp: every line height, gap and component size is a multiple of 4 |
| Spacing scale | 4, 8, 12, 16, 24, 32, 48 |
| Tile padding | 12dp |
| Gap between blocks | 16dp |
| Touch target | 48dp minimum |

Tiles span 2 or 4 columns. Wider screens (tablets) keep the 16dp margin and add columns in steps of 4.

### Typography

One family: **Inter Tight** (OFL). Hero numbers use it too. Amounts always use tabular figures
(`fontFeatureSettings = "tnum"`) so columns line up.

| Style | Size / line (sp) | Weight | Tracking | Use |
|---|---|---|---|---|
| Display | 64 / 60 | 600 | −4% | The hero number. One per screen. |
| Headline | 28 / 32 | 600 | −2% | Screen heading, two-tone |
| Title | 20 / 24 | 600 | −1% | Detail screen titles, sheet titles |
| Body | 16 / 24 | 400 | 0 | Running text, field values |
| Amount | 16 / 24 | 500 | 0 | Amounts in lists and fields, tabular figures |
| Label | 12 / 16 | 600 | +4%, capitals | Section labels, column heads, units ("KES") |
| Caption | 14 / 20 | 400 | 0 | Secondary lines (date and category) |

Every heading style has a muted variant for the two-tone pattern. Text scales with the system font size: rows
wrap, they never clip.

### Colour

| Token | Light | Dark | Use |
|---|---|---|---|
| `app` | `#F3F3F3` | `#050505` | Screen background |
| `tile` | `#FFFFFF` | `#161616` | Tiles, fields, sheets |
| `rule` | `#D4D4D2` | `#2B2B2B` | 1dp row lines, inactive borders, bar tracks |
| `muted` | `#5C5C5C` | `#A6A6A6` | Second heading line, labels, captions |
| `ink` | `#0A0A0A` | `#F3F3F3` | Text, icons, bars, selected chips |
| `accent` | `#1CFDD6` | `#1CFDD6` | Walenje aqua: fill of the primary action |
| `onAccent` | `#050505` | `#050505` | Text and icons on aqua |
| `warn` | `#E8A300` | `#F2B230` | "Needs review" square and badge |
| `warnText` | `#875800` | `#F5BD4F` | "Review" and "Check" words |
| `income` | `#0E7A3E` | `#4ADE80` | Income amounts, always with `+` |
| `error` | `#C4271B` | `#FF6B5E` | Errors such as a PIN mismatch, always with a message |
| `onError` | `#FFFFFF` | `#050505` | Text and icons on `error` |

Rules for aqua:

- Use it as a fill only, with `onAccent` text. Contrast is 16:1.
- Never use aqua for text or icons on a light background (1.3:1). Use `ink` instead.
- Aqua text is allowed in the dark theme only (15.6:1), and only for links.
- One aqua element per screen: the primary action.

Expenses use `ink` with a `−` sign where direction matters. Categories have no colours: name them. Charts use
`ink` bars on `rule` tracks, all on one scale.

### Shape and lines

| Element | Shape |
|---|---|
| Chips, fields, segmented controls, tags | 4dp radius |
| Tiles, photo frames, notes | 8dp radius |
| Pill buttons, circle icon buttons | Full radius |
| Row separator | 1dp `rule` |
| Group header rule, outlined controls | 1.5dp `ink` |

No shadows. No gradients, except a short fade under a control that is docked over scrolling content.

### Icons

Line icons on a 20dp grid, 1.8dp stroke, `currentColor`. Geometric glyphs in circles for actions: ✕ discard,
✓ confirm, ↗ open, + capture.

## Components

Build these once in `core/designsystem` and reuse them. They take tokens from the theme, not colour or size
parameters.

| Component | What it is |
|---|---|
| `TwoToneHeading` | Headline in ink with a muted second line at the same size |
| `MetricReadout` | Label, then a Display number with tabular figures, then an optional caption |
| `Tile` | 8dp-radius card on the grid: a label, one idea, an optional foot |
| `TickRuler` | Row of ticks that shows progress through a period. Past ticks ink, today tall. |
| `PillButton` | Full-radius button. Primary: aqua fill. Secondary: 1.5dp ink outline. |
| `CircleIconButton` | 48dp round button for ✕, ✓, ↗, +. Primary variant has an aqua fill. |
| `FilterChip` | 4dp-radius chip. Ink fill when selected. Optional count badge. |
| `SegmentedControl` | Row of options in a 1.5dp ink outline. The selected option has an ink fill. |
| `FieldRow` | Label and value on a rule. Optional source tag (`QR`, `SUGGESTED`) and Check state. |
| `ReceiptRow` | Merchant and amount on one baseline, date and category below, optional `StatusFlag` |
| `GroupHeader` | Label and total over a 1.5dp ink rule |
| `StatusFlag` | Amber square and a short word ("Review", "Check") |
| `CategoryBar` | Category name, amount and an ink bar on a shared scale |

## Screen recipes

| Screen | Journey | Heading | Hero | Body | Primary action |
|---|---|---|---|---|---|
| Home | J5 | "Business spend" / month | Month's business spend, `TickRuler` for the days | Tiles: needs review (count, ↗), top category. Three recent `ReceiptRow`s. | "Capture receipt" pill above the navigation bar |
| Capture sheet | J2 | "Capture" / none | None | Three options: Scan, Image, PDF | Scan |
| Processing | J3 | "Reading receipt" / page count | None | Photo with progress. Steps as captions: QR, text, fields. | None. ✕ cancels. |
| Review | J4 | "Check receipt" / "N fields need a look" | None: the photo leads | Photo (8dp frame, "eTIMS QR read" tag), then `FieldRow`s. Business/Personal `SegmentedControl`. | ✓ (aqua circle) bottom right. ✕ bottom left. "Save for later" pill between. |
| Receipts | J5 | "Receipts" / count this year | None | Search field, `FilterChip`s, `GroupHeader` per month, `ReceiptRow`s | + (aqua circle) bottom right, above the navigation bar |
| Receipt detail | J5 | Merchant / date | Total | Photo, `FieldRow`s, notes | Edit (outline pill). Delete sits in a menu and asks for confirmation. |
| Reports | J6 | "Report" / period | Business spend for the period, with VAT and count as caption | Period `SegmentedControl`, review note if needed, `CategoryBar`s | "Export PDF" pill. "CSV" outline pill beside it. |
| Settings | J7 | "Settings" / none | None | Groups: profile, security, backup (last backup date), about | "Back up now" when a backup is overdue |
| Onboarding | J1 | Two-tone, larger | None | The only place for editorial touches: oversized words, visible grid lines, corner microcopy | "Continue" pill |

Navigation bar: Home, Receipts, Reports, Settings. The selected item is ink and bold, the rest are muted. No
accent in the bar.

## Accessibility guardrails

- Text people need to read meets WCAG AA: 4.5:1 for body text, 3:1 for large text. Check both themes.
- Follow the aqua rules above.
- Touch targets are at least 48dp.
- Hero numbers have a plain content description, for example "Business spend, 48,230 shillings".
- A status never relies on colour: `StatusFlag` always shows a word.
- Layouts survive 200% font scale: text wraps, rows grow.

## Review checklist

Check every new screen and component:

1. Does everything snap to the 4-column grid and the 4dp baseline?
2. In greyscale, is the hierarchy still clear?
3. Is there exactly one aqua element, and is it the primary action?
4. Is there at most one hero number?
5. Does each tile hold one idea?
6. Is it Inter Tight only, with two-tone headings and tabular figures for amounts?
7. Do rules, not shadows, separate content?
8. Are the primary actions within thumb reach?
9. Does it pass the accessibility guardrails in light and dark themes?

## Current state

The design system follows this file for typography, colour, shape and spacing. The components are still to come.
Onboarding and the PIN keypad still hold some sizes off the scale (64, 128 and 40dp gaps; 40, 60 and 70dp
buttons); steps 4 and 5 replace them. Move to this direction in this order:

1. Typography: done. `WalenjeTheme.typography` holds the seven styles; `TextType` names them;
   `WalenjeText(muted = true)` gives the muted variant.
2. Colour: done. `WalenjeTheme.colors` holds the tokens for light and dark; the theme follows the system
   setting and maps the tokens onto the Material roles (aqua is `primary`, no tonal tint).
3. Shape and spacing: done. `WalenjeTheme.spacing` (steps named by 4dp units: `x4` = 16dp, plus roles such as
   `margin`), `WalenjeTheme.shapes` (`small`, `medium`, `full`) and `WalenjeTheme.lines` (`hairline`, `strong`).
   Material shapes stay on 4dp and 8dp.
4. Components: build `TwoToneHeading`, `MetricReadout`, `PillButton`, `CircleIconButton`, `Tile` and `ReceiptRow`
   first, then the rest as screens need them.
5. Restyle onboarding against the checklist. Then build Home from its recipe.
