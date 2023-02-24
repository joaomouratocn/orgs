package com.example.orgs.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.orgs.R
import com.example.orgs.databinding.DialogLoadImageBinding
import com.example.orgs.util.loadImage

class Dialogs(private val context: Context){
    fun loadImage(
        url: String? = null,
        uploadedImage: (imageUrl: String) -> Unit
    ){
        DialogLoadImageBinding.inflate(LayoutInflater.from(context)).apply {
            url.let {
                imgProduct.loadImage(url)
                edtUrl.setText(url)

                btnLoad.setOnClickListener {
                    val url: String = edtUrl.text.toString()
                    imgProduct.loadImage(url)
                }

                AlertDialog.Builder(context)
                    .setView(root)
                    .setPositiveButton(context.getString(R.string.str_confirm)) { _, _ ->
                        val url = edtUrl.text.toString()
                        uploadedImage(url)
                    }
                    .setNegativeButton(context.getString(R.string.str_cancel)) { _, _ ->

                    }
                    .show()
            }
        }
    }

    fun confirmDelete(yesDelete:() -> Unit){
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.str_delete))
            .setMessage(context.getString(R.string.str_confirm_delete))
            .setPositiveButton(context.getString(R.string.str_confirm)){_, _ ->
                yesDelete()
            }
            .setNegativeButton(context.getString(R.string.str_cancel)){_,_ ->

            }
            .show()
    }
}