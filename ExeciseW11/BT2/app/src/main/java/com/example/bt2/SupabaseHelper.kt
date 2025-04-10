// SupabaseHelper.kt
package com.example.bt2

import android.util.Log
import com.example.bt2.model.Video1Model
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.createSupabaseClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SupabaseHelper {

    private const val SUPABASE_URL = "https://wjcwngquuthyylbsdaqj.supabase.co"
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndqY3duZ3F1dXRoeXlsYnNkYXFqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDQyNjYwODgsImV4cCI6MjA1OTg0MjA4OH0.Uq8Q82d4N5KI8LsrIC1sgURBbIqDeKfLO0xnlFD76C0"

    val supabase: SupabaseClient = createSupabaseClient(
        SUPABASE_URL,
        SUPABASE_ANON_KEY
    ) {
        install(Postgrest)
        install(Storage)
    }

    suspend fun fetchVideos(): List<Video1Model> {
        return withContext(Dispatchers.IO) {
            val result = supabase.from("videos").select().decodeList<Video1Model>()
            result
        }
    }
}
