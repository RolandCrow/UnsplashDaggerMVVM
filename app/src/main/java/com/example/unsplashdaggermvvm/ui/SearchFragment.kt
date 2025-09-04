package com.example.unsplashdaggermvvm.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.unsplashdaggermvvm.R
import com.example.unsplashdaggermvvm.databinding.FragmentSearchBinding
import com.example.unsplashdaggermvvm.model.ImagesResponse
import com.example.unsplashdaggermvvm.ui.adapters.ImagesAdapter
import com.example.unsplashdaggermvvm.ui.adapters.LoadingStateAdapter
import com.example.unsplashdaggermvvm.utils.hideSoftKeyboard
import com.example.unsplashdaggermvvm.utils.showKeyBoard
import com.example.unsplashdaggermvvm.utils.toast
import com.example.unsplashdaggermvvm.viewmodels.SearchViewModel
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFragment: DaggerFragment(R.layout.fragment_search) {

    private var job: Job? = null
    private var hasFirstInitiatedCall = false
    private lateinit var binding: FragmentSearchBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val adapter = ImagesAdapter { imageResponse, imageView ->
        navigate(imageResponse,imageView)
    }

    private val viewModel: SearchViewModel by viewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setAdapter()
        setSearchView()
        binding.searchView.requestFocus()
        if(!hasFirstInitiatedCall) {
            viewModel.currentQuery()?.let {
                searchImage(it)
            }
            hasFirstInitiatedCall = true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setSearchView() {
        binding.searchView.setOnTouchListener { v,_ ->
            v.isFocusableInTouchMode = true
            false
        }
        binding.searchView.setOnEditorActionListener(TextView.OnEditorActionListener { _,actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchImage(binding.searchView.text.toString().trim())
                return@OnEditorActionListener true
            }
            false
        })
        binding.searchView.setOnTouchListener(View.OnTouchListener {_, event ->
            val drawableRight = 2
            if(event.action == MotionEvent.ACTION_UP) {
                if(event.rawX >= binding.searchView.right - binding.searchView.compoundDrawables[drawableRight].bounds.width()) {
                    requireContext().showKeyBoard(binding.searchView)
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private fun searchImage(query: String) {
        if(query.isEmpty()) return
        requireActivity().hideSoftKeyboard()
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.searchImage(query).collect {
                adapter.submitData(it)
            }
        }
    }

    private fun setAdapter() {
        binding.imagesList.adapter = adapter.withLoadStateFooter(
            LoadingStateAdapter  {adapter.retry()}
        )
        adapter.addLoadStateListener {
            binding.progress.isVisible = it.refresh is LoadState.Loading
            if(it.refresh is LoadState.Error) {
                requireContext().toast("There was a problem fetch data")
            }
        }
    }

    private fun navigate(imagesResponse: ImagesResponse, imageView: ImageView) {

    }
}