package com.aowen.monolith.data.repository.players;

import com.aowen.monolith.network.OmedaCityService;
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
public final class OmedaCityPlayerRepository_Factory implements Factory<OmedaCityPlayerRepository> {
  private final Provider<OmedaCityService> omedaCityServiceProvider;

  private OmedaCityPlayerRepository_Factory(Provider<OmedaCityService> omedaCityServiceProvider) {
    this.omedaCityServiceProvider = omedaCityServiceProvider;
  }

  @Override
  public OmedaCityPlayerRepository get() {
    return newInstance(omedaCityServiceProvider.get());
  }

  public static OmedaCityPlayerRepository_Factory create(
      Provider<OmedaCityService> omedaCityServiceProvider) {
    return new OmedaCityPlayerRepository_Factory(omedaCityServiceProvider);
  }

  public static OmedaCityPlayerRepository newInstance(OmedaCityService omedaCityService) {
    return new OmedaCityPlayerRepository(omedaCityService);
  }
}
