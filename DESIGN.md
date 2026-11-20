# DESIGN.md

Walenje's visual direction. Read it before designing or building a screen or a `core/designsystem` component.
`AGENTS.md` > Architecture rules still decides where UI code lives; this file decides what it looks like.

## Reference

The direction follows the principles in Slava Kornilov's work for Geex Arts (<https://dribbble.com/slavakornilov>):
Swiss-style typography, a neutral base with a few loud accents, bento tiles, and data as the hero. Take the
principles, not his layouts: Walenje should read as its own product, not a copy of a Dribbble shot.

Dribbble shots are concept art. Where this file and a shot disagree on legibility or accessibility, this file wins.

## Principles

1. **Hierarchy comes from type, not decoration.** One neo-grotesque family with large jumps in size and weight. Use
   two-tone headings: a title in the primary text colour with a muted subtitle at the same size directly beneath it
   (for example "Spending" / "This month").
2. **Neutral first, colour with meaning.** Near-white, near-black and greys carry the layout. Every accent colour
   means something: the brand, a category, income or expense. A screen should still read in greyscale.
3. **One idea per tile.** Group content into bento tiles with large radii. Each tile holds one idea and at most one
   hero element. Number badges (`01`, `02`) mark order or count where it helps.
4. **Data is the hero.** Each screen or tile has one large number: a balance, a month's spend, a percentage. Make it
   big and back it with a small visual (a ring, gauge, tick ruler or sparkline), not a paragraph.
5. **Controls sit in the thumb zone.** Primary actions live at the bottom: circular cancel (✕) and confirm (✓)
   buttons in the corners, a pill in between, and arc or tick dials along the bottom edge for picking values.
6. **Objects, not panels.** Tiles may carry their own soft gradient or a hardware-like detail (a knob, a dial, a
   segmented readout) so they feel like things you can touch. Use this sparingly: one or two per screen.
7. **Geometric glyphs.** Bold, simple icons: letters or numbers in circles, thick diagonal arrows (↗) in circles for
   "open" or "go", pills for filters.

## Foundations

Tokens live in `core/designsystem/theme/`. Feature code never hard-codes them (see `AGENTS.md` > UI).

### Typography

- **Text family:** a neo-grotesque sans. Inter Tight / Inter Display is the default choice (OFL, free); Hanken
  Grotesk or Schibsted Grotesk are alternatives. The design system uses Inter Tight.
- **Numeric display face:** one face only for hero numbers: Doto for a dot-matrix look or Geist Mono for a calmer
  one. Never use it in lists, body text or anywhere dense.
- **Figures:** amounts use tabular figures (`fontFeatureSettings = "tnum"`) so columns line up.
- **Scale:** few sizes with big jumps. Display sizes get tighter tracking (about −2% to −4%).
- **Muted variant:** every heading style has a muted colour variant for the two-tone pattern.

### Colour

- **Neutrals:** a ramp from near-white (about `#F3F3F3`) to near-black (about `#050505`) with mid greys between.
  Light and dark themes both come from this ramp.
- **Brand accent:** Walenje aqua `#1CFDD6`, used for the single most important action or highlight on a screen.
- **Category accents:** each spending category gets one saturated colour (for example food orange, transport cobalt,
  bills violet, entertainment magenta). Tiles, charts and chips for that category use it.
- **Semantic:** income, expense, warning and error get their own tokens. Never signal money direction by colour
  alone: always show the `+` or `−` sign.
- **Gradients:** soft mesh gradients built from a category accent and a neighbour, only as tile backgrounds with
  high-contrast text on top.

### Shape and spacing

- Tiles and cards: 24–28dp corner radius.
- Buttons: full pills. Icon buttons: circles, at least 48dp touch target.
- Spacing on a 4dp grid; generous outer margins, tight gaps between tiles (8–12dp).

## Components

Build these once in `core/designsystem` and reuse them. Each takes tokens from the theme, not parameters for colour
or size, except where a category accent is passed in.

| Component | What it is |
|---|---|
| `Tile` | Bento card: two-tone title, optional number badge, one content slot, optional accent or gradient |
| `MetricReadout` | Hero number with unit and label, in the numeric display face |
| `GradientCard` | `Tile` variant with a soft mesh gradient background |
| `CircleIconButton` | Round icon button (✕, ✓, ↗) |
| `NumberBadge` | Number or letter in a circle |
| `TickDial` | Arc or straight tick ruler for picking or showing a value |
| `CardStack` | Vertically stacked, overlapping cards that expand on tap (accounts, wallets) |

## Screen recipes

- **Home:** a bento grid. The balance is the hero `MetricReadout`; around it a "this month" spend tile, a top-category
  tile in that category's colour, and a budget ring.
- **Accounts:** a `CardStack`, one card per account or wallet.
- **Analytics:** one `GradientCard` per category, each with one number and one gauge or tick ruler.
- **Add transaction:** the PIN-keypad style keypad, an amount readout, a category picker (`TickDial` or chips) and
  ✕ / ✓ in the bottom corners.
- **Onboarding and marketing:** the only place editorial touches (oversized background words, grid lines, corner
  microcopy) belong. Keep them out of everyday screens.

## Accessibility guardrails

- Text people need to read meets WCAG AA contrast: 4.5:1 for body text, 3:1 for large text. The pale grey-on-grey
  microcopy common in concept shots is decoration only.
- Touch targets are at least 48dp.
- Hero numbers also have a plain-text content description (for example "Balance, 12,400 shillings").
- Gradients never sit behind small text without a contrast check in both themes.

## Review checklist

Run through this for every new screen or component:

1. In greyscale, is the hierarchy still clear?
2. Does every accent colour mean something?
3. Is there exactly one hero element per screen or tile?
4. Does each tile hold one idea?
5. Is it one text family plus at most one numeric face, with two-tone headings?
6. Are the primary actions within thumb reach?
7. Does it pass the accessibility guardrails above in light and dark themes?

## Current state

The design system does not follow this yet: it uses Inter Tight on the Material 3 scale and a generated mint palette in which
primary, secondary and tertiary are the same green, and the components above don't exist. Moving to this
direction, in order:

1. Typography: add the numeric face to `composeResources/font`, and rebuild `Type.kt` and
   `TextType` around the new scale and the muted variant.
2. Colour: replace the generated palette in `Color.kt` with the neutral ramp, brand, category and semantic tokens.
3. Shape: add the radius and spacing tokens to the theme.
4. Components: build `Tile`, `MetricReadout`, `CircleIconButton` and `NumberBadge` first, then the rest as screens
   need them.
5. Restyle onboarding against the checklist, then build Home from the recipe.
