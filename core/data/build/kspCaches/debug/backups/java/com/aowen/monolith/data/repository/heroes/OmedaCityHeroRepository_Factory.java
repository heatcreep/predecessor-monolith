package com.aowen.monolith.data.repository.heroes;

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
public final class OmedaCityHeroRepository_Factory implements Factory<OmedaCityHeroRepository> {
  private final Provider<OmedaCityService> omedaCityServiceProvider;

  private OmedaCityHeroRepository_Factory(Provider<OmedaCityService> omedaCityServiceProvider) {
    this.omedaCityServiceProvider = omedaCityServiceProvider;
  }

  @Override
  public OmedaCityHeroRepository get() {
    return newInstance(omedaCityServiceProvider.get());
  }

  public static OmedaCityHeroRepository_Factory create(
      Provider<OmedaCityService> omedaCityServiceProvider) {
    return new OmedaCityHeroRepository_Factory(omedaCityServiceProvider);
  }

  public static OmedaCityHeroRepository newInstance(OmedaCityService omedaCityService) {
    return new OmedaCityHeroRepository(omedaCityService);
  }
}
