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
public final class PlayerProfileUiMapper_Factory implements Factory<PlayerProfileUiMapper> {
  @Override
  public PlayerProfileUiMapper get() {
    return newInstance();
  }

  public static PlayerProfileUiMapper_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PlayerProfileUiMapper newInstance() {
    return new PlayerProfileUiMapper();
  }

  private static final class InstanceHolder {
    static final PlayerProfileUiMapper_Factory INSTANCE = new PlayerProfileUiMapper_Factory();
  }
}
