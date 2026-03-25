package com.aowen.monolith.network;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.functions.Functions;
import io.github.jan.supabase.gotrue.Auth;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("com.aowen.monolith.di.SupabaseApiKey")
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
public final class SupabaseAuthServiceImpl_Factory implements Factory<SupabaseAuthServiceImpl> {
  private final Provider<Auth> authProvider;

  private final Provider<Functions> functionsProvider;

  private final Provider<String> supabaseApiKeyProvider;

  private SupabaseAuthServiceImpl_Factory(Provider<Auth> authProvider,
      Provider<Functions> functionsProvider, Provider<String> supabaseApiKeyProvider) {
    this.authProvider = authProvider;
    this.functionsProvider = functionsProvider;
    this.supabaseApiKeyProvider = supabaseApiKeyProvider;
  }

  @Override
  public SupabaseAuthServiceImpl get() {
    return newInstance(authProvider.get(), functionsProvider.get(), supabaseApiKeyProvider.get());
  }

  public static SupabaseAuthServiceImpl_Factory create(Provider<Auth> authProvider,
      Provider<Functions> functionsProvider, Provider<String> supabaseApiKeyProvider) {
    return new SupabaseAuthServiceImpl_Factory(authProvider, functionsProvider, supabaseApiKeyProvider);
  }

  public static SupabaseAuthServiceImpl newInstance(Auth auth, Functions functions,
      String supabaseApiKey) {
    return new SupabaseAuthServiceImpl(auth, functions, supabaseApiKey);
  }
}
