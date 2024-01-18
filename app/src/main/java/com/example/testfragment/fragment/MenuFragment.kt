package com.example.testfragment.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testfragment.Options
import com.example.testfragment.contract.navigator
import com.example.testfragment.databinding.FragmentMenuBinding
import java.lang.IllegalStateException

class MenuFragment : Fragment() {

    private lateinit var options: Options

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options =
            savedInstanceState?.getParcelable(STATE_OPTIONS_BUNDLE) ?: arguments?.getParcelable(
                STATE_OPTIONS_ARGUMENTS
            ) ?: Options.DEFAULT ?: throw IllegalStateException("Привет")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentMenuBinding.inflate(layoutInflater, container, false).apply {

        listenResult()

        openBoxButton.setOnClickListener {
            navigator().openBoxSelected(options)
        }

        optionsButton.setOnClickListener {
        navigator().openOptions(options)
        }

        aboutButton.setOnClickListener {
            navigator().openAbout()
        }

        exitButton.setOnClickListener {
            navigator().goBack()
        }

    }.root

    private fun listenResult() {
        navigator().listenerResult(Options::class.java,viewLifecycleOwner) {
        this.options = it
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(STATE_OPTIONS_BUNDLE, options)
        super.onSaveInstanceState(outState)
    }

    companion object {

        private const val STATE_OPTIONS_ARGUMENTS = "STATE_OPTIONS_ARGUMENTS"
        private const val STATE_OPTIONS_BUNDLE = "STATE_OPTIONS_BUNDLE"

        @JvmStatic
        fun newInstance(options: Options): MenuFragment {
            val fragment = MenuFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(STATE_OPTIONS_ARGUMENTS, options)
            }
            return fragment
        }
    }

}

