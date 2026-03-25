# Feature Extraction Plan — Final Migration Phase

## Current State

**10 core modules extracted** (136 Kotlin files + 402 drawables). **132 Kotlin files remain in `:app`** — all feature UI, app shell, Glance widget, and navigation.

**8 feature module shells** exist in `feature/*/` with `build.gradle.kts` but **no source code** — files still live in `app/src/main/java/com/aowen/monolith/feature/*`.

### What Blocks Extraction

Features can't be separate modules today because they import from each other via:

| Dependency Type | Count | Example |
|---|---|---|
| **Route constants** | 12 | `BuildsRoute`, `ProfileRoute`, `SearchRoute` — used for transition animations |
| **Navigate functions** | 10 | `navigateToSearch()`, `navigateToBuildDetails()` — `NavController` extensions |
| **Shared UI components** | 5 | `MatchesList`, `PlayerSearchSection`, `RecentPlayersSection`, `PlayerResultCard`, `SearchScreenUiState` |
| **Shared helper** | 1 | `sharedViewModel` inline function in builds |

---

## Strategy: NIA Lambda-Navigation Pattern

The Now in Android (NIA) architecture decouples features from each other by:

1. **Features never import from other features** — only from `core:*` modules
2. **Features expose**: a `NavGraphBuilder` extension (e.g., `heroesScreen(...)`) that takes **lambda callbacks** for navigation instead of `NavController`
3. **`:app`'s `MonolithNavHost`** wires all features together — it imports all features' nav extensions and passes lambdas that call `navController.navigateTo*()` 
4. **`core:navigation`** holds shared route constants + `NavController` extension functions

### Key Design Decision: Where Do Navigate Functions Live?

**Option A (Recommended):** Move all `navigateTo*()` extensions + route constants to `core:navigation`. This is simplest — `:app` and features both depend on `core:navigation`.

**Option B:** Keep `navigateTo*()` in each feature's public API surface. Each feature defines its own routes and exposes navigate functions. `:app` imports from each feature. Other features never import navigate functions — they receive them as lambdas. More pure but more boilerplate.

**Recommendation: Option A** — it maps cleanly to our existing `Routes` object and minimizes changes.

---

## Execution Plan

### Phase 1: Prepare `core:navigation` (Prerequisite — unblocks all features)

**Goal:** Move ALL route constants, navigate functions, and `sharedViewModel` helper to `core:navigation` so features never need to import from each other.

**Steps:**
1. Move `sharedViewModel` inline function from `builds/addbuild/navigation/` → `core:navigation`
2. Replace all local `const val *Route` definitions in nav files with imports from `Routes.*`
3. Move all `NavController.navigateTo*()` extension functions to `core:navigation`
4. Verify app compiles with all navigation now routed through `core:navigation`

**Files created/modified:**
- `core/navigation/src/.../navigation/Routes.kt` — already has constants, add navigate functions
- `core/navigation/src/.../navigation/SharedViewModel.kt` — new file for `sharedViewModel` helper
- All 17 navigation files in `app/src/main/java/.../feature/*/navigation/` — remove local constants and navigate functions, import from `core:navigation`

**Dependencies to add to `core:navigation/build.gradle.kts`:**
```kotlin
api(libs.androidx.compose.navigation)
api(libs.androidx.hilt.navigation.compose) // for sharedViewModel
```

### Phase 2: Refactor Screen Composables to Lambda Navigation

**Goal:** Every screen `@Composable` accepts navigation callbacks as lambda parameters instead of `NavController`.

**Current pattern (every screen):**
```kotlin
fun ProfileScreenRoute(
    navController: NavController,  // ← feature depends on NavController + other features
    ...
)
```

**Target pattern:**
```kotlin
fun ProfileScreenRoute(
    onNavigateToSearch: () -> Unit,       // ← pure lambdas
    onNavigateToLogin: () -> Unit,
    ...
)
```

**Per-feature refactoring:**
| Feature | Screen Composables | Navigate Calls to Convert |
|---|---|---|
| auth | `LoginRoute` | `navigateToHome` |
| matches | `MatchDetailsRoute`, `MoreMatchesRoute`, `MatchesList` | `navigateToPlayerDetails` |
| profile | `ProfileScreenRoute` | `navigateToSearch`, `navigateToLoginFromLogout` |
| items | `ItemsScreenRoute`, `ItemDetailRoute` | `navigateToSearch` |
| heroes | `HeroesScreenRoute`, `HeroDetailRoute` | `navigateToSearch`, `navigateToBuildDetails` |
| builds | `BuildsScreenRoute`, `BuildDetailRoute`, `AddBuild*` | `navigateToSearch` |
| search | `SearchScreenRoute` | `navigateToBuildDetails`, `navigateToHeroDetails`, `navigateToPlayerDetails`, `navigateToItemDetails`, `navigateToMatchDetails` |
| home | `HomeScreenRoute`, `PlayerDetailScreen`, `HeroWinPickRateRoute` | `navigateToSearch`, `navigateToBuildDetails`, `navigateToHeroDetails`, `navigateToMoreMatches`, `navigateToMatchDetails` |

**Each feature's nav graph builder also changes:**
```kotlin
// Before:
fun NavGraphBuilder.heroesScreen(navController: NavController) {
    composable(Routes.HEROES) {
        HeroesScreenRoute(navController)
    }
}

// After:
fun NavGraphBuilder.heroesScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToBuildDetails: (String) -> Unit,
    onNavigateToHeroDetails: (Long, String) -> Unit,
) {
    composable(Routes.HEROES) {
        HeroesScreenRoute(
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToBuildDetails = onNavigateToBuildDetails,
        )
    }
    heroDetailsScreen(
        onNavigateToSearch = onNavigateToSearch,
        ...
    )
}
```

**And `MonolithNavHost` wires it all:**
```kotlin
fun MonolithNavHost(navController: NavHostController, ...) {
    NavHost(...) {
        heroesScreen(
            onNavigateToSearch = { navController.navigateToSearch() },
            onNavigateToBuildDetails = { id -> navController.navigateToBuildDetails(id) },
            ...
        )
    }
}
```

### Phase 3: Move Remaining Shared Components

Before features can be separate modules, the last cross-feature UI deps must move to core:

| Component | Current Location | Target | Used By |
|---|---|---|---|
| `MatchesList` | `feature.matches` | `core:designsystem` | home |
| `PlayerSearchSection` | `feature.home` | `core:designsystem` | search |
| `RecentPlayersSection` | `feature.home` | `core:designsystem` | search |
| `PlayerResultCard` | `feature.search.components` | `core:designsystem` | home |
| `SearchScreenUiState` / `PlayersListState` | `feature.search` | `core:data` | home |

**Note:** `PlayerSearchSection` and `RecentPlayersSection` currently depend on `SearchScreenUiState` which needs to move to `core:data` first.

### Phase 4: Copy Feature Source to Feature Modules

For each feature (in dependency order):
1. Copy all `.kt` files from `app/src/main/java/.../feature/{name}/` → `feature/{name}/src/main/kotlin/.../feature/{name}/`
2. Update `feature/{name}/build.gradle.kts` with needed deps (add `core:navigation`, `core:network` where needed)
3. Add `implementation(project(":feature:{name}"))` to `app/build.gradle.kts`
4. Delete the feature files from `app/`
5. Verify `assembleDebug` passes

**Extraction order** (least-coupled first):
1. **`:feature:auth`** (4 files, 0 remaining cross-feature deps after Phase 2)
2. **`:feature:matches`** (10 files, 0 cross-feature deps after Phase 2+3)
3. **`:feature:profile`** (5 files, 0 cross-feature deps after Phase 2)
4. **`:feature:items`** (7 files, 0 cross-feature deps after Phase 2)
5. **`:feature:heroes`** (7 files, 0 cross-feature deps after Phase 2)
6. **`:feature:builds`** (23 files, 0 cross-feature deps after Phase 2)
7. **`:feature:search`** (7 files, 0 cross-feature deps after Phase 2+3)
8. **`:feature:home`** (16 files, 0 cross-feature deps after Phase 2+3) — extracted last because it has the most deps

### Phase 5: App Shell Cleanup

1. `:app` should only contain: `MainActivity`, `MainApplication`, `MonolithNavHost`, `MonolithApp`, `MonolithAppState`, `AppModule` (DI), Glance widget code
2. Move `FullScreenLoadingIndicator` from `MainActivity.kt` to `core:designsystem` (if not already)
3. Clean up `app/build.gradle.kts` — remove feature-specific deps (accompanist, paging, markdown, coil) since they'll be in feature modules
4. Verify final `assembleDebug` + run tests

---

## Work Breakdown for Developer Sub-Agents

### Agent 1: Navigation Foundation
**Scope:** Phase 1 entirely
- Populate `core:navigation` with all route constants (wire `Routes.*`), navigate functions, and `sharedViewModel` helper
- Update all 17 navigation files to import from `core:navigation`
- Verify build

### Agent 2: Screen Lambda Refactoring (Batch A — simpler features)
**Scope:** Phase 2 for auth, matches, profile, items
- Convert each screen from `NavController` → lambda params
- Update corresponding nav graph builders
- Update `MonolithNavHost` wiring for these features

### Agent 3: Screen Lambda Refactoring (Batch B — complex features)
**Scope:** Phase 2 for heroes, builds, search, home
- Same pattern but more navigation callbacks
- Handle `sharedViewModel` usage in search + builds
- Update `MonolithNavHost` wiring for these features

### Agent 4: Shared Component Extraction + Feature Module Migration
**Scope:** Phase 3 + Phase 4 + Phase 5
- Move remaining shared components to core modules
- Copy feature source to feature module directories
- Wire feature module deps
- Delete from app, verify build
- Final app shell cleanup

---

## Risk Mitigation

- **Build after every feature extraction** — never extract two features at once without verifying
- **Smart cast issues** — any nullable property from cross-module data classes needs local val capture
- **R imports** — feature modules get their own `R` class. Drawables are in `core:resources`, so features import `com.aowen.monolith.core.resources.R` for drawables and their own `R` for feature-specific resources
- **Hilt** — each feature module needs the Hilt plugin. ViewModels annotated with `@HiltViewModel` work across modules automatically
- **String resources** — features may reference `R.string.*` from app. Either move strings to `core:resources` or duplicate per-feature

## Success Criteria
- `./gradlew :app:assembleDebug` passes
- `:app` has < 30 Kotlin files (shell + Glance only)
- Every `feature:*` module compiles independently (`./gradlew :feature:auth:compileDebugKotlin`)
- Zero cross-feature imports (`rg "import com.aowen.monolith.feature\." feature/*/src` returns nothing)
