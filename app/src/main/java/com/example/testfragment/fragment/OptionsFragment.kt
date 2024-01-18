package com.example.testfragment.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.testfragment.Options
import com.example.testfragment.R
import com.example.testfragment.contract.HasCustomTitle
import com.example.testfragment.contract.navigator
import com.example.testfragment.databinding.FragmentOptionsBinding
import java.lang.IllegalStateException


class OptionsFragment : Fragment(), HasCustomTitle {
    private lateinit var binding: FragmentOptionsBinding
    private lateinit var options: Options
    private lateinit var adapter: ArrayAdapter<BoxCountItem>
    private lateinit var list: List<BoxCountItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        options = savedInstanceState?.getParcelable(KEY_BUNDLE) ?: arguments?.getParcelable(
            KEY_RESULT
        ) ?: throw IllegalStateException("Я тута")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOptionsBinding.inflate(layoutInflater, container, false)
        setupSpinner()
        setupCheckBox()
        updateUi()

        binding.cancelButton.setOnClickListener {
            navigator().goBack()
        }

        binding.confirmButton.setOnClickListener {
            onConfirmedPressed()
        }
        return binding.root
    }

    private fun onConfirmedPressed() {
        navigator().setResult(options)
        navigator().goBack()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_BUNDLE, options)
        super.onSaveInstanceState(outState)
    }

    fun setupSpinner() {
        list = (1..6).map { BoxCountItem(it, resources.getQuantityString(R.plurals.boxes, it, it)) }
        adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            list
        )
        binding.boxCountSpinner.adapter = adapter
        binding.boxCountSpinner.setSelection(2)
        binding.boxCountSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = list[position].count
                    options = options.copy(boxCount = selectedItem)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }
    }

    private fun setupCheckBox() {
        binding.enableTimerCheckBox.setOnClickListener {
            options = options.copy(isTimerEnabled = binding.enableTimerCheckBox.isChecked)
        }
    }

    fun updateUi() {
        binding.enableTimerCheckBox.isChecked = options.isTimerEnabled

        val currentIndex = list.indexOfFirst { it.count == options.boxCount }
        binding.boxCountSpinner.setSelection(currentIndex)
    }

    class BoxCountItem(
        val count: Int,
        val optionTitle: String
    ) {
        override fun toString(): String {
            return optionTitle
        }
    }

    companion object {
        private const val KEY_BUNDLE = "KEY_BUNDLE"
        private const val KEY_RESULT = "KEY_RESULT"

        @JvmStatic
        fun newInstance(options: Options) =
            OptionsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_RESULT, options)
                }
            }
    }

    override fun getTitleRes(): Int = R.string.options
}