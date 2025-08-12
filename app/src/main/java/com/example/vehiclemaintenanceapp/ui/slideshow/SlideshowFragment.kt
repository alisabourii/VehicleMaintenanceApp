package com.example.vehiclemaintenanceapp.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vehiclemaintenanceapp.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    private lateinit var typeSpinner: Spinner
    private lateinit var brandSpinner: Spinner
    private lateinit var modelSpinner: Spinner
    private lateinit var yearSpinner: Spinner


    // This property is only valid between onCreateView and
    // onDestroyView.
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
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}