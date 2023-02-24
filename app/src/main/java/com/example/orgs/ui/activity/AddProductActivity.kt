package com.example.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.orgs.R
import com.example.orgs.dao.Dao
import com.example.orgs.databinding.ActivityFormAddProductBinding
import com.example.orgs.model.Product
import com.example.orgs.ui.dialog.Dialogs
import com.example.orgs.util.loadImage
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class AddProductActivity : AppCompatActivity() {
    private var url: String? = null
    private var receivedId: Int? = null
    private val format: NumberFormat by lazy {
        NumberFormat.getCurrencyInstance(Locale("pt","br"))
    }

    private val binding:ActivityFormAddProductBinding by lazy {
        ActivityFormAddProductBinding.inflate(layoutInflater)
    }
    private lateinit var dao:Dao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = getString(R.string.str_register_products)

        val receivedProduct:Product? = intent.getParcelableExtra("product")

        receivedProduct?.let {product ->
            title = getString(R.string.str_update_products)
            binding.edtName.setText(product.name)
            binding.edtDesc.setText(product.description)
            binding.edtPrice.setText(format.format(product.price))
            binding.btnSave.text = getString(R.string.str_update)
            binding.imgItem.loadImage(product.image)
            receivedId = product.id
            url = product.image
        }

        dao = Dao()
        configureButtonClick()

        binding.imgItem.setOnClickListener {
            openDialog(receivedProduct?.image)
        }
    }

    private fun openDialog(image: String?) {
        Dialogs(this).loadImage(image) {
            binding.imgItem.load(it)
            url = it
        }
    }

    private fun configureButtonClick() {
        binding.btnSave.setOnClickListener {
            val product = Product(
                name = binding.edtName.text.toString(),
                description = binding.edtDesc.text.toString(),
                price = if (binding.edtPrice.text?.isBlank() == true){BigDecimal.ZERO}
                else{BigDecimal(binding.edtPrice.text.toString())},
                image = url
            )

            if (receivedId != null){
                product.id = receivedId as Int
                dao.updateProduct(product)
            }else{
                dao.addProduct(product)
            }
            finish()
        }
    }
}