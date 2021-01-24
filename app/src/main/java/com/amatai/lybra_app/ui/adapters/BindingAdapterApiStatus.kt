package com.amatai.lybra_app.ui.adapters

import android.view.View
import android.widget.RelativeLayout
import androidx.databinding.BindingAdapter

enum class ApiStatus { LOADING, ERROR, DONE }


@BindingAdapter("ApiStatus")
fun bindStatus(statusRelativeLayout: RelativeLayout, status: ApiStatus?) {
    when (status) {
        ApiStatus.LOADING -> {
            statusRelativeLayout.visibility = View.VISIBLE
        }
        ApiStatus.ERROR -> {
            statusRelativeLayout.visibility = View.VISIBLE
        }
        ApiStatus.DONE -> {
            statusRelativeLayout.visibility = View.INVISIBLE
        }
    }
}