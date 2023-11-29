package com.example.waygo.pagging

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.waygo.SessionPrefs
import com.example.waygo.data.response.TourismItems
import com.example.waygo.retrofit.ApiService

class TourismPagging (private val apiService: ApiService, private val context: Context?) :
    PagingSource<Int, TourismItems>() {

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TourismItems> {
        val apiKey = getApikey()
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val data = apiService.getStoriesWithPaging(page = page, token = "Bearer $apiKey")
            Log.d("RETROFIT", data.body()!!.toString())
            val responseData = data.body()!!.listStory
            LoadResult.Page(
                data = responseData,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            Log.d("LOADERROR", exception.toString())
            return LoadResult.Error(exception)
        }
    }

    private fun getApikey(): String {
        val session = context?.let { SessionPrefs(it) }
        val token = session?.getToken()
        return token?.api_key.toString()
    }

    override fun getRefreshKey(state: PagingState<Int, TourismItems>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}