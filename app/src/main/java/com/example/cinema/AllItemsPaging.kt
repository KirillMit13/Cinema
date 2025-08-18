package com.example.cinema

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.example.cinema.data.mapper.toDomain
import com.example.cinema.data.remote.model.KinopoiskService
import com.example.cinema.domain.model.Film
import kotlinx.coroutines.CoroutineScope

class FilmsPagingSource(
    private val loader: suspend (page: Int) -> List<Film>
) : PagingSource<Int, Film>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
        return try {
            val page = params.key ?: 1
            val data = loader(page)
            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Film>): Int? = null
}

fun pagerForTop(api: KinopoiskService, type: String, scope: CoroutineScope) =
    Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 1),
        pagingSourceFactory = {
            FilmsPagingSource { page -> 
                api.getTopFilms(type = type, page = page).items.map { it.toDomain() }
            }
        }
    ).flow.cachedIn(scope)

fun pagerForFilters(
    api: KinopoiskService,
    scope: CoroutineScope,
    countryId: Int? = null,
    genreId: Int? = null,
    type: String? = null,
    order: String = "RATING",
    ratingFrom: Int = 8
) = Pager(
    config = PagingConfig(pageSize = 20, prefetchDistance = 1),
    pagingSourceFactory = {
        FilmsPagingSource { page ->
            api.filterFilms(
                type = type,
                order = order,
                ratingFrom = ratingFrom,
                countries = countryId,
                genres = genreId,
                page = page
            ).items.map { it.toDomain() }
        }
    }
).flow.cachedIn(scope)


