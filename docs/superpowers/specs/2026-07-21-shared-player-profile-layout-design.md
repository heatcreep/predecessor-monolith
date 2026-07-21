# Shared Player Profile Layout

## Goal

Make the ProfileScreen layout reusable so that both the current user's profile (`feature/profile`) and any looked-up player's profile (`feature/home/playerdetails`) share the same visual layout. Each screen remains a separate nav route with its own ViewModel, but the body content (profile card + chip filter tabs + tab content) is a single shared composable.

## Decisions

- **Target layout:** ProfileScreen's compact `PlayerProfileCard` row + chip filter tabs (Matches / Heroes / Friends & Enemies)
- **Tabs:** Same three tabs for both screens
- **Data model:** Both screens will use the `Player` domain model (ProfileScreen already does; PlayerDetailScreen will after GraphQL migration) and map to a shared `PlayerProfileCardUiModel`
- **Match history:** Paged for both screens. PlayerDetailScreen's paging migration happens as part of its GraphQL migration (not this effort), since the current Retrofit-backed fetch is being replaced. The shared layout's content slot accepts either `LazyPagingItems` or a plain list, so PlayerDetailScreen can adopt paging when it migrates.
- **PlayerDetailScreen extras:** Claim/unclaim functionality is handled in that screen's Scaffold (top bar action + dialog), outside the shared layout

## Shared UI Model

`PlayerProfileCardUiModel` and `Player.toPlayerProfileCardUiModel()` move from `feature/profile/impl/ui/PlayerProfileCard.kt` to `core/ui/model/`.

```kotlin
// core/ui/model/PlayerProfileCardUiModel.kt
data class PlayerProfileCardUiModel(
    val rankIconUrl: String,
    val playerName: String,
    val rankPoints: String,
    val rankTitle: String,
    val winPercentage: String,
    val region: String,
    val favoriteHeroIconUrl: String,
)

fun Player.toPlayerProfileCardUiModel(): PlayerProfileCardUiModel
```

No new model is needed. `core:ui` already depends on `core:model` via `api(projects.core.model)`, so `Player` is accessible.

## Shared Composables

### PlayerProfileCard

Moves from `feature/profile/impl/ui/PlayerProfileCard.kt` to `core/ui/cards/playerprofile/PlayerProfileCard.kt`. Purely presentational, takes `PlayerProfileCardUiModel`.

### PlayerProfileLayout

New composable in `core/ui/cards/playerprofile/PlayerProfileLayout.kt`. Composes the profile card, chip filter tab row, and a content slot.

```kotlin
@Composable
fun PlayerProfileLayout(
    profileCard: PlayerProfileCardUiModel,
    tabLabels: List<@StringRes Int>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (selectedTab: Int) -> Unit,
)
```

This composable renders:
1. `PlayerProfileCard` at the top
2. A `LazyRow` of `PredCompanionChipFilter` chips for tab selection
3. The `content` slot, which each screen fills with its own tab body (match list, heroes, etc.)

## Screen-Specific Concerns

### ProfileScreenRoute (current user)

- Scaffold: "Profile" title, search + settings icons
- Settings bottom sheet (console, theme, sign out)
- Auth state handling (Loading / SignedOut / Error / UserInfoLoaded)
- Login button when signed out
- When signed in: maps `CurrentUser.players.first()` to `PlayerProfileCardUiModel`, renders `PlayerProfileLayout`
- Tab content: paged match history for Matches tab; Heroes and Friends & Enemies tabs (currently empty stubs)

### PlayerDetailsRoute (any player)

- Scaffold: "Player Details" title, back arrow + claim/unclaim heart
- Unclaim player dialog
- Pull-to-refresh
- Maps `Player` to `PlayerProfileCardUiModel`, renders `PlayerProfileLayout`
- Tab content: paged match history for Matches tab (migrated from current finite list fetch)
- Claim/unclaim is entirely in this screen's Scaffold, not in the shared layout

## Cleanup

Files to remove or replace once migration is complete:

| File | Action |
|------|--------|
| `feature/profile/impl/ui/PlayerProfileCard.kt` | Composable moves to `core:ui`; model + mapper move to `core:ui/model` |
| `feature/home/impl/playerdetails/PlayerProfilePlayerStatsCard.kt` | Replaced by shared `PlayerProfileCard` |
| `core/ui/cards/playerprofile/PlayerProfileTitleCard.kt` | Remove (earlier iteration, unused after migration) |
| `core/ui/model/mapper/PlayerProfileUiMapper.kt` | Remove once PlayerDetailScreen uses `Player` model via GraphQL |
| `core/ui/model/mapper/PlayerCardMapper.kt` | Remove once PlayerDetailScreen uses `Player` model via GraphQL |

## Out of Scope

- GraphQL migration for PlayerDetailScreen's network layer (prerequisite for full convergence, separate effort)
- Implementation of Heroes and Friends & Enemies tab content
- Changes to navigation structure or nav keys
