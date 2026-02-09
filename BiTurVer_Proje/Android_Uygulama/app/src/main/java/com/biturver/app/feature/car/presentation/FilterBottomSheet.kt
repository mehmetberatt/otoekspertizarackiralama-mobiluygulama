package com.biturver.app.feature.car.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.biturver.app.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.slider.RangeSlider
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class FilterBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.bottomsheet_filters, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actCity = view.findViewById<MaterialAutoCompleteTextView>(R.id.actCitySheet)
        val actType = view.findViewById<MaterialAutoCompleteTextView>(R.id.actTypeSheet)
        val actSort = view.findViewById<MaterialAutoCompleteTextView>(R.id.actSortSheet)
        val slider = view.findViewById<RangeSlider>(R.id.sliderPriceSheet)

        actCity.setSimpleItems(resources.getStringArray(R.array.turkey_cities))
        actType.setSimpleItems(resources.getStringArray(R.array.car_types))
        actSort.setSimpleItems(resources.getStringArray(R.array.sort_options))

        val st = requireArguments()

        actCity.setText(st.getString(K_CITY, ""), false)
        actType.setText(st.getString(K_TYPE, ""), false)
        actSort.setText(st.getString(K_SORT, "Fiyat (artan)"), false)

        slider.valueFrom = 500f
        slider.valueTo = 6000f
        val minPrice = st.getFloat(K_PRICE_MIN, 500f)
        val maxPrice = st.getFloat(K_PRICE_MAX, 6000f)
        slider.values = listOf(minPrice, maxPrice)

        fun checkChip(id: Int, checked: Boolean) {
            if(checked) view.findViewById<Chip>(id).isChecked = true
        }

        val tr = st.getString(K_TR, null)
        checkChip(R.id.chipAutoSheet, tr == "Otomatik")
        checkChip(R.id.chipManualSheet, tr == "Manuel")

        val fuel = st.getString(K_FUEL, null)
        checkChip(R.id.chipGasolineSheet, fuel == "Benzin")
        checkChip(R.id.chipDieselSheet, fuel == "Dizel")
        checkChip(R.id.chipElectricSheet, fuel == "Elektrik")

        val btnApply = view.findViewById<MaterialButton>(R.id.btnApplySheet)
        val btnClear = view.findViewById<MaterialButton>(R.id.btnClearSheet)

        btnClear.setOnClickListener {
            actCity.setText("", false)
            actType.setText("", false)
            actSort.setText("Fiyat (artan)", false)
            slider.values = listOf(500f, 6000f)

            view.findViewById<Chip>(R.id.chipAutoSheet).isChecked = false
            view.findViewById<Chip>(R.id.chipManualSheet).isChecked = false
            view.findViewById<Chip>(R.id.chipGasolineSheet).isChecked = false
            view.findViewById<Chip>(R.id.chipDieselSheet).isChecked = false
            view.findViewById<Chip>(R.id.chipElectricSheet).isChecked = false
        }

        btnApply.setOnClickListener {
            val resultBundle = Bundle().apply {
                putString(K_CITY, actCity.text.toString())
                putString(K_TYPE, actType.text.toString())
                putString(K_SORT, actSort.text.toString())

                putFloat(K_PRICE_MIN, slider.values[0])
                putFloat(K_PRICE_MAX, slider.values[1])

                putString(K_TR, when {
                    view.findViewById<Chip>(R.id.chipAutoSheet).isChecked -> "Otomatik"
                    view.findViewById<Chip>(R.id.chipManualSheet).isChecked -> "Manuel"
                    else -> null
                })

                putString(K_FUEL, when {
                    view.findViewById<Chip>(R.id.chipGasolineSheet).isChecked -> "Benzin"
                    view.findViewById<Chip>(R.id.chipDieselSheet).isChecked -> "Dizel"
                    view.findViewById<Chip>(R.id.chipElectricSheet).isChecked -> "Elektrik"
                    else -> null
                })
            }

            parentFragmentManager.setFragmentResult(REQ_KEY, resultBundle)
            dismiss()
        }
    }

    companion object {
        const val REQ_KEY = "filters_result"

        const val K_CITY = "city"
        const val K_TYPE = "type"
        const val K_SORT = "sort"
        const val K_TR = "tr"
        const val K_FUEL = "fuel"
        const val K_PRICE_MIN = "price_min"
        const val K_PRICE_MAX = "price_max"

        fun newInstance(currentState: Bundle): FilterBottomSheet {
            return FilterBottomSheet().apply { arguments = currentState }
        }
    }
}