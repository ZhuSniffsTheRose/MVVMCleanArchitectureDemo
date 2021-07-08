package com.zhu.mvvmdemo.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhu.mvvmdemo.R
import com.zhu.mvvmdemo.databinding.FragmentPagingListBinding
import com.zhu.mvvmdemo.ui.adapters.PostsAdapter
import com.zhu.mvvmdemo.ui.adapters.PostsLoadStateAdapter
import com.zhu.mvvmdemo.ui.viewmodels.PagingViewModel
import com.zhu.mvvmdemo.util.viewBindings
import kotlinx.coroutines.flow.collectLatest

/**
 *
 *   Paging operate in three layers of app
 *   1. The repository layer :  PagingSource(数据源 response from server or DB) + PagingMediator（数据源: response from server with db)
 *   2. The ViewModel layer : Pager，
 *   3. The UI layer : PagingDataAdapter
 *
 */
class PagingFragment : Fragment(R.layout.fragment_paging_list) {

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
                PagingFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }

    private val viewModel by viewModels<PagingViewModel>()

    private val binding by viewBindings(FragmentPagingListBinding::bind)


    private lateinit var postsAdapter: PostsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()

        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = postsAdapter
        }

        viewModel.updateKeyWords("androiddev")

        lifecycleScope.launchWhenCreated {
            postsAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.swipeRefresh.isRefreshing = loadStates.mediator?.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.posts.collectLatest {
                postsAdapter.submitData(it)
            }
        }
    }

    private fun initAdapter() {
        postsAdapter = PostsAdapter().also {
            it.withLoadStateHeaderAndFooter(
                    header = PostsLoadStateAdapter(it),
                    footer = PostsLoadStateAdapter(it)
            )
        }

        postsAdapter.addLoadStateListener { loadState ->
            val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                    ?: loadState.refresh as? LoadState.Error

            Log.d("heiheihei", "PagingFragment initAdapter:---> $errorState")
            errorState?.let {
                Toast.makeText(
                        requireContext(),
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                ).show()
            }

        }
    }
}
