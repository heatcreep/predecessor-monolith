# Hero Stats: Network-Only Fetch for Profile Screen

## Problem

Hero stats are fetched from the network via `PlayerFragment` but dropped during Room persistence:
- `UserFragment.asEntity()` maps to `PlayerEntity` which has no hero stats field
- `CurrentUserEntity.asCurrentUser()` hardcodes `heroStatistics = emptyList()`

Since `ProfileViewModel.uiState` reads from `currentUserState` (which always goes through Room), hero stats on the profile screen are always empty.

Meanwhile, `PlayerDetailsViewModel` works correctly because it calls `PlayerRepository.fetchPlayerById()` which maps through `PlayerFragment.asPlayer()` directly from the network.

## Decision: Don't Store Hero Stats in Room

Hero stats change after every match and contain per-hero breakdowns (kills, deaths, assists, win rates) across all heroes a player has used. Storing them in Room means:
- Stale data shown until the next sync
- Additional Room entity/converter complexity for data that's always re-fetched anyway
- No offline use case justifies caching (you can't play matches offline)

The match history pattern already validates this approach — it fetches from network on each visit with no Room caching.

## Design

### Data Flow

```
Room path (unchanged): cached user → playerCard, rank, name (fast, cached)
Network path (new):    fetchCurrentUser() → extract heroStats → heroStatsState StateFlow → UI
```

The Room-backed `currentUserState` flow continues providing user profile card data immediately from cache. Hero stats get their own fetch triggered in `ProfileViewModel.init`, producing a separate `StateFlow` with loading/success/error states.

### ViewModel Changes

New sealed interface in `ProfileViewModel`:

```kotlin
sealed interface HeroStatsUiState {
    data object Loading : HeroStatsUiState
    data class Loaded(val stats: List<HeroStatisticsUiModel>) : HeroStatsUiState
    data class Error(val message: String) : HeroStatsUiState
}
```

- `ProfileViewModel` exposes `heroStatsState: StateFlow<HeroStatsUiState>`
- On `SignedIn`, calls `userRepository.fetchCurrentUser()`, maps hero stats to `HeroStatisticsUiModel`, emits to `heroStatsState`
- `CurrentUserUiModel.players` changes from `List<PlayerProfileUiModel>` to `List<PlayerProfileCardUiModel>` (hero stats no longer bundled)

### UI Changes

- `ProfileScreen` collects `heroStatsState` separately from `uiState`
- Heroes tab (tab 1) switches on `heroStatsState`: Loading → spinner, Loaded → LazyColumn, Error → error + retry
- Matches tab and profile card are unchanged
- `PlayerProfileLayout` stays untouched (PlayerDetailsViewModel still uses bundled `PlayerProfileUiModel`)

### Bug Fix

`toPlayerProfileUiModel()` lines 52-53: `averageDeaths` and `averageAssists` both use `heroStatistic.averageKills`. Fixed as part of extracting the hero stats mapping to `ProfileViewModel`.

### Scope

- Only `ProfileViewModel` and `ProfileScreen` change
- `PlayerDetailsViewModel` continues working as-is (direct network fetch via `PlayerRepository.fetchPlayerById()`)
- No GraphQL changes
- No new dependencies added to `ProfileViewModel`
- `PlayerProfileUiModel` and `toPlayerProfileUiModel()` remain for `PlayerDetailsViewModel`'s use
