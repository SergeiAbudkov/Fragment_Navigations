package com.example.testfragment.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testfragment.R
import com.example.testfragment.contract.HasCustomTitle
import com.example.testfragment.contract.navigator
import com.example.testfragment.databinding.FragmentBoxBinding


class BoxFragment : Fragment(), HasCustomTitle {

    private lateinit var binding: FragmentBoxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBoxBinding.inflate(layoutInflater,container,false)

        binding.toMainMenuButton.setOnClickListener {
            navigator().goToMenu()
        }

        return binding.root
    }

    companion object {

    }

    override fun getTitleRes(): Int = R.string.box
}