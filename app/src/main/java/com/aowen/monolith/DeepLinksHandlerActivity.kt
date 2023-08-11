package com.aowen.monolith

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.handleDeeplinks
import javax.inject.Inject

@AndroidEntryPoint
class DeepLinksHandlerActivity : ComponentActivity() {

    @Inject
    lateinit var supabaseClient: SupabaseClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supabaseClient.handleDeeplinks(intent = intent,
            onSessionSuccess = {
                Log.d("MONOLITH-DEBUG://", "onSessionSuccess")
            })
    }
}