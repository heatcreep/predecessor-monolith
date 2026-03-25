package com.aowen.monolith.network;

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
public final class UserRecentSearchRepositoryImpl_Factory implements Factory<UserRecentSearchRepositoryImpl> {
  private final Provider<SupabasePostgrestService> postgrestServiceProvider;

  private final Provider<PlayerRepository> omedaCityPlayerRepositoryProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  private UserRecentSearchRepositoryImpl_Factory(
      Provider<SupabasePostgrestService> postgrestServiceProvider,
      Provider<PlayerRepository> omedaCityPlayerRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider) {
    this.postgrestServiceProvider = postgrestServiceProvider;
    this.omedaCityPlayerRepositoryProvider = omedaCityPlayerRepositoryProvider;
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public UserRecentSearchRepositoryImpl get() {
    return newInstance(postgrestServiceProvider.get(), omedaCityPlayerRepositoryProvider.get(), userRepositoryProvider.get());
  }

  public static UserRecentSearchRepositoryImpl_Factory create(
      Provider<SupabasePostgrestService> postgrestServiceProvider,
      Provider<PlayerRepository> omedaCityPlayerRepositoryProvider,
      Provider<UserRepository> userRepositoryProvider) {
    return new UserRecentSearchRepositoryImpl_Factory(postgrestServiceProvider, omedaCityPlayerRepositoryProvider, userRepositoryProvider);
  }

  public static UserRecentSearchRepositoryImpl newInstance(
      SupabasePostgrestService postgrestService, PlayerRepository omedaCityPlayerRepository,
      UserRepository userRepository) {
    return new UserRecentSearchRepositoryImpl(postgrestService, omedaCityPlayerRepository, userRepository);
  }
}
