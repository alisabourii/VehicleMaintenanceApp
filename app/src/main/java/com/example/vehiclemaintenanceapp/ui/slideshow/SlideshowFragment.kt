package com.example.vehiclemaintenanceapp.ui.slideshow

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vehiclemaintenanceapp.R
import com.example.vehiclemaintenanceapp.databinding.FragmentSlideshowBinding
import java.io.File

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageButton: ImageButton
    private lateinit var saveButton: Button

    private val years = listOf("2017", "2018", "2019", "2020","2021", "2022", "2023", "2024","2025")

    private val carData = mapOf(
        "Otomobil" to mapOf(
            "Toyota" to mapOf(
                "4Runner" to years,
                "Corolla" to years,
                "Corolla Cross" to years,
                "Corolla Hatchback" to years,
                "C-HR" to years,
                "RAV4" to years,
                "Yaris" to years,
                "Yaris Cross" to years,
                "Land Cruiser Prado" to years,
                "HILUX" to years,
                "Proace City" to years,
                "Proace City Cargo" to years,
                "Proace Verso" to years,
                "Proace Cargo" to years,
            ),
            "BMW" to mapOf(
                "I Series" to years,
                "X Series" to years,
                "M Series" to years,
                "1 Series" to years,
                "2 Series" to years,
                "3 Series" to years,
                "4 Series" to years,
                "5 Series" to years,
                "7 Series" to years,
                "8 Series" to years,
                "Z4 Series" to years
            )

        ),
        "SUV" to mapOf(
            "Jeep" to mapOf(
                "Cherokee" to years
            )
        ),
        "Ticari" to mapOf(
            "VW" to mapOf(
                "Caddy" to years
            )
        ),
        "Motor" to mapOf(
            "Bajaj" to mapOf(
                "Dominar" to years
            )
        )


    )

    // Galeriden resim seçmek için launcher
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                imageButton.setImageURI(it)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)

        imageButton = binding.imageButton
        imageButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        setupSpinners()

        return binding.root
    }

    private fun saveCurrentSelection() {
        val type  = binding.typeSpinner.selectedItem?.toString() ?: return
        val brand = binding.brandSpinner.selectedItem?.toString() ?: return
        val model = binding.modelSpinner.selectedItem?.toString() ?: return
        val year  = binding.yearSpinner.selectedItem?.toString() ?: return
        val engine = binding.engineSpinner.selectedItem?.toString() ?: ""

        // 🔴 DİKKAT: Fragment'ta Context ALWAYS -> requireContext()
        CsvStore.appendRow(
            requireContext(),
            listOf(type, brand, model, year, engine)
        )

        Toast.makeText(requireContext(), "Kaydedildi ✅", Toast.LENGTH_SHORT).show()
        Log.d("CSV", "Path: " + CsvStore.filePath(requireContext()))
    }

    private fun loadAllRows() {
        val rows = CsvStore.readRows(requireContext())
        // Örnek: Logcat’te göster
        rows.forEach { row ->
            Log.d("CSV", row.joinToString(" | "))
        }
        Toast.makeText(requireContext(), "Toplam satır: ${rows.size}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun setupSpinners() {
        val typeSpinner = binding.typeSpinner
        val brandSpinner = binding.brandSpinner
        val modelSpinner = binding.modelSpinner
        val yearSpinner = binding.yearSpinner
        val engineSpinner = binding.engineSpinner
        val saveButton = binding.button

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




        saveButton.setOnClickListener{
            saveCurrentSelection()   // Aşağıdaki fonksiyon

        }

    }
}
