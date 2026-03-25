package com.aowen.monolith.data.repository.items;

import com.aowen.monolith.network.OmedaCityService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class OmedaCityItemRepository_Factory implements Factory<OmedaCityItemRepository> {
  private final Provider<OmedaCityService> omedaCityServiceProvider;

  private OmedaCityItemRepository_Factory(Provider<OmedaCityService> omedaCityServiceProvider) {
    this.omedaCityServiceProvider = omedaCityServiceProvider;
  }

  @Override
  public OmedaCityItemRepository get() {
    return newInstance(omedaCityServiceProvider.get());
  }

  public static OmedaCityItemRepository_Factory create(
      Provider<OmedaCityService> omedaCityServiceProvider) {
    return new OmedaCityItemRepository_Factory(omedaCityServiceProvider);
  }

  public static OmedaCityItemRepository newInstance(OmedaCityService omedaCityService) {
    return new OmedaCityItemRepository(omedaCityService);
  }
}
