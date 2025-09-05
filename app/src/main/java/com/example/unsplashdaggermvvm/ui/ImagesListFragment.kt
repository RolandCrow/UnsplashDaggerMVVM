package com.example.unsplashdaggermvvm.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import com.example.unsplashdaggermvvm.R
import com.example.unsplashdaggermvvm.databinding.FragmentImagesListBinding
import com.example.unsplashdaggermvvm.model.ImagesResponse
import com.example.unsplashdaggermvvm.ui.adapters.ImagesAdapter
import com.example.unsplashdaggermvvm.ui.adapters.LoadingStateAdapter
import com.example.unsplashdaggermvvm.utils.toast
import com.example.unsplashdaggermvvm.viewmodels.ImagesListViewModel
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImagesListFragment: DaggerFragment(R.layout.fragment_images_list){
    private var hasFirstInitialCall = false
    private lateinit var binding: FragmentImagesListBinding
    private var job: Job? = null

    private val adapter = ImagesAdapter {imageResponse, imageView -> navigate(imageResponse,imageView)}

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ImagesListViewModel by viewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentImagesListBinding.bind(view)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setAdapter()
        if(!hasFirstInitialCall) {
            getImages()
            hasFirstInitialCall = true
        }
        val menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(
                menu: Menu,
                menuInflater: MenuInflater
            ) {
                menuInflater.inflate(R.menu.menu_main,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId) {
                    R.id.destination_search -> {
                        navigateSearch()
                        true
                    }
                    R.id.scroll_down -> {
                        scrollDown()
                        true
                    }
                    R.id.scroll_up -> {
                        scrollUp()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        })
    }

    private fun scrollUp() {
        binding.imagesList.scrollToPosition(0)
    }

    private fun scrollDown() {
        binding.imagesList.scrollToPosition(adapter.itemCount - 1)
    }
    private fun navigateSearch() {
        binding.root.findNavController().navigate(ImagesListFragmentDirections.toSearchFragment())
    }

    private fun getImages() {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getImage().collect {
                adapter.submitData(it)
            }
        }
    }

    private fun setAdapter() {
        binding.imagesList.adapter = adapter.withLoadStateFooter(
            LoadingStateAdapter {adapter.retry()}
        )
        adapter.addLoadStateListener {
            binding.progress.isVisible = it.refresh is LoadState.Loading
            if(it.refresh is LoadState.Error) {
                requireContext().toast("There was a problem fetching data")
            }
        }
    }

    private fun navigate(imagesResponse: ImagesResponse, imageView: ImageView) {
        val action = ImagesListFragmentDirections.toSingleImageFragment(imagesResponse)
        binding.root.findNavController().navigate(action)
    }
}