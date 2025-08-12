package com.example.vehiclemaintenanceapp.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vehiclemaintenanceapp.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!

    private val carData = mapOf(
        "Otomobil" to mapOf(
            "BMW" to mapOf(
                "3 Series" to listOf("2020", "2021", "2022"),
                "X5" to listOf("2018", "2019", "2020")
            ),
            "Toyota" to mapOf(
                "Corolla" to listOf("2017", "2018", "2019", "2020")
            )
        ),
        "SUV" to mapOf(
            "Jeep" to mapOf(
                "Cherokee" to listOf("2015", "2016", "2017")
            )
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupSpinners()

        return root
    }

    private fun setupSpinners() {
        val typeSpinner = binding.typeSpinner
        val brandSpinner = binding.brandSpinner
        val modelSpinner = binding.modelSpinner
        val yearSpinner = binding.yearSpinner

        val types = carData.keys.toList()
        typeSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)

        // Tür seçildiğinde markaları doldur
        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedType = types[position]
                val brands = carData[selectedType]?.keys?.toList() ?: emptyList()
                brandSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, brands)

                // Marka değiştiğinde modeli ve yılı sıfırla
                modelSpinner.adapter = null
                yearSpinner.adapter = null
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        brandSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedType = typeSpinner.selectedItem?.toString() ?: return
                val selectedBrand = brandSpinner.selectedItem?.toString() ?: return
                val models = carData[selectedType]?.get(selectedBrand)?.keys?.toList() ?: emptyList()
                modelSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, models)

                // Yılı sıfırla
                yearSpinner.adapter = null
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        modelSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedType = typeSpinner.selectedItem?.toString() ?: return
                val selectedBrand = brandSpinner.selectedItem?.toString() ?: return
                val selectedModel = modelSpinner.selectedItem?.toString() ?: return
                val years = carData[selectedType]?.get(selectedBrand)?.get(selectedModel) ?: emptyList()
                yearSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
