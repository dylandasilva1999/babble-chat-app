package com.example.noted

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import androidx.core.content.ContextCompat
import com.example.babble.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
    // Remove OnCreate function

    //Include validation
    fun showErrorSnackBar(message: String, errorMessage: Boolean) {

        //Make SnackBar
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this, R.color.design_default_color_error
                )
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this, R.color.design_default_color_secondary
                )
            )
        }
        snackBar.show()
    }
}