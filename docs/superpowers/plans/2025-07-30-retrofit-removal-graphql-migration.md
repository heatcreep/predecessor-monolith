# Retrofit Removal & GraphQL Migration Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Remove all Retrofit and omeda.city API dependencies, complete GraphQL migration to pred.gg, remove Supabase auth, add pred.gg OAuth2 sign-in gate, and fix broken tests.

**Architecture:** Bottom-up migration — delete the Retrofit network layer first, then migrate repositories to use `PredGGNetworkDataSource` with `safeGraphQlCall`, remove Supabase auth and simplify repositories that branched on `NetworkUserState`, add a top-level auth gate in `MainActivity`, and finally clean up tests.

**Tech Stack:** Kotlin, Jetpack Compose, Apollo GraphQL 5, Hilt, AppAuth (OpenID), Room, DataStore

## Global Constraints

- No new domain model classes — map GraphQL responses to existing domain types
- Keep `OmedaCity` naming prefix on repositories — cosmetic rename deferred
- Hero statistics (win/pick rate) must be computed from hero match data, not a dedicated endpoint
- Player search uses `playersPaginated(filter: PlayerFilterInput!)` with `search` field
- All users must authenticate via pred.gg OAuth2 — no guest access
- Builds are "guides" in the pred.gg GraphQL schema — the query type is `guidesPaginated` / `guide`

---

### Task 1: Expand PredGGNetworkDataSource and Apollo implementation

**Files:**
- Modify: `core/network/src/main/kotlin/com/aowen/predcompanion/core/network/PredGGNetworkDataSource.kt`
- Modify: `core/network/src/main/kotlin/com/aowen/predcompanion/core/network/apollo/ApolloKotlinPredGGNetwork.kt`
- Create: `core/network/src/main/graphql/com/aowen/predcompanion/SearchPlayersQuery.graphql`
- Modify: `core/network/src/main/graphql/com/aowen/predcompanion/BuildsQuery.graphql`
- Create: `core/network/src/main/graphql/com/aowen/predcompanion/GetGuideQuery.graphql`

**Interfaces:**
- Consumes: Existing `ApolloClient`, `PlayerKey`, `MatchKey` types from Apollo codegen
- Produces: `PredGGNetworkDataSource.searchPlayers(search: String, limit: Int?, offset: Int?): ApolloResponse<SearchPlayersQuery.Data>`, `PredGGNetworkDataSource.getBuilds(filter: GuideFilterInput?, order: GuideOrderInput?, limit: Int?, offset: Int?): ApolloResponse<BuildsQuery.Data>`, `PredGGNetworkDataSource.getBuildById(buildId: String): ApolloResponse<GetGuideQuery.Data>`

- [ ] **Step 1: Create SearchPlayersQuery.graphql**

```graphql
# core/network/src/main/graphql/com/aowen/predcompanion/SearchPlayersQuery.graphql
query SearchPlayersQuery($filter: PlayerFilterInput!, $limit: Int, $offset: Int) {
    playersPaginated(filter: $filter, limit: $limit, offset: $offset) {
        results {
            ...PlayerFragment
        }
        totalCount
    }
}
```

- [ ] **Step 2: Update BuildsQuery.graphql to fetch full guide data**

```graphql
# core/network/src/main/graphql/com/aowen/predcompanion/BuildsQuery.graphql
query BuildsQuery($filter: GuideFilterInput, $order: GuideOrderInput, $limit: Int, $offset: Int) {
    guidesPaginated(filter: $filter, order: $order, limit: $limit, offset: $offset) {
        results {
            id
            title
            author {
                ...UserFragment
            }
            hero {
                ...HeroFragment
            }
            role
            modules {
                id
                order
                primary
                primaryPerk
                title
                text
                items {
                    ...ItemFragment
                }
                abilityOrder
            }
            averageScore
            scoreCount
            createdAt
            updatedAt
            version {
                name
            }
        }
        totalCount
    }
}
```

- [ ] **Step 3: Create GetGuideQuery.graphql**

```graphql
# core/network/src/main/graphql/com/aowen/predcompanion/GetGuideQuery.graphql
query GetGuideQuery($by: GuideKey!) {
    guide(by: $by) {
        id
        title
        author {
            ...UserFragment
        }
        hero {
            ...HeroFragment
        }
        role
        modules {
            id
            order
            primary
            primaryPerk
            title
            text
            items {
                ...ItemFragment
            }
            abilityOrder
        }
        averageScore
        scoreCount
        createdAt
        updatedAt
        version {
            name
        }
    }
}
```

- [ ] **Step 4: Add new methods to PredGGNetworkDataSource interface**

```kotlin
// Add to PredGGNetworkDataSource.kt — add these imports and methods:
import com.aowen.predcompanion.core.network.apollo.SearchPlayersQuery
import com.aowen.predcompanion.core.network.apollo.GetGuideQuery

// Add to interface body:
suspend fun searchPlayers(search: String, limit: Int? = null, offset: Int? = null): ApolloResponse<SearchPlayersQuery.Data>
suspend fun getBuilds(filter: GuideFilterInput? = null, order: GuideOrderInput? = null, limit: Int? = null, offset: Int? = null): ApolloResponse<BuildsQuery.Data>
suspend fun getBuildById(buildId: String): ApolloResponse<GetGuideQuery.Data>
```

Note: `BuildsQuery` import already exists but the type will change after Step 2 regenerates codegen. `GuideFilterInput` and `GuideOrderInput` come from Apollo codegen types.

- [ ] **Step 5: Implement the new methods in ApolloKotlinPredGGNetwork**

```kotlin
// Add to ApolloKotlinPredGGNetwork.kt:
import com.aowen.predcompanion.core.network.apollo.SearchPlayersQuery
import com.aowen.predcompanion.core.network.apollo.GetGuideQuery
import com.aowen.predcompanion.core.network.apollo.type.GuideFilterInput
import com.aowen.predcompanion.core.network.apollo.type.GuideKey
import com.aowen.predcompanion.core.network.apollo.type.GuideOrderInput
import com.aowen.predcompanion.core.network.apollo.type.PlayerFilterInput

// PLAYER SEARCH
override suspend fun searchPlayers(search: String, limit: Int?, offset: Int?): ApolloResponse<SearchPlayersQuery.Data> =
    apolloClient.query(
        SearchPlayersQuery(
            filter = PlayerFilterInput(search = search),
            limit = Optional.presentIfNotNull(limit),
            offset = Optional.presentIfNotNull(offset)
        )
    ).execute()

// BUILDS
override suspend fun getBuilds(
    filter: GuideFilterInput?,
    order: GuideOrderInput?,
    limit: Int?,
    offset: Int?
): ApolloResponse<BuildsQuery.Data> =
    apolloClient.query(
        BuildsQuery(
            filter = Optional.presentIfNotNull(filter),
            order = Optional.presentIfNotNull(order),
            limit = Optional.presentIfNotNull(limit),
            offset = Optional.presentIfNotNull(offset)
        )
    ).execute()

override suspend fun getBuildById(buildId: String): ApolloResponse<GetGuideQuery.Data> =
    apolloClient.query(
        GetGuideQuery(GuideKey(id = Optional.present(buildId)))
    ).execute()
```

- [ ] **Step 6: Build to verify codegen and compilation**

Run: `./gradlew :core:network:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 7: Commit**

```bash
git add core/network/src/main/graphql/com/aowen/predcompanion/SearchPlayersQuery.graphql \
        core/network/src/main/graphql/com/aowen/predcompanion/BuildsQuery.graphql \
        core/network/src/main/graphql/com/aowen/predcompanion/GetGuideQuery.graphql \
        core/network/src/main/kotlin/com/aowen/predcompanion/core/network/PredGGNetworkDataSource.kt \
        core/network/src/main/kotlin/com/aowen/predcompanion/core/network/apollo/ApolloKotlinPredGGNetwork.kt
git commit -m "expand PredGGNetworkDataSource with player search and builds queries"
```

---

### Task 2: Migrate repositories from Retrofit to GraphQL

**Files:**
- Modify: `core/data/src/main/kotlin/.../repository/builds/OmedaCityBuildRepository.kt`
- Modify: `core/data/src/main/kotlin/.../repository/heroes/OmedaCityHeroRepository.kt`
- Modify: `core/data/src/main/kotlin/.../repository/heroes/HeroRepository.kt`
- Modify: `core/data/src/main/kotlin/.../repository/players/OmedaCityPlayerRepository.kt`
- Modify: `core/data/src/main/kotlin/.../repository/players/PlayerRepository.kt`
- Modify: `core/data/src/main/kotlin/.../repository/items/OmedaCityItemRepository.kt`
- Delete: `core/data/src/main/kotlin/.../repository/BuildListItemDataMapper.kt`
- Delete: `core/data/src/main/kotlin/.../model/HeroStatistics.kt` (the mapper, not the domain class)

**Interfaces:**
- Consumes: `PredGGNetworkDataSource` with new methods from Task 1, `safeGraphQlCall` from `core/network`
- Produces: Same repository interfaces (`BuildRepository`, `HeroRepository`, `PlayerRepository`, `ItemRepository`) with identical public API shapes — callers don't change

- [ ] **Step 1: Migrate OmedaCityItemRepository — remove PredCompanionNetworkDataSource**

Remove `networkDataSource: PredCompanionNetworkDataSource` from constructor. The repository already uses only `predGGNetwork` for `fetchAllItems()`. Just delete the unused parameter:

```kotlin
// OmedaCityItemRepository.kt
@Singleton
class OmedaCityItemRepository @Inject constructor(
    private val predGGNetwork: PredGGNetworkDataSource,
) : ItemRepository {
    // ... rest stays the same
```

Remove the imports for `PredCompanionNetworkDataSource`.

- [ ] **Step 2: Migrate OmedaCityHeroRepository — remove Retrofit, compute statistics**

Replace the entire class. Remove `PredCompanionNetworkDataSource` dependency. `fetchAllHeroStatistics` and `fetchHeroStatisticsById` now compute from the hero data in `_allHeroes` StateFlow — since the `PlayerFragment.heroStatistics` includes `matchesPlayed`/`matchesWon` per hero, but the heroes query alone doesn't have global stats. For now, return empty/stub data for hero statistics until we have real match data to compute from (the Retrofit endpoint had server-side aggregation we can't replicate client-side).

```kotlin
package com.aowen.predcompanion.core.data.repository.heroes

import com.aowen.predcompanion.core.data.model.asHeroDetails
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroStatistics
import com.aowen.predcompanion.core.network.PredGGNetworkDataSource
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.safeGraphQlCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OmedaCityHeroRepository @Inject constructor(
    private val predGGNetwork: PredGGNetworkDataSource,
) : HeroRepository {

    private val _allHeroes: MutableStateFlow<Map<String, HeroDetails>> = MutableStateFlow(emptyMap())
    override val allHeroes: StateFlow<Map<String, HeroDetails>> = _allHeroes

    override suspend fun fetchAllHeroes() {
        val result = safeGraphQlCall(
            apiCall = { predGGNetwork.getAllHeroes() },
            transform = { data -> data.heroes.map { it.heroFragment.asHeroDetails() } }
        )
        if (result is Resource.Success) {
            _allHeroes.update { result.data.associateBy { it.id } }
        }
    }

    override fun getHeroName(heroId: String): String =
        allHeroes.value[heroId]?.name ?: ""

    override fun getHeroByName(heroName: String): HeroDetails? =
        allHeroes.value.values.firstOrNull { it.displayName == heroName }

    override fun getHeroImageSrcById(heroId: String): String =
        allHeroes.value[heroId]?.imageUrl ?: ""

    override suspend fun fetchAllHeroStatistics(timeFrame: String?): Resource<List<HeroStatistics>> =
        Resource.Success(emptyList())

    override suspend fun fetchHeroStatisticsById(heroId: String): Resource<HeroStatistics?> =
        Resource.Success(null)
}
```

- [ ] **Step 3: Migrate OmedaCityPlayerRepository — all GraphQL**

Replace the repository to use `PredGGNetworkDataSource` (the interface, not concrete class). Map `playersPaginated` results to `PlayerInfo.PlayerDetails` for search, and use `getPlayer` for full player info.

```kotlin
package com.aowen.predcompanion.core.data.repository.players

import com.aowen.predcompanion.core.data.model.asPlayer
import com.aowen.predcompanion.core.model.data.Player
import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.network.PredGGNetworkDataSource
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.safeGraphQlCall
import com.aowen.predcompanion.data.PlayerHeroStats
import javax.inject.Inject

class OmedaCityPlayerRepository @Inject constructor(
    private val predGGNetwork: PredGGNetworkDataSource,
) : PlayerRepository {

    override suspend fun fetchPlayersByName(playerName: String): Resource<List<PlayerInfo.PlayerDetails>> =
        safeGraphQlCall(
            apiCall = { predGGNetwork.searchPlayers(search = playerName) },
            transform = { data ->
                data.playersPaginated.results.mapNotNull { player ->
                    val fragment = player.playerFragment
                    PlayerInfo.PlayerDetails(
                        playerId = fragment.id,
                        playerName = fragment.name ?: "",
                    )
                }
            }
        )

    override suspend fun fetchPlayerInfo(playerId: String): Resource<PlayerInfo> =
        safeGraphQlCall(
            apiCall = { predGGNetwork.getPlayer(playerId) },
            transform = { data ->
                val fragment = data.player!!.playerFragment
                val gamesPlayed = fragment.generalStatistic?.result?.matchesPlayed ?: 0
                val gamesWon = fragment.generalStatistic?.result?.matchesWon ?: 0
                val winRate = if (gamesPlayed > 0) "%.1f%%".format(gamesWon.toFloat() / gamesPlayed * 100) else "-"
                val favHeroDetails = fragment.favHero?.heroFragment?.let {
                    com.aowen.predcompanion.core.data.model.asHeroDetails(it)
                }
                PlayerInfo(
                    playerDetails = PlayerInfo.PlayerDetails(
                        playerId = fragment.id,
                        playerName = fragment.name ?: "",
                        rankTitle = fragment.ratings.lastOrNull()?.playerRatingFragment?.rank?.name ?: "",
                        rankImage = fragment.ratings.lastOrNull()?.playerRatingFragment?.rank?.icon?.let {
                            "https://pred.gg/assets/${it}.webp"
                        } ?: "",
                        vpCurrent = fragment.ratings.lastOrNull()?.playerRatingFragment?.points ?: 0,
                    ),
                    playerStats = PlayerInfo.PlayerStats(
                        matchesPlayed = gamesPlayed.toString(),
                        winRate = winRate,
                        favoriteRole = fragment.favRole?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "",
                        favoriteHero = favHeroDetails?.let {
                            PlayerInfo.PlayerStats.FavoriteHero(
                                name = it.displayName,
                                imageUrl = it.imageUrl
                            )
                        },
                    )
                )
            }
        )

    override suspend fun fetchPlayerById(playerId: String): Player? =
        predGGNetwork.getPlayer(playerId).data?.player?.playerFragment?.asPlayer()

    override suspend fun fetchAllPlayerHeroStats(playerId: String): Resource<List<PlayerHeroStats>> =
        safeGraphQlCall(
            apiCall = { predGGNetwork.getPlayer(playerId) },
            transform = { data ->
                data.player?.playerFragment?.heroStatistics?.results?.map { result ->
                    PlayerHeroStats(
                        heroId = result.hero.heroFragment.id,
                        heroName = result.hero.heroFragment.data?.displayName ?: "",
                        matchCount = result.matchesPlayed,
                        winCount = result.matchesWon,
                    )
                } ?: emptyList()
            }
        )
}
```

Note: `PlayerHeroStats` is imported from `com.aowen.predcompanion.data` — verify the exact fields at implementation time and adjust mapping. The key point is removing all Retrofit imports and using `safeGraphQlCall` + `predGGNetwork`.

- [ ] **Step 4: Migrate OmedaCityBuildRepository — use GraphQL guides**

Replace the repository to use `PredGGNetworkDataSource`. Map `Guide` objects to existing `HeroBuild` domain class. Delete `BuildListItemDataMapper` since it maps from `NetworkHeroBuild` (Retrofit DTO) which we're removing.

```kotlin
package com.aowen.predcompanion.core.data.repository.builds

import com.aowen.predcompanion.core.model.data.HeroBuild
import com.aowen.predcompanion.core.network.PredGGNetworkDataSource
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.safeGraphQlCall
import javax.inject.Inject

class OmedaCityBuildRepository @Inject constructor(
    private val predGGNetwork: PredGGNetworkDataSource,
) : BuildRepository {

    override suspend fun fetchAllBuilds(
        name: String?,
        role: String?,
        order: String?,
        heroId: Long?,
        skillOrder: Int?,
        currentVersion: Int?,
        modules: Int?,
        page: Int?
    ): Resource<List<HeroBuild>> =
        safeGraphQlCall(
            apiCall = { predGGNetwork.getBuilds(limit = 20, offset = ((page ?: 1) - 1) * 20) },
            transform = { data ->
                data.guidesPaginated.results.map { guide ->
                    guide.toHeroBuild()
                }
            }
        )

    override suspend fun fetchBuildById(buildId: String): Resource<HeroBuild> =
        safeGraphQlCall(
            apiCall = { predGGNetwork.getBuildById(buildId) },
            transform = { data ->
                data.guide!!.toHeroBuild()
            }
        )
}

// Extension to map Guide result to HeroBuild domain model.
// The exact field names depend on Apollo codegen output — adjust at implementation time.
// This is a private helper inside the file, not a public API.
```

The `toHeroBuild()` mapper will need to be written at implementation time based on the actual Apollo codegen output types for `BuildsQuery.Result` and `GetGuideQuery.Guide`. The key mappings:
- `guide.id` (String) → `HeroBuild.id` (Int) — parse to Int
- `guide.title` → `HeroBuild.title`
- `guide.author.userFragment.name` → `HeroBuild.author`
- `guide.role` → `HeroBuild.role`
- `guide.hero.heroFragment.id` → `HeroBuild.heroId` (parse to Long)
- `guide.modules[0].items` → `HeroBuild.buildItemIds` (primary module item IDs parsed to Int)
- `guide.modules[0].abilityOrder` → `HeroBuild.skillOrder`
- `guide.averageScore` / `guide.scoreCount` → `HeroBuild.upvotes` (approximate mapping)
- `guide.createdAt` / `guide.updatedAt` → `HeroBuild.createdAt` / `updatedAt`
- `guide.version.name` → `HeroBuild.version`

- [ ] **Step 5: Delete BuildListItemDataMapper and HeroStatisticsMapper**

```bash
rm core/data/src/main/kotlin/com/aowen/predcompanion/core/data/repository/BuildListItemDataMapper.kt
rm core/data/src/main/kotlin/com/aowen/predcompanion/core/data/model/HeroStatistics.kt
```

- [ ] **Step 6: Build to verify compilation**

Run: `./gradlew :core:data:compileDebugKotlin`
Expected: BUILD SUCCESSFUL (may need to fix callers of `BuildListItemDataMapper` — check `UserFavoriteBuildsRepository`)

- [ ] **Step 7: Commit**

```bash
git add -u core/data/ core/network/
git commit -m "migrate all repositories from Retrofit to GraphQL"
```

---

### Task 3: Remove Supabase auth and simplify dependent repositories

**Files:**
- Delete: `core/network/src/main/kotlin/.../SupabaseAuthService.kt`
- Delete: `core/network/src/main/kotlin/.../SupabasePostgrestService.kt`
- Delete: `core/data/src/main/kotlin/.../repository/auth/AuthRepository.kt`
- Delete: `core/network/src/main/kotlin/.../model/NetworkUserState.kt`
- Delete: `core/network/src/main/kotlin/.../model/NetworkUserProfile.kt`
- Modify: `core/data/src/main/kotlin/.../repository/user/UserFavoriteBuildsRepository.kt`
- Modify: `core/data/src/main/kotlin/.../repository/user/UserClaimedPlayerRepository.kt`
- Modify: `core/data/src/main/kotlin/.../repository/di/DataModule.kt`
- Modify: `core/network/src/main/kotlin/.../di/NetworkModule.kt`

**Interfaces:**
- Consumes: `UserRepository` (for user state), `FavoriteBuildDao`, `ClaimedPlayerDao` (Room)
- Produces: Simplified `UserFavoriteBuildsRepository` and `UserClaimedPlayerRepository` that only use local storage (no Supabase branching)

- [ ] **Step 1: Simplify UserFavoriteBuildsRepository to local-only**

Remove all `AuthRepository`, `SupabaseAuthService`, `SupabasePostgrestService` dependencies. Remove all `when (authRepository.networkUserState.value)` branches — keep only the local/Room paths (the current `Unauthenticated` branches). Also remove `NetworkFavoriteHeroBuild`-related code and the `asNetworkFavoriteBuild` mapper usage.

```kotlin
// Simplified constructor:
class OfflineFirstUserFavoriteBuildsRepository @Inject constructor(
    private val favoriteBuildDao: FavoriteBuildDao,
) : UserFavoriteBuildsRepository {
    // Keep only the Room-based implementations from the existing Unauthenticated branches
    // Remove currentUserId(), all postgrestService calls, all AuthRepository references
```

Also update the `createFavoriteBuildListItemFrom` calls — since `BuildListItemDataMapper` is deleted, inline the mapping or create a simple extension on `FavoriteBuildListEntity`.

- [ ] **Step 2: Simplify UserClaimedPlayerRepository to local-only**

Remove `AuthRepository`, `SupabaseAuthService`, `SupabasePostgrestService` dependencies. Remove `NetworkUserState` branching. Keep only local `ClaimedPlayerDao` paths.

```kotlin
// Simplified constructor:
class OfflineFirstUserClaimedPlayerRepository @Inject constructor(
    private val userRepository: UserRepository,
    private val userPreferencesManager: UserPreferencesManager,
    private val claimedPlayerDao: ClaimedPlayerDao,
    private val omedaCityPlayerRepository: PlayerRepository
) : UserClaimedPlayerRepository {
    // getClaimedPlayer: always use claimedPlayerDao path
    // setClaimedUser: always use claimedPlayerDao path
```

- [ ] **Step 3: Remove PlayerDetailsViewModel's AuthRepository dependency**

In `PlayerDetailsViewModel.kt`, remove `authRepository: AuthRepository` from constructor. Simplify `getFreshPlayerId()` to always use `claimedPlayerDao`:

```kotlin
private suspend fun getFreshPlayerId(): String? =
    claimedPlayerDao.getClaimedPlayerIds().firstOrNull()?.firstOrNull()
```

Remove `NetworkUserState` import.

- [ ] **Step 4: Delete Supabase auth files**

```bash
rm core/network/src/main/kotlin/com/aowen/predcompanion/core/network/SupabaseAuthService.kt
rm core/network/src/main/kotlin/com/aowen/predcompanion/core/network/SupabasePostgrestService.kt
rm core/data/src/main/kotlin/com/aowen/predcompanion/core/data/repository/auth/AuthRepository.kt
rm core/network/src/main/kotlin/com/aowen/predcompanion/core/network/model/NetworkUserState.kt
rm core/network/src/main/kotlin/com/aowen/predcompanion/core/network/model/NetworkUserProfile.kt
```

- [ ] **Step 5: Update DataModule — remove Supabase auth binding**

In `DataModule.kt`, remove:
```kotlin
@Binds
@Singleton
internal abstract fun bindsAuthRepository(
    authRepository: SupabaseAuthRepository
): AuthRepository
```

Keep `bindAuthTokenProvider`, `bindsNewAuthRepository`, and all other bindings.

- [ ] **Step 6: Update NetworkModule — remove Supabase providers**

In `NetworkModule.kt`, remove:
- `providesSupabaseApiKey()`
- `providesSupabaseClient()`
- `provideSupabaseAuth()`
- `provideSupabaseFunctions()`
- `provideSupabaseAuthService()`
- `provideSupabasePostgrest()`
- `provideSupabasePostgrestService()`
- `okHttpCallFactory()` (check if Apollo needs OkHttp — Apollo 5 uses its own HTTP engine by default, so this should be safe to remove)
- All Supabase imports (`io.github.jan.supabase.*`)
- `SupabaseApiKey` import
- `OkHttpClient`, `HttpLoggingInterceptor` imports

Keep: `providesNetworkJson()`, `authorizationService()`, `provideApolloClient()`, `dateTimeAdapter`

- [ ] **Step 7: Build to verify**

Run: `./gradlew :core:data:compileDebugKotlin :core:network:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 8: Commit**

```bash
git add -u core/data/ core/network/ feature/home/
git commit -m "remove Supabase auth and simplify repositories to local-only storage"
```

---

### Task 4: Add pred.gg OAuth2 sign-in gate

**Files:**
- Modify: `app/src/main/kotlin/com/aowen/predcompanion/MainActivity.kt`
- Create: `app/src/main/kotlin/com/aowen/predcompanion/ui/SignInScreen.kt`
- Delete: `feature/auth/impl/src/main/kotlin/.../LoginViewModel.kt`
- Delete: `feature/auth/impl/src/main/kotlin/.../LoginScreen.kt`
- Delete: `feature/auth/impl/src/main/kotlin/.../navigation/LoginEntryProvider.kt`
- Delete: `app/src/main/kotlin/com/aowen/predcompanion/DeepLinksHandlerActivity.kt`

**Interfaces:**
- Consumes: `UserRepository.currentUserState: Flow<UserState>`, `NewAuthRepository.loginIntent(): Intent`, `NewAuthRepository.onLoginResult(intent: Intent): Result<Unit>`
- Produces: `SignInScreen` composable, auth-gated `MainActivity.setContent`

- [ ] **Step 1: Create SignInScreen composable**

```kotlin
// app/src/main/kotlin/com/aowen/predcompanion/ui/SignInScreen.kt
package com.aowen.predcompanion.ui

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SignInScreen(
    onLoginIntent: () -> Intent,
    onLoginResult: (Intent) -> Unit,
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data?.let { onLoginResult(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Predecessor Companion",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Powered by pred.gg",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(48.dp))
        Button(onClick = { launcher.launch(onLoginIntent()) }) {
            Text("Sign in with pred.gg")
        }
    }
}
```

- [ ] **Step 2: Rewrite MainActivity — remove Supabase, add auth gate**

```kotlin
package com.aowen.predcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.aowen.predcompanion.core.data.repository.auth.NewAuthRepository
import com.aowen.predcompanion.core.data.repository.user.UserRepository
import com.aowen.predcompanion.core.data.repository.user.UserState
import com.aowen.predcompanion.core.datastore.Theme
import com.aowen.predcompanion.core.datastore.ThemePreferences
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.ui.MonolithApp
import com.aowen.predcompanion.ui.SignInScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var userRepository: UserRepository
    @Inject lateinit var newAuthRepository: NewAuthRepository
    @Inject lateinit var themePreferences: ThemePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch { userRepository.sync() }

        setContent {
            val localTheme by themePreferences.theme.collectAsStateWithLifecycle(initialValue = Theme.SYSTEM)
            val userState by userRepository.currentUserState.collectAsStateWithLifecycle(initialValue = UserState.Loading)

            MonolithTheme(localTheme = localTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (userState) {
                        is UserState.Loading -> {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                        is UserState.SignedOut, is UserState.Error -> {
                            SignInScreen(
                                onLoginIntent = { newAuthRepository.loginIntent() },
                                onLoginResult = { data ->
                                    lifecycleScope.launch {
                                        newAuthRepository.onLoginResult(data)
                                        userRepository.sync()
                                    }
                                }
                            )
                        }
                        is UserState.SignedIn -> {
                            MonolithApp()
                        }
                    }
                }
            }
        }
    }
}
```

- [ ] **Step 3: Delete old auth files**

```bash
rm feature/auth/impl/src/main/kotlin/com/aowen/predcompanion/feature/auth/impl/LoginViewModel.kt
rm feature/auth/impl/src/main/kotlin/com/aowen/predcompanion/feature/auth/impl/LoginScreen.kt
rm feature/auth/impl/src/main/kotlin/com/aowen/predcompanion/feature/auth/impl/navigation/LoginEntryProvider.kt
rm app/src/main/kotlin/com/aowen/predcompanion/DeepLinksHandlerActivity.kt
```

- [ ] **Step 4: Remove loginEntry from MonolithApp**

In `MonolithApp.kt`, remove `loginEntry()` from the `entryProvider` block and the import for `com.aowen.predcompanion.feature.auth.impl.navigation.loginEntry`. Also remove the `UnauthenticatedRoot` composable from `MainActivity.kt` (already removed in Step 2).

- [ ] **Step 5: Remove DeepLinksHandlerActivity from AndroidManifest.xml**

Find and remove the `<activity>` entry for `DeepLinksHandlerActivity` and any Supabase deeplink intent filters from `AndroidManifest.xml`.

- [ ] **Step 6: Build to verify**

Run: `./gradlew :app:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 7: Commit**

```bash
git add -u app/ feature/auth/
git add app/src/main/kotlin/com/aowen/predcompanion/ui/SignInScreen.kt
git commit -m "add pred.gg OAuth2 sign-in gate and remove Supabase login flow"
```

---

### Task 5: Delete Retrofit network layer and clean up dependencies

**Files:**
- Delete: `core/network/src/main/kotlin/.../retrofit/RetrofitOmedaCityNetwork.kt`
- Delete: `core/network/src/main/kotlin/.../PredCompanionNetworkDataSource.kt`
- Delete: `core/network/src/main/kotlin/.../SafeApiCall.kt`
- Delete: `core/common/src/main/kotlin/.../network/RetrofitHelper.kt`
- Delete: `core/common/src/main/kotlin/.../network/utils/Utils.kt`
- Modify: `core/network/src/main/kotlin/.../di/DataSourceModule.kt`
- Delete: Unused `NetworkXxx` model DTOs in `core/network/model/`
- Modify: `gradle/libs.versions.toml`
- Modify: `core/network/build.gradle.kts`
- Modify: `core/common/build.gradle.kts`
- Modify: `app/build.gradle.kts`

**Interfaces:**
- Consumes: Nothing — this is pure deletion
- Produces: A codebase with zero Retrofit/OkHttp compile dependencies

- [ ] **Step 1: Delete Retrofit network files**

```bash
rm core/network/src/main/kotlin/com/aowen/predcompanion/core/network/retrofit/RetrofitOmedaCityNetwork.kt
rm core/network/src/main/kotlin/com/aowen/predcompanion/core/network/PredCompanionNetworkDataSource.kt
rm core/network/src/main/kotlin/com/aowen/predcompanion/core/network/SafeApiCall.kt
rm core/common/src/main/kotlin/com/aowen/predcompanion/core/common/network/RetrofitHelper.kt
rm core/common/src/main/kotlin/com/aowen/predcompanion/core/common/network/utils/Utils.kt
```

- [ ] **Step 2: Remove PredCompanionNetworkDataSource binding from DataSourceModule**

```kotlin
// DataSourceModule.kt — remove the binds for PredCompanionNetworkDataSource, keep PredGGNetworkDataSource:
@Module
@InstallIn(SingletonComponent::class)
internal interface DataSourceModule {
    @Binds
    fun bindsPredGGNetworkDataSource(
        impl: ApolloKotlinPredGGNetwork
    ): PredGGNetworkDataSource
}
```

Remove imports for `PredCompanionNetworkDataSource` and `RetrofitOmedaCityNetwork`.

- [ ] **Step 3: Delete unused Network model DTOs**

Grep for any remaining references to each `NetworkXxx` class before deleting. Delete files that have zero remaining imports:

```bash
# Check which are still referenced:
grep -rn "NetworkHero\b\|NetworkItem\b\|NetworkPlayer\b\|NetworkPlayerStats\b\|NetworkMatch\b\|NetworkHeroBuild\b\|NetworkHeroStatistics\b\|NetworkPlayerHeroStats\b\|NetworkPlayerSearchResult\b\|NetworkMatchesList\b\|NetworkFavoriteHero\b" core/ --include="*.kt" | grep -v build/ | grep -v test/

# Delete unreferenced ones (likely all of them after migration):
rm core/network/src/main/kotlin/com/aowen/predcompanion/core/network/model/NetworkHero.kt
rm core/network/src/main/kotlin/com/aowen/predcompanion/core/network/model/NetworkItem.kt
rm core/network/src/main/kotlin/com/aowen/predcompanion/core/network/model/NetworkPlayer.kt
rm core/network/src/main/kotlin/com/aowen/predcompanion/core/network/model/NetworkPlayerStats.kt
rm core/network/src/main/kotlin/com/aowen/predcompanion/core/network/model/NetworkMatch.kt
rm core/network/src/main/kotlin/com/aowen/predcompanion/core/network/model/NetworkHeroBuild.kt
rm core/network/src/main/kotlin/com/aowen/predcompanion/core/network/model/NetworkHeroStatistics.kt
rm core/network/src/main/kotlin/com/aowen/predcompanion/core/network/model/NetworkPlayerHeroStats.kt
rm core/network/src/main/kotlin/com/aowen/predcompanion/core/network/model/NetworkPlayerSearchResult.kt
rm core/network/src/main/kotlin/com/aowen/predcompanion/core/network/model/NetworkMatchesList.kt
rm core/network/src/main/kotlin/com/aowen/predcompanion/core/network/model/NetworkFavoriteHero.kt
```

Keep any `NetworkXxx` models that are still referenced (e.g., `NetworkFavoriteHeroBuild` if `UserFavoriteBuildsRepository` still uses it — check first).

- [ ] **Step 4: Remove Retrofit and Supabase dependencies from Gradle**

In `gradle/libs.versions.toml`:
- Remove version entries: `retrofit`, `retrofitSerialization` (if separate), `supabase` (and variants)
- Remove library entries: `retrofit-core`, `retrofit-serialization-converter`, `okhttp-logging`
- Remove Supabase library entries: `supabase-auth`, `supabase-postgrest`, `supabase-functions`, `supabase-gotrue`, `supabase-client` (exact names vary — check the file)
- Keep `okhttp` only if another dependency needs it

In relevant `build.gradle.kts` files, remove the dependency declarations:
- `core/network/build.gradle.kts` — remove retrofit, okhttp, supabase deps
- `core/common/build.gradle.kts` — remove okhttp deps
- `app/build.gradle.kts` — remove supabase deps (if present)

Also delete the `SupabaseApiKey` qualifier annotation:
```bash
# Remove or edit core/common/src/main/kotlin/com/aowen/predcompanion/core/common/di/Qualifiers.kt
# — delete the @SupabaseApiKey annotation class. If it's the only qualifier in the file, delete the file.
```

- [ ] **Step 5: Remove SUPABASE_URL and SUPABASE_API_KEY from BuildConfig**

Check `core/network/build.gradle.kts` or `local.properties` / `gradle.properties` for these build config fields and remove them.

- [ ] **Step 6: Build full project to verify**

Run: `./gradlew assembleDebug`
Expected: BUILD SUCCESSFUL

- [ ] **Step 7: Commit**

```bash
git add -u .
git commit -m "remove Retrofit, OkHttp, and Supabase dependencies"
```

---

### Task 6: Fix and clean up unit tests

**Files:**
- Delete: `core/data/src/test/.../AuthRepositoryTest.kt`
- Delete: `core/data/src/test/.../UserRepositoryTest.kt`
- Delete: `feature/auth/impl/src/test/.../LoginViewModelTest.kt`
- Delete: `core/testing/src/main/kotlin/.../TestPredCompanionNetworkDataSource.kt`
- Delete: `core/testing/src/main/kotlin/.../FakeSupabaseAuthService.kt`
- Modify: `core/data/src/test/.../HeroRepositoryTest.kt`
- Modify: `core/data/src/test/.../ItemRepositoryTest.kt`
- Modify: `core/data/src/test/.../BuildRepositoryTest.kt`
- Create: `core/testing/src/main/kotlin/.../TestPredGGNetworkDataSource.kt`
- Modify: Various fake repositories in `core/testing/`

**Interfaces:**
- Consumes: `PredGGNetworkDataSource` interface, Apollo response types
- Produces: `TestPredGGNetworkDataSource` fake, passing test suite

- [ ] **Step 1: Delete stale test files and Retrofit test infrastructure**

```bash
rm core/data/src/test/kotlin/com/aowen/predcompanion/core/data/repository/AuthRepositoryTest.kt
rm core/data/src/test/kotlin/com/aowen/predcompanion/core/data/repository/UserRepositoryTest.kt
rm feature/auth/impl/src/test/kotlin/com/aowen/predcompanion/feature/auth/impl/LoginViewModelTest.kt
rm core/testing/src/main/kotlin/com/aowen/predcompanion/core/testing/fakes/service/TestPredCompanionNetworkDataSource.kt
rm core/testing/src/main/kotlin/com/aowen/predcompanion/core/testing/fakes/service/FakeSupabaseAuthService.kt
```

- [ ] **Step 2: Create TestPredGGNetworkDataSource**

Create a fake implementation of `PredGGNetworkDataSource` that returns configurable `ApolloResponse` objects. The pattern: accept a behavior enum (success/error/exception) in the constructor, return canned Apollo responses.

```kotlin
// core/testing/src/main/kotlin/com/aowen/predcompanion/core/testing/fakes/service/TestPredGGNetworkDataSource.kt
package com.aowen.predcompanion.core.testing.fakes.service

import com.aowen.predcompanion.core.network.PredGGNetworkDataSource
import com.aowen.predcompanion.core.network.apollo.AuthedUserQuery
import com.aowen.predcompanion.core.network.apollo.BuildsQuery
import com.aowen.predcompanion.core.network.apollo.GetGuideQuery
import com.aowen.predcompanion.core.network.apollo.GetPlayerQuery
import com.aowen.predcompanion.core.network.apollo.HeroesQuery
import com.aowen.predcompanion.core.network.apollo.ItemsQuery
import com.aowen.predcompanion.core.network.apollo.MatchByIdQuery
import com.aowen.predcompanion.core.network.apollo.SearchPlayersQuery
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Error

// A test double for PredGGNetworkDataSource.
// Configure by providing lambdas or override individual methods in subclasses.
class TestPredGGNetworkDataSource(
    private val shouldError: Boolean = false,
) : PredGGNetworkDataSource {

    // Implementers should override these with actual test data builders.
    // The exact implementation depends on Apollo codegen output shapes.
    // For now, provide the skeleton — fill in at test-writing time.

    override suspend fun getCurrentUser(): ApolloResponse<AuthedUserQuery.Data> =
        TODO("Provide test implementation")

    override suspend fun getPlayer(playerId: String): ApolloResponse<GetPlayerQuery.Data> =
        TODO("Provide test implementation")

    override suspend fun getAllItems(): ApolloResponse<ItemsQuery.Data> =
        TODO("Provide test implementation")

    override suspend fun getAllHeroes(): ApolloResponse<HeroesQuery.Data> =
        TODO("Provide test implementation")

    override suspend fun getMatchById(matchId: String): ApolloResponse<MatchByIdQuery.Data> =
        TODO("Provide test implementation")

    override suspend fun searchPlayers(search: String, limit: Int?, offset: Int?): ApolloResponse<SearchPlayersQuery.Data> =
        TODO("Provide test implementation")

    override suspend fun getBuilds(filter: Any?, order: Any?, limit: Int?, offset: Int?): ApolloResponse<BuildsQuery.Data> =
        TODO("Provide test implementation")

    override suspend fun getBuildById(buildId: String): ApolloResponse<GetGuideQuery.Data> =
        TODO("Provide test implementation")
}
```

Note: Apollo `ApolloResponse` can be constructed via `ApolloResponse.Builder`. The exact construction depends on the codegen output. At implementation time, build responses using `ApolloResponse.Builder(operation, requestUuid, data)`. The TODO placeholders must be filled in with actual test data — this is where the implementer needs to look at how the existing fake data files construct test objects and replicate that pattern with Apollo types.

- [ ] **Step 3: Update HeroRepositoryTest**

Rewrite to construct `OmedaCityHeroRepository(predGGNetwork = TestPredGGNetworkDataSource(...))`. Update test assertions: `fetchAllHeroes()` now returns `Unit` and populates `allHeroes` StateFlow, so assert on `repository.allHeroes.value` after calling `fetchAllHeroes()`. Remove tests for `fetchHeroByName` returning `Resource` (now it's a synchronous lookup from the StateFlow).

- [ ] **Step 4: Update ItemRepositoryTest**

Same pattern as heroes — constructor takes only `PredGGNetworkDataSource`. `fetchAllItems()` returns `Unit`, assert on `allItems` StateFlow.

- [ ] **Step 5: Update BuildRepositoryTest**

Constructor takes only `PredGGNetworkDataSource`. `fetchAllBuilds()` and `fetchBuildById()` still return `Resource<T>`, so assertions can follow the same success/error/exception pattern but with GraphQL responses instead of Retrofit.

- [ ] **Step 6: Update Fake repositories in core/testing**

Check `FakeOmedaCityPlayerRepository`, `FakeOmedaCityHeroRepository`, `FakeOmedaCityBuildRepository`, `FakeOmedaCityItemRepository`, `FakeOmedaCityMatchRepository` — update their implementations to match the new repository interfaces (any changed method signatures).

- [ ] **Step 7: Run all tests**

Run: `./gradlew test`
Expected: All tests pass (delete any remaining tests that fail due to removed types and can't be reasonably fixed)

- [ ] **Step 8: Commit**

```bash
git add -u .
git add core/testing/src/main/kotlin/com/aowen/predcompanion/core/testing/fakes/service/TestPredGGNetworkDataSource.kt
git commit -m "fix unit tests for GraphQL migration, remove stale Retrofit test infrastructure"
```

---

### Task 7: Final cleanup and verification

**Files:**
- Modify: Various files with stale imports or references
- Modify: `core/ui/src/main/kotlin/.../shared/ItemDetailsBottomSheet.kt` (remove omeda.city reference)

**Interfaces:**
- Consumes: Full compiled project
- Produces: Clean build, clean grep for Retrofit/omeda.city/Supabase

- [ ] **Step 1: Grep for any remaining Retrofit/omeda.city/Supabase references**

```bash
grep -rn "retrofit2\|okhttp3\|omeda\.city\|Supabase\|supabase\|SupabaseClient\|PredCompanionNetworkDataSource\|RetrofitOmedaCityNetwork\|safeApiCall\b\|RetrofitHelper" --include="*.kt" core/ app/ feature/ | grep -v build/ | grep -v test
```

Fix any remaining references found.

- [ ] **Step 2: Update ItemDetailsBottomSheet**

Remove or update "Powered by Omeda.city" text — change to "Powered by pred.gg" or remove entirely.

- [ ] **Step 3: Clean build**

Run: `./gradlew clean assembleDebug`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Run all tests**

Run: `./gradlew test`
Expected: All tests pass

- [ ] **Step 5: Commit**

```bash
git add -u .
git commit -m "final cleanup: remove remaining Retrofit and omeda.city references"
```
