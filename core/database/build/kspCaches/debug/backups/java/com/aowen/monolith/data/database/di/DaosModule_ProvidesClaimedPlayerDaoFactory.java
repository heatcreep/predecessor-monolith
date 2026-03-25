package com.aowen.monolith.data.database.di;

import com.aowen.monolith.data.database.MonolithDatabase;
import com.aowen.monolith.data.database.dao.ClaimedPlayerDao;
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
public final class DaosModule_ProvidesClaimedPlayerDaoFactory implements Factory<ClaimedPlayerDao> {
  private final Provider<MonolithDatabase> databaseProvider;

  private DaosModule_ProvidesClaimedPlayerDaoFactory(Provider<MonolithDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ClaimedPlayerDao get() {
    return providesClaimedPlayerDao(databaseProvider.get());
  }

  public static DaosModule_ProvidesClaimedPlayerDaoFactory create(
      Provider<MonolithDatabase> databaseProvider) {
    return new DaosModule_ProvidesClaimedPlayerDaoFactory(databaseProvider);
  }

  public static ClaimedPlayerDao providesClaimedPlayerDao(MonolithDatabase database) {
    return Preconditions.checkNotNullFromProvides(DaosModule.INSTANCE.providesClaimedPlayerDao(database));
  }
}
