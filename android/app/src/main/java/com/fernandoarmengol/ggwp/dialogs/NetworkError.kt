package com.fernandoarmengol.ggwp.dialogs

import android.app.AlertDialog
import android.content.Context

object NetworkError {
    fun dialogNetworkError(context: Context, text: String){
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setMessage(text)
        }
        builder.setCancelable(false)
        builder.show()
    }
}