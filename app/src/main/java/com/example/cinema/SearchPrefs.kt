package com.example.cinema

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchPrefs(private val dataStore: DataStore<Preferences>) {
    private val KEY_TYPE = stringPreferencesKey("search_type") // ALL/MOVIE/TV
    private val KEY_COUNTRY = intPreferencesKey("search_country")
    private val KEY_GENRE = intPreferencesKey("search_genre")
    private val KEY_YEAR_FROM = intPreferencesKey("search_year_from")
    private val KEY_YEAR_TO = intPreferencesKey("search_year_to")
    private val KEY_RATING_FROM = intPreferencesKey("search_rating_from")
    private val KEY_RATING_TO = intPreferencesKey("search_rating_to")
    private val KEY_ORDER = stringPreferencesKey("search_order") // RATING/NUM_VOTE/YEAR
    private val KEY_HIDE_WATCHED = booleanPreferencesKey("search_hide_watched")

    val state: Flow<SearchSettings> = dataStore.data.map { prefs ->
        SearchSettings(
            type = prefs[KEY_TYPE] ?: "ALL",
            countryId = prefs[KEY_COUNTRY],
            genreId = prefs[KEY_GENRE],
            yearFrom = prefs[KEY_YEAR_FROM] ?: 1900,
            yearTo = prefs[KEY_YEAR_TO] ?: 2100,
            ratingFrom = prefs[KEY_RATING_FROM] ?: 0,
            ratingTo = prefs[KEY_RATING_TO] ?: 10,
            order = prefs[KEY_ORDER] ?: "RATING",
            hideWatched = prefs[KEY_HIDE_WATCHED] ?: false
        )
    }

    suspend fun save(settings: SearchSettings) {
        dataStore.edit { prefs ->
            prefs[KEY_TYPE] = settings.type
            settings.countryId?.let { prefs[KEY_COUNTRY] = it } ?: prefs.remove(KEY_COUNTRY)
            settings.genreId?.let { prefs[KEY_GENRE] = it } ?: prefs.remove(KEY_GENRE)
            prefs[KEY_YEAR_FROM] = settings.yearFrom
            prefs[KEY_YEAR_TO] = settings.yearTo
            prefs[KEY_RATING_FROM] = settings.ratingFrom
            prefs[KEY_RATING_TO] = settings.ratingTo
            prefs[KEY_ORDER] = settings.order
            prefs[KEY_HIDE_WATCHED] = settings.hideWatched
        }
    }
}

data class SearchSettings(
    val type: String,
    val countryId: Int?,
    val genreId: Int?,
    val yearFrom: Int,
    val yearTo: Int,
    val ratingFrom: Int,
    val ratingTo: Int,
    val order: String,
    val hideWatched: Boolean
)


