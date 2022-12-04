package com.example.projeto.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.projeto.R
import com.google.android.material.snackbar.Snackbar

class BaseActivity : AppCompatActivity() {

        private lateinit var mProgressDialog: Dialog

        fun showErrorSnackBar(message: String, errorMessage: Boolean){
            val snackBar=
                Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
            val snackBarView = snackBar.view

            if(errorMessage){
                snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                        this@BaseActivity,
                        R.color.colorSnackBarError
                    )
                )
            }else{
                snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                        this@BaseActivity,
                        R.color.colorSnackBarSuccess
                    )
                )
            }
            snackBar.show()
    }


    //ESTA NO MAIN
    fun showProgressDialog(){
        mProgressDialog = Dialog(this)

        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.show()
        //mProgressDialog.tv_progress_text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

    }

    fun hideProgressDialog(){
        mProgressDialog.dismiss()

    }

}