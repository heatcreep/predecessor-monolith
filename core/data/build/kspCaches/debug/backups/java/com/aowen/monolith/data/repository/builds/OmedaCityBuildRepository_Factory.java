package com.aowen.monolith.data.repository.builds;

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
public final class OmedaCityBuildRepository_Factory implements Factory<OmedaCityBuildRepository> {
  private final Provider<OmedaCityService> omedaCityServiceProvider;

  private OmedaCityBuildRepository_Factory(Provider<OmedaCityService> omedaCityServiceProvider) {
    this.omedaCityServiceProvider = omedaCityServiceProvider;
  }

  @Override
  public OmedaCityBuildRepository get() {
    return newInstance(omedaCityServiceProvider.get());
  }

  public static OmedaCityBuildRepository_Factory create(
      Provider<OmedaCityService> omedaCityServiceProvider) {
    return new OmedaCityBuildRepository_Factory(omedaCityServiceProvider);
  }

  public static OmedaCityBuildRepository newInstance(OmedaCityService omedaCityService) {
    return new OmedaCityBuildRepository(omedaCityService);
  }
}
