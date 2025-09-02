package com.example.unsplashdaggermvvm.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.unsplashdaggermvvm.R
import com.example.unsplashdaggermvvm.databinding.ImageQualityLayoutBinding

class ChooseQualityDialog: DialogFragment() {
    private lateinit var binding: ImageQualityLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.image_quality_layout, container,false)
        binding.apply {
            full.setOnClickListener {
                mCallback?.downloadFull()
                dialog?.dismiss()
            }
            regular.setOnClickListener {
                mCallback?.downloadRegular()
                dialog?.dismiss()
            }
        }
        return binding.root
    }

    var mCallback: ChooseInterface? = null

    interface ChooseInterface {
        fun downloadFull()
        fun downloadRegular()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mCallback = try {
            parentFragment as ChooseInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(
                parentFragment.toString() + "must be implement ChooseInterface"
            )
        }
    }
}