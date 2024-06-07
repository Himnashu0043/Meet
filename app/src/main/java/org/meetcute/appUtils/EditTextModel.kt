package org.meetcute.appUtils

import androidx.databinding.ObservableField



data class EditTextModel(
    val text: ObservableField<String> = ObservableField(),
    val error: ObservableField<String?> = ObservableField()
)

data class EditTextCountryModel(
    val countryName: ObservableField<String> = ObservableField(),
    val error: ObservableField<String?> = ObservableField(),
    val countryCode: ObservableField<String> = ObservableField(),
    val countryFlag: ObservableField<Int> = ObservableField()
)