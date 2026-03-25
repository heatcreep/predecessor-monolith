package com.aowen.monolith.network;

import com.aowen.monolith.data.database.dao.ClaimedPlayerDao;
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
public final class AuthRepositoryImpl_Factory implements Factory<AuthRepositoryImpl> {
  private final Provider<SupabaseAuthService> authServiceProvider;

  private final Provider<ClaimedPlayerDao> claimedPlayerDaoProvider;

  private final Provider<SupabasePostgrestService> postgrestServiceProvider;

  private final Provider<UserPreferencesManager> userPreferencesManagerProvider;

  private AuthRepositoryImpl_Factory(Provider<SupabaseAuthService> authServiceProvider,
      Provider<ClaimedPlayerDao> claimedPlayerDaoProvider,
      Provider<SupabasePostgrestService> postgrestServiceProvider,
      Provider<UserPreferencesManager> userPreferencesManagerProvider) {
    this.authServiceProvider = authServiceProvider;
    this.claimedPlayerDaoProvider = claimedPlayerDaoProvider;
    this.postgrestServiceProvider = postgrestServiceProvider;
    this.userPreferencesManagerProvider = userPreferencesManagerProvider;
  }

  @Override
  public AuthRepositoryImpl get() {
    return newInstance(authServiceProvider.get(), claimedPlayerDaoProvider.get(), postgrestServiceProvider.get(), userPreferencesManagerProvider.get());
  }

  public static AuthRepositoryImpl_Factory create(Provider<SupabaseAuthService> authServiceProvider,
      Provider<ClaimedPlayerDao> claimedPlayerDaoProvider,
      Provider<SupabasePostgrestService> postgrestServiceProvider,
      Provider<UserPreferencesManager> userPreferencesManagerProvider) {
    return new AuthRepositoryImpl_Factory(authServiceProvider, claimedPlayerDaoProvider, postgrestServiceProvider, userPreferencesManagerProvider);
  }

  public static AuthRepositoryImpl newInstance(SupabaseAuthService authService,
      ClaimedPlayerDao claimedPlayerDao, SupabasePostgrestService postgrestService,
      UserPreferencesManager userPreferencesManager) {
    return new AuthRepositoryImpl(authService, claimedPlayerDao, postgrestService, userPreferencesManager);
  }
}
