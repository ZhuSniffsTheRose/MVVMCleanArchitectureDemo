package com.zhu.mvvmdemo.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhu.mvvmdemo.R
import com.zhu.mvvmdemo.databinding.FragmentListBinding
import com.zhu.mvvmdemo.ui.adapters.HeaderAdapter
import com.zhu.mvvmdemo.ui.adapters.ItemsAdapter
import com.zhu.mvvmdemo.data.ContentResponse
import com.zhu.mvvmdemo.util.viewBindings
import com.zhu.mvvmdemo.utils.autoCleared
import com.zhu.mvvmdemo.utils.toast

/**
 * A fragment representing a list of Items.
 */
class ListFragment : Fragment(R.layout.fragment_list) {

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
                ListFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }

    private var concatAdapter by autoCleared<ConcatAdapter>()

    private val binding by viewBindings(FragmentListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Set the adapter
        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            val headerAdapter = HeaderAdapter()
            headerAdapter.updateCount(1024)
            val flowersAdapter = ItemsAdapter { flower -> adapterOnClick(flower) }.also {
                it.submitList(ContentResponse.ITEMS)
            }
            concatAdapter = ConcatAdapter(headerAdapter, flowersAdapter)
            adapter = concatAdapter
        }
    }

    private fun adapterOnClick(it: ContentResponse.PlaceholderItem) {
        requireContext().toast("cliecked")
    }
}