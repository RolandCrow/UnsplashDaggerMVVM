package com.example.unsplashdaggermvvm.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.unsplashdaggermvvm.R
import com.example.unsplashdaggermvvm.databinding.FragmentSearchBinding
import com.example.unsplashdaggermvvm.databinding.FragmentSingleImageBinding
import com.example.unsplashdaggermvvm.model.ImagesResponse
import com.example.unsplashdaggermvvm.utils.ERROR_DOWNLOADING
import com.example.unsplashdaggermvvm.utils.HAS_DOWNLOADED
import com.example.unsplashdaggermvvm.utils.HAS_SAVED
import com.example.unsplashdaggermvvm.utils.STARTING_DOWNLOAD
import com.example.unsplashdaggermvvm.utils.toast
import com.example.unsplashdaggermvvm.viewmodels.SingleImageViewModel
import com.example.unsplashdaggermvvm.viewmodels.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    private val storagePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        when {
            granted -> {
                chooseQualityDialog.show(childFragmentManager,null)
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE) -> {
                showUserWhyPermission()
            }
            else -> requireContext().toast("Permission denied")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSingleImageBinding.bind(view)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = null

        viewModel.imageString  = image?.urls?.regular
        binding.viewmodel = viewModel

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.download_menu,menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    chooseQualityDialog.show(childFragmentManager,null)
                } else {
                    storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
                return false
            }
        })
    }

    private fun startLoad() {
        makeSnackBar()
        viewModel.notifyDownLoading.observe(viewLifecycleOwner, {
            when(it) {
                STARTING_DOWNLOAD -> {
                    snackBar?.show()
                }
                ERROR_DOWNLOADING -> {
                    snackBar?.dismiss()
                }
                HAS_DOWNLOADED -> {
                    snackBar?.setText("Saving...")
                }
                HAS_SAVED -> {
                    snackBar?.dismiss()
                }
            }
        })
        lifecycleScope.launch(Dispatchers.Default) {
            image?.let { viewModel.getBitmapFromUrl(it.urls.full, it.id) }
        }
    }

    private fun makeSnackBar() {
        val parentLayout = requireActivity().findViewById<View>(android.R.id.content)
        snackBar = Snackbar.make(parentLayout, "Downloading...", Snackbar.LENGTH_INDEFINITE)
    }

    override fun downloadFull() {
        requireContext().toast("Downloading full resolution may take a while")
        startLoad()
    }

    override fun downloadRegular() {
        lifecycleScope.launch(Dispatchers.Default) {
            val bitmap = (binding.imageView.drawable).toBitmap()
            image?.id?.let { viewModel.saveImage(bitmap,it) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        snackBar?.dismiss()
    }

    private fun showUserWhyPermission() {
        AlertDialog.Builder(requireContext())
            .setTitle("Write storage permission needed")
            .setMessage("This permission is required inorder to save the image to the gallery")
            .setPositiveButton(
                "Okay"
            ) { _,_ ->
                storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }.create().show()
    }
}