# Retrofit Removal & GraphQL Migration Design

**Date:** 2025-07-30
**Deadline:** 2025-07-31
**Approach:** Bottom-up (network layer → repositories → auth → DI/deps → tests)

## Section 1: Network Layer Teardown

### Delete
- `core/network/.../retrofit/RetrofitOmedaCityNetwork.kt` — Retrofit service interface + implementation
- `core/network/.../PredCompanionNetworkDataSource.kt` — interface returning `retrofit2.Response`
- `core/network/.../SafeApiCall.kt` — Retrofit-specific wrapper
- `core/common/.../network/RetrofitHelper.kt` — omeda.city base URL + image URL helpers
- `core/network/.../di/DataSourceModule.kt` binding for `PredCompanionNetworkDataSource`
- All `NetworkXxx` model DTOs in `core/network/model/` used only by Retrofit
- `core/common/.../network/utils/Utils.kt` — OkHttp cache utility
- Retrofit + OkHttp dependencies from `libs.versions.toml` and `build.gradle.kts`

### Keep
- `ApolloClient`, `PredGGNetworkDataSource`, `ApolloKotlinPredGGNetwork`, `SafeGraphQlCall.kt`, `Resource.kt`
- `AuthApolloInterceptor` + `AuthTokenProvider`
- OkHttp only if Apollo 5 still needs it as transport

### Expand
- Add missing methods to `PredGGNetworkDataSource` for builds, hero statistics data, and player search

## Section 2: Repository Migration

### Builds (`OmedaCityBuildRepository`)
- Switch from Retrofit to `PredGGNetworkDataSource`
- Fill out `BuildsQuery.graphql` if needed to cover all fields
- Map GraphQL response to existing `HeroBuild` / `FavoriteBuildListItem` domain classes

### Heroes (`OmedaCityHeroRepository`)
- Remove `PredCompanionNetworkDataSource` dependency
- Compute hero statistics (win rate, pick rate) from hero match data in the hero query
- `HeroStatistics` domain class stays — populated differently

### Players (`OmedaCityPlayerRepository`)
- Remove all Retrofit calls, move remaining methods to GraphQL
- If no GraphQL player search query exists on pred.gg, disable search functionality rather than blocking the migration

### Items (`OmedaCityItemRepository`)
- Remove lingering `PredCompanionNetworkDataSource` injection (already mostly GraphQL)

### Matches (`OmedaCityMatchRepository`)
- No changes needed — already fully GraphQL

### Naming
- Keep `OmedaCity` prefix on repository class names — cosmetic rename deferred

## Section 3: Auth Gate & Supabase Removal

### Delete
- `SupabaseAuthService.kt` / `SupabaseAuthServiceImpl`
- `SupabaseAuthRepository` / `AuthRepository.kt` (Supabase one)
- `FakeSupabaseAuthService.kt`
- `AuthRepositoryTest.kt`
- `LoginViewModel.kt`, `LoginScreen.kt` (Supabase/Discord login UI)
- `LoginEntryProvider.kt`
- `DeepLinksHandlerActivity.kt`
- Supabase dependencies from gradle
- Supabase DI bindings in `DataModule.kt` and `NetworkModule.kt`

### Keep
- `AppAuthRepository` / `NewAuthRepository` (pred.gg OAuth2)
- `PredGgAuthTokenProvider` / `AuthTokenProvider`
- `AuthStateDataStore`
- `AuthApolloInterceptor`
- `AuthLauncher.kt`

### Sign-In Gate (`MainActivity`)
- Observe `UserState` from `UserRepository`
- `SignedOut` → full-screen sign-in composable with "Sign in with pred.gg" button
- `SignedIn` → `MonolithApp` (normal nav scaffold)
- `Loading` → splash/loading indicator
- No guest access

### New Sign-In Screen
- Simple composable: app logo + "Sign in with pred.gg" button
- Triggers AppAuth intent flow via `AuthLauncher`
- Reuses existing `AppAuthRepository` wiring

## Section 4: DI & Dependency Cleanup

### Gradle
- Remove `retrofit-core`, `retrofit-serialization-converter`, `okhttp-logging` from `libs.versions.toml`
- Remove Supabase dependencies (auth, postgrest, gotrue, client)
- Remove from all `build.gradle.kts` that reference them
- Keep OkHttp only if Apollo 5 requires it

### DI Modules
- `NetworkModule.kt`: Remove `okHttpCallFactory()` (unless Apollo needs it), remove Supabase providers
- `DataSourceModule.kt`: Remove `PredCompanionNetworkDataSource` binding
- `DataModule.kt`: Remove `SupabaseAuthRepository` → `AuthRepository` binding
- Repository modules: Update constructor params to remove `PredCompanionNetworkDataSource`

### Dead Code Sweep
- Delete unreferenced `NetworkXxx` DTOs
- Relocate image URL helpers if still used

## Section 5: Unit Tests

### Delete (stale beyond repair)
- `AuthRepositoryTest.kt` — tests removed Supabase flow
- `UserRepositoryTest.kt` — empty
- `LoginViewModelTest.kt` — tests removed Supabase login

### Fix
- `HeroRepositoryTest.kt` — update constructor, fix assertions for StateFlow API
- `ItemRepositoryTest.kt` — same pattern
- `BuildRepositoryTest.kt` — migrate to GraphQL fake
- Feature ViewModel tests — fix any that break from changed repo interfaces

### Test Infrastructure
- Delete `TestPredCompanionNetworkDataSource.kt` (returns `retrofit2.Response`)
- Delete `FakeSupabaseAuthService.kt`
- Create `TestPredGGNetworkDataSource` fake for repository tests

### Future Test Plan (out of scope for deadline)
- Auth gate flow (signed out → sign-in → signed in → main app)
- `AppAuthRepository` token refresh and error handling
- Computed hero statistics logic
- Builds repository with GraphQL
- End-to-end navigation after auth
