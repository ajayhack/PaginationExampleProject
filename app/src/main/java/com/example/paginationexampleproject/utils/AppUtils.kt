package com.example.paginationexampleproject.utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.example.paginationexampleproject.R

object AppUtils {
    private var progressDialog : Dialog? = null

    //region==========setup Progress Dialog:-
    fun setProgressDialog(activity : Activity) : Dialog? {
         progressDialog = Dialog(activity).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.item_progress_dialog)
            setCancelable(false)
        }
        return progressDialog as Dialog
    }
    //endregion
}