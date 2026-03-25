package com.aowen.monolith.network;

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
public final class UserRepositoryImpl_Factory implements Factory<UserRepositoryImpl> {
  private final Provider<SupabaseAuthService> authServiceProvider;

  private final Provider<SupabasePostgrestService> postgrestServiceProvider;

  private UserRepositoryImpl_Factory(Provider<SupabaseAuthService> authServiceProvider,
      Provider<SupabasePostgrestService> postgrestServiceProvider) {
    this.authServiceProvider = authServiceProvider;
    this.postgrestServiceProvider = postgrestServiceProvider;
  }

  @Override
  public UserRepositoryImpl get() {
    return newInstance(authServiceProvider.get(), postgrestServiceProvider.get());
  }

  public static UserRepositoryImpl_Factory create(Provider<SupabaseAuthService> authServiceProvider,
      Provider<SupabasePostgrestService> postgrestServiceProvider) {
    return new UserRepositoryImpl_Factory(authServiceProvider, postgrestServiceProvider);
  }

  public static UserRepositoryImpl newInstance(SupabaseAuthService authService,
      SupabasePostgrestService postgrestService) {
    return new UserRepositoryImpl(authService, postgrestService);
  }
}
