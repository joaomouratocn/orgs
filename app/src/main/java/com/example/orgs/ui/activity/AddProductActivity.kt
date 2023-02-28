package com.example.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.orgs.R
import com.example.orgs.data.room.database.AppDataBase
import com.example.orgs.databinding.ActivityFormAddProductBinding
import com.example.orgs.model.Product
import com.example.orgs.ui.dialog.Dialogs
import com.example.orgs.util.*
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class AddProductActivity : AppCompatActivity() {
    private var url: String? = null
    private var receivedId: Int = 0

    private val productDao by lazy {
        AppDataBase.getInstance(this).productDao()
    }

    private val format: NumberFormat by lazy {
        NumberFormat.getCurrencyInstance(Locale("pt","br"))
    }

    private val binding:ActivityFormAddProductBinding by lazy {
        ActivityFormAddProductBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = getString(R.string.str_register_products)

        checkReceivedProduct()

        configureButtonClick()

        configureImageItem()
    }

    private fun configureImageItem() {
        binding.imgItem.setOnClickListener {
            openDialog(url)
        }
    }

    private fun checkReceivedProduct(){
        receivedId = intent.getIntExtra(SEND_ID_KEY, 0)
        productDao.getProduct(receivedId)?.let {
            title = getString(R.string.str_update_products)
            loadFields(it.toProduct())
            url = it.image
        }

    }

    private fun loadFields(product: Product) {
        binding.edtName.setText(product.name)
        binding.edtDesc.setText(product.description)
        binding.edtPrice.setText(format.format(product.price))
        binding.btnSave.text = getString(R.string.str_update)
        binding.imgItem.loadImage(product.image)
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
                id = receivedId,
                name = binding.edtName.text.toString(),
                description = binding.edtDesc.text.toString(),
                price = if (binding.edtPrice.text?.isBlank() == true){BigDecimal.ZERO}
                else{BigDecimal(binding.edtPrice.text.toString())},
                image = url
            )

            productDao.insertProduct(product.toProductEntity())
            finish()
        }
    }
}