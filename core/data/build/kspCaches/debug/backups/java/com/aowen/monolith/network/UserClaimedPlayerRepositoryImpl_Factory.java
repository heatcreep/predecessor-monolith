package com.aowen.monolith.network;

import com.aowen.monolith.data.database.dao.ClaimedPlayerDao;
import com.aowen.monolith.data.repository.players.di.PlayerRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class UserClaimedPlayerRepositoryImpl_Factory implements Factory<UserClaimedPlayerRepositoryImpl> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<UserPreferencesManager> userPreferencesManagerProvider;

  private final Provider<SupabasePostgrestService> postgrestServiceProvider;

  private final Provider<ClaimedPlayerDao> claimedPlayerDaoProvider;

  private final Provider<PlayerRepository> omedaCityPlayerRepositoryProvider;

  private UserClaimedPlayerRepositoryImpl_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<UserPreferencesManager> userPreferencesManagerProvider,
      Provider<SupabasePostgrestService> postgrestServiceProvider,
      Provider<ClaimedPlayerDao> claimedPlayerDaoProvider,
      Provider<PlayerRepository> omedaCityPlayerRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.userRepositoryProvider = userRepositoryProvider;
    this.userPreferencesManagerProvider = userPreferencesManagerProvider;
    this.postgrestServiceProvider = postgrestServiceProvider;
    this.claimedPlayerDaoProvider = claimedPlayerDaoProvider;
    this.omedaCityPlayerRepositoryProvider = omedaCityPlayerRepositoryProvider;
  }

  @Override
  public UserClaimedPlayerRepositoryImpl get() {
    return newInstance(authRepositoryProvider.get(), userRepositoryProvider.get(), userPreferencesManagerProvider.get(), postgrestServiceProvider.get(), claimedPlayerDaoProvider.get(), omedaCityPlayerRepositoryProvider.get());
  }

  public static UserClaimedPlayerRepositoryImpl_Factory create(
      Provider<AuthRepository> authRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<UserPreferencesManager> userPreferencesManagerProvider,
      Provider<SupabasePostgrestService> postgrestServiceProvider,
      Provider<ClaimedPlayerDao> claimedPlayerDaoProvider,
      Provider<PlayerRepository> omedaCityPlayerRepositoryProvider) {
    return new UserClaimedPlayerRepositoryImpl_Factory(authRepositoryProvider, userRepositoryProvider, userPreferencesManagerProvider, postgrestServiceProvider, claimedPlayerDaoProvider, omedaCityPlayerRepositoryProvider);
  }

  public static UserClaimedPlayerRepositoryImpl newInstance(AuthRepository authRepository,
      UserRepository userRepository, UserPreferencesManager userPreferencesManager,
      SupabasePostgrestService postgrestService, ClaimedPlayerDao claimedPlayerDao,
      PlayerRepository omedaCityPlayerRepository) {
    return new UserClaimedPlayerRepositoryImpl(authRepository, userRepository, userPreferencesManager, postgrestService, claimedPlayerDao, omedaCityPlayerRepository);
  }
}
