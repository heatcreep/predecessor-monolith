package com.aowen.monolith.network;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.postgrest.Postgrest;
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
public final class SupabasePostgrestServiceImpl_Factory implements Factory<SupabasePostgrestServiceImpl> {
  private final Provider<Postgrest> postgrestProvider;

  private SupabasePostgrestServiceImpl_Factory(Provider<Postgrest> postgrestProvider) {
    this.postgrestProvider = postgrestProvider;
  }

  @Override
  public SupabasePostgrestServiceImpl get() {
    return newInstance(postgrestProvider.get());
  }

  public static SupabasePostgrestServiceImpl_Factory create(Provider<Postgrest> postgrestProvider) {
    return new SupabasePostgrestServiceImpl_Factory(postgrestProvider);
  }

  public static SupabasePostgrestServiceImpl newInstance(Postgrest postgrest) {
    return new SupabasePostgrestServiceImpl(postgrest);
  }
}
