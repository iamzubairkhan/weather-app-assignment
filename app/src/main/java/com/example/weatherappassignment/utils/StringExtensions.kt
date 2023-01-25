package com.example.weatherappassignment.utils

import java.util.Locale

/**
 * Replacement for Kotlin's deprecated `capitalize()` function.
 */
fun String.capitalized() = this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }