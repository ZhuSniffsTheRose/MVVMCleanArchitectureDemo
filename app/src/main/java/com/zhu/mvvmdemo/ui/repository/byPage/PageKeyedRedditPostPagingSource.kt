package com.zhu.mvvmdemo.ui.repository.byPage

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zhu.mvvmdemo.api.api
import com.zhu.mvvmdemo.data.RedditPost
import retrofit2.HttpException
import java.io.IOException

/**
 * @author Zhu
 * @date 2021/7/5 11:14
 * @desc 一次列表请求对应一个 PageKeyedRedditPostPagingSource 实例， 包括下拉刷新
 */
class PageKeyedRedditPostPagingSource(val keyWord: String) : PagingSource<String, RedditPost>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditPost> {
        return try {
            val data = api.getTop(
                    subreddit = keyWord ?: "androiddev",
                    after = if (params is LoadParams.Append) params.key else null,
                    before = if (params is LoadParams.Prepend) params.key else null,
                    limit = params.loadSize
            ).data

            LoadResult.Page(
                    data = data.children.map { it.data },
                    prevKey = data.before,
                    nextKey = data.after
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, RedditPost>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}