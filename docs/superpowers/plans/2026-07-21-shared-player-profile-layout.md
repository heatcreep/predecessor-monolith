# Shared Player Profile Layout Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Extract ProfileScreen's layout (profile card + chip filter tabs + tab content) into a shared composable in `core:ui` so both the current-user profile and any-player detail screen share the same visual structure.

**Architecture:** Move `PlayerProfileCardUiModel`, its mapper, and the `PlayerProfileCard` composable from `feature/profile/impl` to `core/ui`. Create a new `PlayerProfileLayout` composable that composes the card + chip tabs + content slot. Each screen keeps its own ViewModel and Scaffold, delegating the body to the shared layout.

**Tech Stack:** Kotlin, Jetpack Compose, Hilt, Paging 3, Coil 3

## Global Constraints

- Follow existing multimodule conventions: shared UI in `core:ui`, models in `core:ui/model`, composables in `core:ui/cards/` or `core:ui/components/`
- `core:ui` already depends on `core:model` (via `api`) and `core:resources` (via `api`) — no new module dependencies needed
- String resources used by shared composables must live in `core:resources`, not feature modules
- Branch: `feature/shared-player-profile-layout`

---

### Task 1: Move string resources to core:resources

Two string resources currently in `feature/profile/impl` are used by `PlayerProfileCard`. They need to move to `core:resources` so the composable can live in `core:ui`.

**Files:**
- Modify: `core/resources/src/main/res/values/strings.xml`
- Modify: `feature/profile/impl/src/main/res/values/strings.xml`

**Interfaces:**
- Consumes: nothing
- Produces: `core_resources_player_profile_win_percentage` and `core_resources_player_profile_region` string resource IDs available via `com.aowen.predcompanion.core.resources.R`

- [ ] **Step 1: Add string resources to core:resources**

In `core/resources/src/main/res/values/strings.xml`, add these two entries inside `<resources>`:

```xml
<string name="core_resources_player_profile_win_percentage">Winrate: %1$s</string>
<string name="core_resources_player_profile_region">Region: %1$s</string>
```

- [ ] **Step 2: Remove string resources from feature/profile/impl**

In `feature/profile/impl/src/main/res/values/strings.xml`, remove these two lines:

```xml
<string name="feature_profile_impl_win_percentage">Winrate: %1$s</string>
<string name="feature_profile_impl_region">Region: %1$s</string>
```

- [ ] **Step 3: Update ProfileScreen's PlayerProfileCard to use core resources temporarily**

In `feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ui/PlayerProfileCard.kt`, update the two `stringResource` calls that reference the moved strings. Change:

```kotlin
stringResource(
    R.string.feature_profile_impl_region,
    playerProfileCardUiModel.region
)
```

to:

```kotlin
stringResource(
    coreResources.string.core_resources_player_profile_region,
    playerProfileCardUiModel.region
)
```

And change:

```kotlin
stringResource(
    R.string.feature_profile_impl_win_percentage,
    playerProfileCardUiModel.winPercentage
)
```

to:

```kotlin
stringResource(
    coreResources.string.core_resources_player_profile_win_percentage,
    playerProfileCardUiModel.winPercentage
)
```

The `import com.aowen.predcompanion.feature.profile.impl.R` can remain for now (still used by the preview); it will be removed when the file moves in Task 3.

- [ ] **Step 4: Build to verify**

Run: `./gradlew :feature:profile:impl:compileDebugKotlin :core:resources:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 5: Commit**

```bash
git add core/resources/src/main/res/values/strings.xml feature/profile/impl/src/main/res/values/strings.xml feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ui/PlayerProfileCard.kt
git commit -m "move player profile string resources to core:resources"
```

---

### Task 2: Move PlayerProfileCardUiModel and mapper to core:ui

Move the UI model and the `Player.toPlayerProfileCardUiModel()` extension from the feature module to `core:ui/model`.

**Files:**
- Create: `core/ui/src/main/kotlin/com/aowen/predcompanion/core/ui/model/PlayerProfileCardUiModel.kt`
- Modify: `feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ui/PlayerProfileCard.kt` (remove model + mapper, update imports)
- Modify: `feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ProfileScreen.kt` (update import for `toPlayerProfileCardUiModel`)

**Interfaces:**
- Consumes: `com.aowen.predcompanion.core.model.data.Player` from `core:model`
- Produces: `com.aowen.predcompanion.core.ui.model.PlayerProfileCardUiModel` data class and `Player.toPlayerProfileCardUiModel()` extension function

- [ ] **Step 1: Create PlayerProfileCardUiModel in core:ui**

Create `core/ui/src/main/kotlin/com/aowen/predcompanion/core/ui/model/PlayerProfileCardUiModel.kt`:

```kotlin
package com.aowen.predcompanion.core.ui.model

import com.aowen.predcompanion.core.model.data.Player

data class PlayerProfileCardUiModel(
    val rankIconUrl: String,
    val playerName: String,
    val rankPoints: String,
    val rankTitle: String,
    val winPercentage: String,
    val region: String,
    val favoriteHeroIconUrl: String,
)

fun Player.toPlayerProfileCardUiModel(): PlayerProfileCardUiModel {
    return PlayerProfileCardUiModel(
        playerName = name,
        rankIconUrl = rankIconUrl,
        rankPoints = currentRankPoints,
        rankTitle = currentRankTitle,
        winPercentage = winRate,
        region = "NA",
        favoriteHeroIconUrl = favoriteHero?.imageUrl ?: "",
    )
}
```

- [ ] **Step 2: Remove model and mapper from PlayerProfileCard.kt**

In `feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ui/PlayerProfileCard.kt`:

Remove the `PlayerProfileCardUiModel` data class (lines 45-53) and the `Player.toPlayerProfileCardUiModel()` extension function (lines 55-65).

Add this import:

```kotlin
import com.aowen.predcompanion.core.ui.model.PlayerProfileCardUiModel
```

- [ ] **Step 3: Update ProfileScreen.kt import**

In `feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ProfileScreen.kt`:

Change:

```kotlin
import com.aowen.predcompanion.feature.profile.impl.ui.toPlayerProfileCardUiModel
```

to:

```kotlin
import com.aowen.predcompanion.core.ui.model.toPlayerProfileCardUiModel
```

- [ ] **Step 4: Build to verify**

Run: `./gradlew :core:ui:compileDebugKotlin :feature:profile:impl:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 5: Commit**

```bash
git add core/ui/src/main/kotlin/com/aowen/predcompanion/core/ui/model/PlayerProfileCardUiModel.kt feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ui/PlayerProfileCard.kt feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ProfileScreen.kt
git commit -m "move PlayerProfileCardUiModel and mapper to core:ui"
```

---

### Task 3: Move PlayerProfileCard composable to core:ui

Move the composable from `feature/profile/impl/ui/` to `core/ui/cards/playerprofile/`. The existing `PlayerProfileTitleCard.kt` in that directory is an older iteration and will be cleaned up in Task 6.

**Files:**
- Create: `core/ui/src/main/kotlin/com/aowen/predcompanion/core/ui/cards/playerprofile/PlayerProfileCard.kt`
- Delete: `feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ui/PlayerProfileCard.kt`
- Modify: `feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ProfileScreen.kt` (update import)

**Interfaces:**
- Consumes: `PlayerProfileCardUiModel` from Task 2, `PlayerIcon` from `core:ui`, string resources from Task 1
- Produces: `com.aowen.predcompanion.core.ui.cards.playerprofile.PlayerProfileCard` composable

- [ ] **Step 1: Create PlayerProfileCard.kt in core:ui**

Create `core/ui/src/main/kotlin/com/aowen/predcompanion/core/ui/cards/playerprofile/PlayerProfileCard.kt` with the full composable. The key changes from the original: package declaration, import paths (use `core.ui.model.PlayerProfileCardUiModel` and `core.resources.R` for strings), and remove the feature-local `R` import.

```kotlin
package com.aowen.predcompanion.core.ui.cards.playerprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil3.annotation.ExperimentalCoilApi
import coil3.asImage
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.core.ui.common.PlayerIcon
import com.aowen.predcompanion.core.ui.model.PlayerProfileCardUiModel
import com.aowen.predcompanion.core.resources.R as coreResources

@Composable
fun PlayerProfileCard(
    playerProfileCardUiModel: PlayerProfileCardUiModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PlayerIcon(
                heroImageUrl = playerProfileCardUiModel.favoriteHeroIconUrl,
                heroIconSize = 48.dp,
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                Text(
                    text = playerProfileCardUiModel.playerName,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.secondary,
                )
                Row(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = stringResource(
                            coreResources.string.core_resources_player_profile_region,
                            playerProfileCardUiModel.region
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = stringResource(
                            coreResources.string.core_resources_player_profile_win_percentage,
                            playerProfileCardUiModel.winPercentage
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
        Row(
            modifier = Modifier.width(140.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = playerProfileCardUiModel.rankIconUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    alignment = BiasAlignment(
                        horizontalBias = 0f,
                        verticalBias = -0.25f
                    ),
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = playerProfileCardUiModel.rankPoints,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Text(
                        text = playerProfileCardUiModel.rankTitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@PreviewLightDark
@Composable
fun PlayerProfileCardPreview() {
    val context = LocalContext.current
    val heroIconUrl = "https://example.com/hero.png"
    val rankIconUrl = "https://example.com/rank.png"
    val previewHandler = remember {
        AsyncImagePreviewHandler { request ->
            val drawableRes = when (request.data) {
                heroIconUrl -> coreResources.drawable.narbash
                rankIconUrl -> coreResources.drawable.gold
                else -> coreResources.drawable.unknown
            }
            ContextCompat.getDrawable(context, drawableRes)!!.asImage()
        }
    }

    MonolithTheme {
        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
            PlayerProfileCard(
                playerProfileCardUiModel = PlayerProfileCardUiModel(
                    favoriteHeroIconUrl = heroIconUrl,
                    playerName = "heatcreep.tv",
                    rankIconUrl = rankIconUrl,
                    winPercentage = "49.7%",
                    region = "NA",
                    rankPoints = "648",
                    rankTitle = "Gold III"
                )
            )
        }
    }
}
```

- [ ] **Step 2: Delete the old PlayerProfileCard.kt**

Delete `feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ui/PlayerProfileCard.kt`.

- [ ] **Step 3: Update ProfileScreen.kt import**

In `feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ProfileScreen.kt`:

Change:

```kotlin
import com.aowen.predcompanion.feature.profile.impl.ui.PlayerProfileCard
```

to:

```kotlin
import com.aowen.predcompanion.core.ui.cards.playerprofile.PlayerProfileCard
```

- [ ] **Step 4: Build to verify**

Run: `./gradlew :core:ui:compileDebugKotlin :feature:profile:impl:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 5: Commit**

```bash
git add core/ui/src/main/kotlin/com/aowen/predcompanion/core/ui/cards/playerprofile/PlayerProfileCard.kt feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ProfileScreen.kt
git rm feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ui/PlayerProfileCard.kt
git commit -m "move PlayerProfileCard composable to core:ui"
```

---

### Task 4: Create PlayerProfileLayout shared composable

Create the shared layout composable that combines the profile card, chip filter tabs, and a content slot.

**Files:**
- Create: `core/ui/src/main/kotlin/com/aowen/predcompanion/core/ui/cards/playerprofile/PlayerProfileLayout.kt`

**Interfaces:**
- Consumes: `PlayerProfileCardUiModel` from Task 2, `PlayerProfileCard` from Task 3, `PredCompanionChipFilter` from `core:ui/filters`
- Produces: `com.aowen.predcompanion.core.ui.cards.playerprofile.PlayerProfileLayout` composable

- [ ] **Step 1: Create PlayerProfileLayout.kt**

Create `core/ui/src/main/kotlin/com/aowen/predcompanion/core/ui/cards/playerprofile/PlayerProfileLayout.kt`:

```kotlin
package com.aowen.predcompanion.core.ui.cards.playerprofile

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aowen.predcompanion.core.ui.filters.PredCompanionChipFilter
import com.aowen.predcompanion.core.ui.model.PlayerProfileCardUiModel

@Composable
fun PlayerProfileLayout(
    profileCard: PlayerProfileCardUiModel,
    tabLabels: List<@StringRes Int>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (selectedTab: Int) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PlayerProfileCard(
            playerProfileCardUiModel = profileCard,
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(tabLabels) { index, labelRes ->
                PredCompanionChipFilter(
                    text = stringResource(labelRes),
                    selected = selectedTab == index,
                    onClick = { onTabSelected(index) }
                )
            }
        }
        content(selectedTab)
    }
}
```

- [ ] **Step 2: Build to verify**

Run: `./gradlew :core:ui:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add core/ui/src/main/kotlin/com/aowen/predcompanion/core/ui/cards/playerprofile/PlayerProfileLayout.kt
git commit -m "add PlayerProfileLayout shared composable"
```

---

### Task 5: Update ProfileScreen to use PlayerProfileLayout

Replace the inline card + tabs + content code in `ProfileScreen` with the shared `PlayerProfileLayout`.

**Files:**
- Modify: `feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ProfileScreen.kt`

**Interfaces:**
- Consumes: `PlayerProfileLayout` from Task 4, `PlayerProfileCardUiModel` and `toPlayerProfileCardUiModel` from Task 2
- Produces: Updated `ProfileScreen` composable using shared layout

- [ ] **Step 1: Update ProfileScreen composable**

In `feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ProfileScreen.kt`, replace the imports:

Remove:

```kotlin
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import com.aowen.predcompanion.core.ui.filters.PredCompanionChipFilter
import com.aowen.predcompanion.core.ui.cards.playerprofile.PlayerProfileCard
```

Add:

```kotlin
import com.aowen.predcompanion.core.ui.cards.playerprofile.PlayerProfileLayout
```

- [ ] **Step 2: Replace the UserInfoLoaded branch content**

In the `is UserUiState.UserInfoLoaded` branch (where `userState.userInfo != null`), replace the existing `Column` containing `PlayerProfileCard`, `LazyRow` of chip filters, and `when(selectedTab)` block with:

```kotlin
PlayerProfileLayout(
    profileCard = userState.userInfo.players.first()
        .toPlayerProfileCardUiModel(),
    tabLabels = tabList,
    selectedTab = selectedTab,
    onTabSelected = { selectedTab = it },
) { tab ->
    when (tab) {
        0 -> {
            LazyColumn(
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(matches.itemCount) {
                    matches[it]?.let { matchItem ->
                        MatchPlayerCard(
                            matchListItem = matchItem,
                        )
                    }
                }
            }
        }
        1 -> {}
        2 -> {}
    }
}
```

The `tabList`, `selectedTab`, `LazyColumn`, `rememberLazyListState`, `MatchPlayerCard`, and the match paging remain unchanged — only the wrapping structure changes.

- [ ] **Step 3: Build to verify**

Run: `./gradlew :feature:profile:impl:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Run existing tests**

Run: `./gradlew :feature:profile:impl:testDebugUnitTest`
Expected: All tests pass (ProfileViewModelTest is ViewModel-only, unaffected by composable changes)

- [ ] **Step 5: Commit**

```bash
git add feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ProfileScreen.kt
git commit -m "update ProfileScreen to use shared PlayerProfileLayout"
```

---

### Task 6: Clean up superseded files

Remove older iterations and unused mappers that are superseded by the shared composables.

**Files:**
- Delete: `core/ui/src/main/kotlin/com/aowen/predcompanion/core/ui/cards/playerprofile/PlayerProfileTitleCard.kt`

**Interfaces:**
- Consumes: nothing
- Produces: nothing (cleanup only)

- [ ] **Step 1: Check for usages of PlayerProfileTitleCard**

Run: `grep -r "PlayerProfileTitleCard" --include="*.kt" .`
Expected: Only the file's own definition and its preview. If any other file imports it, update that file first.

- [ ] **Step 2: Delete PlayerProfileTitleCard.kt**

```bash
git rm core/ui/src/main/kotlin/com/aowen/predcompanion/core/ui/cards/playerprofile/PlayerProfileTitleCard.kt
```

- [ ] **Step 3: Build to verify no breakage**

Run: `./gradlew compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Commit**

```bash
git commit -m "remove superseded PlayerProfileTitleCard"
```

---

### Task 7: Update PlayerDetailScreen to use shared layout

Replace the `TabRow` + `HorizontalPager` + `PlayerProfilePlayerStatsCard` structure in `PlayerDetailScreen` with the shared `PlayerProfileLayout`. The Scaffold (top bar with back arrow + claim/unclaim heart, unclaim dialog, pull-to-refresh) stays in `PlayerDetailScreen`. Since PlayerDetailScreen still uses `PlayerInfo.PlayerDetails` (pre-GraphQL migration), build the `PlayerProfileCardUiModel` inline from the existing data. The `PlayerHeroStatsTab` content moves into the "Heroes" chip tab. `PlayerStatsTab` is removed as a composable — its match list content goes into the "Matches" chip tab.

**Files:**
- Modify: `feature/home/impl/src/main/kotlin/com/aowen/predcompanion/feature/home/impl/playerdetails/PlayerDetailScreen.kt`
- Modify: `core/resources/src/main/res/values/strings.xml`
- Modify: `feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ProfileScreen.kt`
- Modify: `feature/profile/impl/src/main/res/values/strings.xml`

**Interfaces:**
- Consumes: `PlayerProfileLayout` from Task 4, `PlayerProfileCardUiModel` from Task 2, `PlayerInfo.PlayerDetails` and `PlayerInfo.PlayerStats` from `core:model`
- Produces: Updated `PlayerDetailScreen` composable using shared layout

- [ ] **Step 1: Add tab string resources to core:resources**

In `core/resources/src/main/res/values/strings.xml`, add:

```xml
<string name="core_resources_player_profile_tab_matches">Matches</string>
<string name="core_resources_player_profile_tab_heroes">Heroes</string>
<string name="core_resources_player_profile_tab_friends_enemies"><![CDATA[Friends & Enemies]]></string>
```

- [ ] **Step 2: Update ProfileScreen to use shared tab string resources**

In `feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ProfileScreen.kt`, change:

```kotlin
val tabList = listOf(
    R.string.feature_profile_impl_nav_matches,
    R.string.feature_profile_impl_nav_heroes,
    R.string.feature_profile_impl_nav_friends_enemies
)
```

to:

```kotlin
val tabList = listOf(
    coreResources.string.core_resources_player_profile_tab_matches,
    coreResources.string.core_resources_player_profile_tab_heroes,
    coreResources.string.core_resources_player_profile_tab_friends_enemies,
)
```

Remove the now-unused tab strings from `feature/profile/impl/src/main/res/values/strings.xml`:

```xml
<string name="feature_profile_impl_nav_matches">Matches</string>
<string name="feature_profile_impl_nav_heroes">Heroes</string>
<string name="feature_profile_impl_nav_friends_enemies"><![CDATA[Friends & Enemies]]></string>
```

- [ ] **Step 3: Rewrite the Loaded branch in PlayerDetailScreen**

In `feature/home/impl/src/main/kotlin/com/aowen/predcompanion/feature/home/impl/playerdetails/PlayerDetailScreen.kt`:

Add imports:

```kotlin
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.aowen.predcompanion.core.ui.cards.playerprofile.PlayerProfileLayout
import com.aowen.predcompanion.core.ui.model.PlayerProfileCardUiModel
import com.aowen.predcompanion.core.resources.R as coreResources
```

In the `is PlayerDetailsState.Loaded` branch, replace the `TabRow` + `HorizontalPager` block (the entire `Column` inside `PullToRefreshBox`) with:

```kotlin
Column(
    modifier = Modifier
        .fillMaxHeight()
        .padding(horizontal = 16.dp)
) {
    val tabLabels = listOf(
        coreResources.string.core_resources_player_profile_tab_matches,
        coreResources.string.core_resources_player_profile_tab_heroes,
        coreResources.string.core_resources_player_profile_tab_friends_enemies,
    )
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val profileCard = PlayerProfileCardUiModel(
        playerName = uiState.player?.playerName ?: "",
        rankIconUrl = uiState.player?.rankImage ?: "",
        rankPoints = "+${uiState.player?.vpCurrent ?: 0} VP",
        rankTitle = uiState.player?.rankTitle ?: "",
        winPercentage = uiState.stats?.winRate ?: "0%",
        region = uiState.player?.region ?: "N/A",
        favoriteHeroIconUrl = uiState.stats?.favoriteHero?.imageUrl ?: "",
    )

    PlayerProfileLayout(
        profileCard = profileCard,
        tabLabels = tabLabels,
        selectedTab = selectedTab,
        onTabSelected = { selectedTab = it },
    ) { tab ->
        when (tab) {
            0 -> {
                PlayerDetailsRecentMatchList(
                    modifier = Modifier.fillMaxSize(),
                    playerId = uiState.playerId,
                    matches = uiState.matches,
                    navigateToMoreMatches = navigateToMoreMatches,
                    navigateToMatchDetails = navigateToMatchDetails
                )
            }
            1 -> {
                PlayerHeroStatsTab(
                    uiState = uiState,
                    handlePlayerHeroStatsSelect = handlePlayerHeroStatsSelect
                )
            }
            2 -> {}
        }
    }
}
```

This removes the `TabRow`, `HorizontalPager`, and the `PlayerStatsTab` wrapper entirely. The `PlayerHeroStatsTab` composable is preserved and rendered inline in the "Heroes" chip tab.

- [ ] **Step 4: Remove the PlayerStatsTab composable**

Delete the `PlayerStatsTab` composable function from `PlayerDetailScreen.kt`. Its match list content is now inline in the "Matches" chip tab above. Also remove unused parameters from `PlayerDetailScreen` composable signature: `handleSavePlayerName`, `handlePlayerNameChange`, `onEditPlayerName` (these were only used by `PlayerStatsTab`).

Update `PlayerDetailsRoute` to stop passing those removed parameters.

- [ ] **Step 5: Build to verify**

Run: `./gradlew :core:ui:compileDebugKotlin :feature:home:impl:compileDebugKotlin :feature:profile:impl:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 6: Run existing tests**

Run: `./gradlew :feature:home:impl:testDebugUnitTest :feature:profile:impl:testDebugUnitTest`
Expected: All tests pass

- [ ] **Step 7: Commit**

```bash
git add core/resources/src/main/res/values/strings.xml feature/home/impl/src/main/kotlin/com/aowen/predcompanion/feature/home/impl/playerdetails/PlayerDetailScreen.kt feature/profile/impl/src/main/kotlin/com/aowen/predcompanion/feature/profile/impl/ProfileScreen.kt feature/profile/impl/src/main/res/values/strings.xml
git commit -m "update PlayerDetailScreen to use shared PlayerProfileLayout"
```

---

### Task 8: Clean up PlayerDetailScreen superseded files

Remove `PlayerProfilePlayerStatsCard` now that PlayerDetailScreen uses the shared layout.

**Files:**
- Delete: `feature/home/impl/src/main/kotlin/com/aowen/predcompanion/feature/home/impl/playerdetails/PlayerProfilePlayerStatsCard.kt`

**Interfaces:**
- Consumes: nothing
- Produces: nothing (cleanup only)

- [ ] **Step 1: Check for usages**

Run: `grep -r "PlayerProfilePlayerStatsCard" --include="*.kt" .`
Expected: Only the file's definition and preview. If it's still referenced from `PlayerDetailScreen.kt` after Task 7 changes, the reference was missed — go back and fix it.

- [ ] **Step 2: Delete the file**

```bash
git rm feature/home/impl/src/main/kotlin/com/aowen/predcompanion/feature/home/impl/playerdetails/PlayerProfilePlayerStatsCard.kt
```

- [ ] **Step 3: Build full project**

Run: `./gradlew compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Run all tests**

Run: `./gradlew testDebugUnitTest`
Expected: All tests pass

- [ ] **Step 5: Commit**

```bash
git commit -m "remove superseded PlayerProfilePlayerStatsCard"
```
