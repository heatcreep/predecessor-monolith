# PredCompanion Multi-Module Migration Plan (Now in Android Style)

## Current Status: Phase 6 Complete - Core + Shared UI Extraction Done

### Modules Extracted (136 Kotlin files + 402 drawables moved out of app)

| Module | Files | Purpose |
|--------|-------|---------|
| `:core:resources` | 402 drawables + shared strings | Shared drawable/string resources |
| `:core:common` | 5 | RetrofitHelper, BigDecimalSerializer, Utils, LoggingUtils, DI Qualifiers |
| `:core:model` | 30 | All data models, DTOs, enums (Hero, PlayerDetails, ItemDetails, etc.) |
| `:core:database` | 9 | Room DB, DAOs, entities, DI modules |
| `:core:datastore` | 3 | Theme/User/ClaimedPlayer preferences |
| `:core:network` | 6 | OmedaCityService, Supabase services, SafeApiCall, Resource |
| `:core:data` | 23 | All repositories, UI mappers (BuildListItemUiMapper, HeroUiModel, PlayerProfileUiMapper) |
| `:core:designsystem` | 59 | Theme, shared UI components, cards, dropdowns, filters, shared cross-feature components |
| `:core:navigation` | 1 | Centralized route constants |

### App Module (132 remaining Kotlin files)
- Features: auth, builds, heroes, home, items, matches, profile, search
- App shell: MainActivity, Application, MonolithNavHost, MonolithApp
- Glance widget code
- Feature-specific UI components still tightly coupled via navigation

---

## Remaining Work: Feature Module Extraction (Phase 7)

### Blocker: Cross-Feature Navigation
Features currently depend on each other via:
1. **NavController extension functions** (`navigateToSearch()`, `navigateToBuildDetails()`, etc.)
2. **Route constants** imported from other features (already centralized in `core:navigation` but not yet wired)
3. **Shared ViewModel** (`sharedViewModel` helper in builds)
4. **Feature-specific UI** (PlayerSearchSection, RecentPlayersSection depend on search state)

### How to Resolve (NIA Pattern)
1. Move all `navigateTo*()` functions to `core:navigation` as standalone functions
2. Refactor all screen composables to accept navigation lambdas instead of `NavController`
3. Wire lambdas in `MonolithNavHost` (in `:app`) 
4. Move `sharedViewModel` helper to `core:navigation`
5. Extract features one at a time, starting with least-coupled

### Recommended Feature Extraction Order
1. **auth** (1 cross-feature dep) → Just `navigateToHome`
2. **matches** (1 cross-feature dep) → Just `navigateToPlayerDetails` 
3. **profile** (2 cross-feature deps) → `navigateToLoginFromLogout`, `navigateToSearch`
4. **items** (4 cross-feature deps) → Route constants + `navigateToSearch`
5. **heroes** (8 cross-feature deps) → Route constants + navigation
6. **builds** (3 cross-feature deps) → Route constants + `navigateToSearch`
7. **search** (8 cross-feature deps) → Various navigation + search sections
8. **home** (23 cross-feature deps) → Most coupled, extract last

---

## Build-Logic Convention Plugins Available
- `predcompanion.android.application` / `predcompanion.android.application.compose`
- `predcompanion.android.library` / `predcompanion.android.library.compose`
- `predcompanion.android.feature.impl`
- `predcompanion.android.room`
- `predcompanion.hilt`
- `predcompanion.jvm.library`

## Key Architecture Decisions
- Models use `core.resources.R` for drawables (not `com.aowen.monolith.R`)
- Rank colors defined in `core:model` (not theme) so RankDetails enum works standalone
- `BuildConfig.SUPABASE_API_KEY` replaced with injectable `@SupabaseApiKey` qualifier in `core:network`
- DI qualifiers (`IoDispatcher`, `SupabaseApiKey`) live in `core:common`
- Room upgraded from 2.6.1 → 2.7.1 for Kotlin 2.3.0 compatibility
- Entity mapper extensions extracted to `EntityMappers.kt` in database package
