package com.aowen.monolith.data.database.di;

import android.content.Context;
import com.aowen.monolith.data.database.MonolithDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DatabaseModule_ProvidesMonolithDatabaseFactory implements Factory<MonolithDatabase> {
  private final Provider<Context> contextProvider;

  private DatabaseModule_ProvidesMonolithDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public MonolithDatabase get() {
    return providesMonolithDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvidesMonolithDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvidesMonolithDatabaseFactory(contextProvider);
  }

  public static MonolithDatabase providesMonolithDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providesMonolithDatabase(context));
  }
}
