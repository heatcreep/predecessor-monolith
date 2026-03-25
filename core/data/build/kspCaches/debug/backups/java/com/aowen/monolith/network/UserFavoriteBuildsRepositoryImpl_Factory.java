package com.aowen.monolith.network;

import com.aowen.monolith.data.database.dao.FavoriteBuildDao;
import com.aowen.monolith.ui.model.BuildListItemUiMapper;
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
public final class UserFavoriteBuildsRepositoryImpl_Factory implements Factory<UserFavoriteBuildsRepositoryImpl> {
  private final Provider<SupabasePostgrestService> postgrestServiceProvider;

  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<FavoriteBuildDao> favoriteBuildDaoProvider;

  private final Provider<BuildListItemUiMapper> buildListItemUiMapperProvider;

  private UserFavoriteBuildsRepositoryImpl_Factory(
      Provider<SupabasePostgrestService> postgrestServiceProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<FavoriteBuildDao> favoriteBuildDaoProvider,
      Provider<BuildListItemUiMapper> buildListItemUiMapperProvider) {
    this.postgrestServiceProvider = postgrestServiceProvider;
    this.userRepositoryProvider = userRepositoryProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.favoriteBuildDaoProvider = favoriteBuildDaoProvider;
    this.buildListItemUiMapperProvider = buildListItemUiMapperProvider;
  }

  @Override
  public UserFavoriteBuildsRepositoryImpl get() {
    return newInstance(postgrestServiceProvider.get(), userRepositoryProvider.get(), authRepositoryProvider.get(), favoriteBuildDaoProvider.get(), buildListItemUiMapperProvider.get());
  }

  public static UserFavoriteBuildsRepositoryImpl_Factory create(
      Provider<SupabasePostgrestService> postgrestServiceProvider,
      Provider<UserRepository> userRepositoryProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<FavoriteBuildDao> favoriteBuildDaoProvider,
      Provider<BuildListItemUiMapper> buildListItemUiMapperProvider) {
    return new UserFavoriteBuildsRepositoryImpl_Factory(postgrestServiceProvider, userRepositoryProvider, authRepositoryProvider, favoriteBuildDaoProvider, buildListItemUiMapperProvider);
  }

  public static UserFavoriteBuildsRepositoryImpl newInstance(
      SupabasePostgrestService postgrestService, UserRepository userRepository,
      AuthRepository authRepository, FavoriteBuildDao favoriteBuildDao,
      BuildListItemUiMapper buildListItemUiMapper) {
    return new UserFavoriteBuildsRepositoryImpl(postgrestService, userRepository, authRepository, favoriteBuildDao, buildListItemUiMapper);
  }
}
