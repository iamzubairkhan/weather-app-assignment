package com.example.weatherappassignment.utils

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

fun interface ResourceProvider {
    fun getString(@StringRes stringResId: Int, vararg formatArgs: Any): String
}

class ResourceProviderImpl @Inject constructor(@ApplicationContext private val context: Context) : ResourceProvider {
    override fun getString(@StringRes stringResId: Int, vararg formatArgs: Any): String {
        return context.getString(stringResId, *formatArgs)
    }
}