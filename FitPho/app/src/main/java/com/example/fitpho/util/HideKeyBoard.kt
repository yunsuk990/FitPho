package com.example.fitpho.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    requireActivity().currentFocus?.let { focusView ->
        inputMethodManager.hideSoftInputFromWindow(
            focusView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}