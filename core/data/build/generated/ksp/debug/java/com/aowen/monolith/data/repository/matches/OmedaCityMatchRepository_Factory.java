package com.aowen.monolith.data.repository.matches;

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
public final class OmedaCityMatchRepository_Factory implements Factory<OmedaCityMatchRepository> {
  private final Provider<OmedaCityService> omedaCityServiceProvider;

  private OmedaCityMatchRepository_Factory(Provider<OmedaCityService> omedaCityServiceProvider) {
    this.omedaCityServiceProvider = omedaCityServiceProvider;
  }

  @Override
  public OmedaCityMatchRepository get() {
    return newInstance(omedaCityServiceProvider.get());
  }

  public static OmedaCityMatchRepository_Factory create(
      Provider<OmedaCityService> omedaCityServiceProvider) {
    return new OmedaCityMatchRepository_Factory(omedaCityServiceProvider);
  }

  public static OmedaCityMatchRepository newInstance(OmedaCityService omedaCityService) {
    return new OmedaCityMatchRepository(omedaCityService);
  }
}
