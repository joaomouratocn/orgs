package com.example.orgs.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.R
import com.example.orgs.dao.Dao
import com.example.orgs.model.Product
import java.math.BigDecimal

class FormAddProduct : AppCompatActivity() {
    private lateinit var edtName: EditText
    private lateinit var edtDesc: EditText
    private lateinit var edtPrice:EditText
    private lateinit var btnSave: Button

    private lateinit var dao:Dao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_add_product)

        initFields()

        val receivedProduct = intent.getParcelableExtra<Product>("product")

        if (receivedProduct != null) {
            edtName.setText(receivedProduct.name)
            edtDesc.setText(receivedProduct.description)
            edtPrice.setText(receivedProduct.price.toPlainString())
            btnSave.text = getString(R.string.str_update)
        }

        dao = Dao()

        configureButtonClick(receivedProduct)
    }

    private fun configureButtonClick(product: Product?) {
        btnSave.setOnClickListener {
            val name = edtName.text.toString()
            val desc = edtDesc.text.toString()
            val price = if (edtPrice.text.isBlank()) {
                BigDecimal.ZERO
            } else {
                BigDecimal(edtPrice.text.toString())
            }

            if (product != null){
               dao.updateProduct(Product(product.id, name, desc, price))
            }else{
                dao.addProduct(Product(name = name, description = desc, price = price))
            }
            finish()
        }
    }

    private fun initFields() {
        edtName = findViewById(R.id.edt_name)
        edtDesc = findViewById(R.id.edt_desc)
        edtPrice = findViewById(R.id.edt_price)
        btnSave = findViewById(R.id.btn_save)
    }
}