package com.aowen.monolith.ui.model;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class BuildListItemUiMapper_Factory implements Factory<BuildListItemUiMapper> {
  @Override
  public BuildListItemUiMapper get() {
    return newInstance();
  }

  public static BuildListItemUiMapper_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static BuildListItemUiMapper newInstance() {
    return new BuildListItemUiMapper();
  }

  private static final class InstanceHolder {
    static final BuildListItemUiMapper_Factory INSTANCE = new BuildListItemUiMapper_Factory();
  }
}
