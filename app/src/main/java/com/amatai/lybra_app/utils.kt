package com.amatai.lybra_app

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Context.hideKeyboard(activity: Activity) {
    val inputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
}