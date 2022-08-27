package org.captaindmitro.data.repository

import android.content.SharedPreferences
import org.captaindmitro.data.DEFAULT_CATEGORY
import org.captaindmitro.data.DEFAULT_COUNTRY
import org.captaindmitro.data.LAST_VISITED_CATEGORY
import org.captaindmitro.data.SELECTED_COUNTRY
import javax.inject.Inject

interface SharedPrefsDataSource {
    suspend fun writeCategory(category: String)
    fun readCategory(): String?
    suspend fun writeLanguage(language: String)
    fun readLanguage(): String?

    class Base @Inject constructor(
        private val sharedPreferences: SharedPreferences
    ) : SharedPrefsDataSource {
        override suspend fun writeCategory(category: String) {
            with (sharedPreferences.edit()) {
                putString(LAST_VISITED_CATEGORY, category)
                apply()
            }
        }

        override fun readCategory(): String? = sharedPreferences.getString(LAST_VISITED_CATEGORY, DEFAULT_CATEGORY)

        override suspend fun writeLanguage(language: String) {
            with (sharedPreferences.edit()) {
                putString(SELECTED_COUNTRY, language)
                apply()
            }
        }

        override fun readLanguage(): String? = sharedPreferences.getString(SELECTED_COUNTRY, DEFAULT_COUNTRY)
    }
}