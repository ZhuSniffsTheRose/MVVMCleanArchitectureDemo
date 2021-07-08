package com.zhu.mvvmdemo.ui.viewmodels

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zhu.mvvmdemo.data.RedditPost
import com.zhu.mvvmdemo.ui.repository.byPage.PageKeyedRedditPostPagingSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

/**
 * @author Zhu
 * @date 2021/7/5 10:45
 * @desc
 */
class PagingViewModel : ViewModel() {

    private val keyWords = MutableLiveData<String>("")

    val posts = Pager(PagingConfig(30)) {
        PageKeyedRedditPostPagingSource("it")
    }.flow.cachedIn(viewModelScope)

    fun updateKeyWords(kw: String) {
        keyWords.value = kw
    }
}