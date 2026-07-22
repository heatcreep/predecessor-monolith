# Auth-Gated Hero Statistics

## Problem

The `heroStatistics` field on `Player` requires authentication on the pred.gg GraphQL API. Currently, `PlayerFragment.graphql` always requests this field, meaning unauthenticated users hit an auth-gated field they can't access. The goal is to never query `heroStatistics` when the user isn't signed in, and to show a sign-in prompt on the Heroes tab instead.

## Approach

Split the GraphQL fragment into public and authed variants. The repository selects which query to use based on auth state. The domain model uses nullability to signal "not fetched" vs "empty," and the UI renders a sign-in prompt when the data is absent.

## Design

### GraphQL Layer

**`PlayerFragment.graphql`** — public fields only (remove `heroStatistics`):

```graphql
fragment PlayerFragment on Player {
    id
    name
    favRole
    ratings { ...PlayerRatingFragment }
    generalStatistic { result { matchesPlayed matchesWon } }
    favHero { ...HeroFragment }
}
```

**`PlayerWithHeroStatsFragment.graphql`** — new fragment extending public with auth-gated field:

```graphql
fragment PlayerWithHeroStatsFragment on Player {
    ...PlayerFragment
    heroStatistics {
        results {
            hero { ...HeroFragment }
            matchesPlayed
            matchesWon
            totalKills
            totalDeaths
            totalAssists
        }
    }
}
```

**`GetPlayerQuery.graphql`** — unchanged name, now uses the slimmed `PlayerFragment`:

```graphql
query GetPlayerQuery($playerKey: PlayerKey!) {
    player(by: $playerKey) {
        ...PlayerFragment
    }
}
```

**`GetAuthedPlayerQuery.graphql`** — new query using the authed fragment:

```graphql
query GetAuthedPlayerQuery($playerKey: PlayerKey!) {
    player(by: $playerKey) {
        ...PlayerWithHeroStatsFragment
    }
}
```

### Domain Model

`Player.heroStatistics` changes from `List<PlayerHeroStatistics>` to `List<PlayerHeroStatistics>?`.

- `null` = not fetched (user is not authenticated)
- `emptyList()` = fetched but player has no hero stats

### Data Layer

**Mapping functions:**

- `PlayerFragment.asPlayer()` sets `heroStatistics = null` (public fragment has no hero stats data).
- A new `PlayerWithHeroStatsFragment.asPlayer()` mapper populates `heroStatistics` from the authed fragment response.

**Network data source (`PredGGNetworkDataSource` / `ApolloKotlinPredGGNetwork`):**

Add a new method `getAuthedPlayer(playerId: String)` that executes `GetAuthedPlayerQuery`.

**Repository (`OmedaCityPlayerRepository`):**

`fetchPlayerById` checks auth state (via `NewAuthRepository.isLoggedIn` or `freshAccessToken()`) to decide which query to use:

- Authenticated: call `getAuthedPlayer()`, map via `PlayerWithHeroStatsFragment.asPlayer()`
- Unauthenticated: call `getPlayer()`, map via `PlayerFragment.asPlayer()` (heroStatistics = null)

### UI Layer — Player Detail Screen

The Heroes tab (tab index 1) in `PlayerDetailScreen` inspects `Player.heroStatistics`:

- **`null`**: Render a sign-in prompt — a card/section with text like "Sign in to view hero statistics" and a sign-in button (matching the style used in `ProfileScreen`'s `SignedOut` state).
- **non-null**: Render the hero statistics list.

The tab remains visible and tappable regardless of auth state.

### AuthedUserQuery / UserFragment

`UserFragment.graphql` currently references `PlayerFragment` via `players { ...PlayerFragment }`. Since `currentUser` is always an authed context, `UserFragment` must switch to `PlayerWithHeroStatsFragment` so hero stats are still fetched for the profile screen.

```graphql
fragment UserFragment on User {
    id
    name
    players {
        ...PlayerWithHeroStatsFragment
    }
}
```

The `OfflineFirstUserRepository` mapper for `UserFragment` will need to use the authed `asPlayer()` variant.

### ProfileScreen

No changes needed to the screen itself. `ProfileScreen` already gates behind auth (`UserUiState.SignedOut` shows sign-in button). The `UserFragment` update above ensures hero stats continue to flow through.

## Files Changed

| File | Change |
|---|---|
| `core/network/.../PlayerFragment.graphql` | Remove `heroStatistics` block |
| `core/network/.../PlayerWithHeroStatsFragment.graphql` | New file — extends PlayerFragment with heroStatistics |
| `core/network/.../GetAuthedPlayerQuery.graphql` | New file — uses PlayerWithHeroStatsFragment |
| `core/network/.../UserFragment.graphql` | Change `players` to use `PlayerWithHeroStatsFragment` |
| `core/model/.../Player.kt` | `heroStatistics: List<PlayerHeroStatistics>?` (nullable) |
| `core/data/.../model/Player.kt` | Update `asPlayer()` mapper, add authed variant |
| `core/network/.../PredGGNetworkDataSource.kt` | Add `getAuthedPlayer()` method |
| `core/network/.../ApolloKotlinPredGGNetwork.kt` | Implement `getAuthedPlayer()` |
| `core/data/.../players/OmedaCityPlayerRepository.kt` | Inject auth state, branch on auth for query selection |
| `feature/home/.../playerdetails/PlayerDetailScreen.kt` | Heroes tab: sign-in prompt when heroStatistics is null |
| `feature/home/.../playerdetails/PlayerDetailsViewModel.kt` | Pass auth state or sign-in action to UI |
