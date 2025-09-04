package com.example.unsplashdaggermvvm.ui

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.unsplashdaggermvvm.R
import com.example.unsplashdaggermvvm.databinding.FragmentSearchBinding
import com.example.unsplashdaggermvvm.databinding.FragmentSingleImageBinding
import com.example.unsplashdaggermvvm.model.ImagesResponse
import com.example.unsplashdaggermvvm.viewmodels.SingleImageViewModel
import com.example.unsplashdaggermvvm.viewmodels.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class SingleImageFragment: DaggerFragment(R.layout.fragment_single_image), ChooseQualityDialog.ChooseInterface {
    private var snackBar: Snackbar? = null
    private lateinit var binding: FragmentSingleImageBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SingleImageViewModel by viewModels {
        viewModelFactory
    }

    private var image: ImagesResponse? = null
    private val chooseQualityDialog = ChooseQualityDialog()


    override fun downloadFull() {

    }

    override fun downloadRegular() {

    }

}