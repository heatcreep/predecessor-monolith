package com.aowen.monolith.data.database.di;

import com.aowen.monolith.data.database.MonolithDatabase;
import com.aowen.monolith.data.database.dao.FavoriteBuildDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DaosModule_ProvidesFavoriteBuildDaoFactory implements Factory<FavoriteBuildDao> {
  private final Provider<MonolithDatabase> databaseProvider;

  private DaosModule_ProvidesFavoriteBuildDaoFactory(Provider<MonolithDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public FavoriteBuildDao get() {
    return providesFavoriteBuildDao(databaseProvider.get());
  }

  public static DaosModule_ProvidesFavoriteBuildDaoFactory create(
      Provider<MonolithDatabase> databaseProvider) {
    return new DaosModule_ProvidesFavoriteBuildDaoFactory(databaseProvider);
  }

  public static FavoriteBuildDao providesFavoriteBuildDao(MonolithDatabase database) {
    return Preconditions.checkNotNullFromProvides(DaosModule.INSTANCE.providesFavoriteBuildDao(database));
  }
}
