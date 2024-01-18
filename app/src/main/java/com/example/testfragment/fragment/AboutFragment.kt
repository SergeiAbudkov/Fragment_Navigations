package com.example.testfragment.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.testfragment.R
import com.example.testfragment.contract.HasCustomTitle
import com.example.testfragment.contract.navigator
import com.example.testfragment.databinding.FragmentAboutBinding

class AboutFragment : Fragment(), HasCustomTitle {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentAboutBinding.inflate(inflater,container,false).apply{
        versionNameTextView.text = "1.0"
        versionCodeTextView.text = "1"
        okButton.setOnClickListener {
            navigator().goBack()
        }

    }.root

    override fun getTitleRes(): Int = R.string.about


}