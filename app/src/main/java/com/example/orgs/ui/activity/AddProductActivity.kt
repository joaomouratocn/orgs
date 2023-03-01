package com.example.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.orgs.R
import com.example.orgs.data.room.database.AppDataBase
import com.example.orgs.databinding.ActivityFormAddProductBinding
import com.example.orgs.model.Product
import com.example.orgs.ui.dialog.Dialogs
import com.example.orgs.util.SEND_ID_KEY
import com.example.orgs.util.loadImage
import com.example.orgs.util.toProduct
import com.example.orgs.util.toProductEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import okhttp3.internal.proxy.NullProxySelector
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

//Activity ultilizando coroutines sem lifecycle para fazer chamadas assincronas (CRIANDO ESCOPO)
class AddProductActivity : AppCompatActivity() {
    val job = Job()
    private var url: String? = null
    private var receivedId: Int = 0

    private val productDao by lazy {
        AppDataBase.getInstance(this).productDao()
    }

    private val format: NumberFormat by lazy {
        NumberFormat.getCurrencyInstance(Locale("pt", "br"))
    }

    private val binding: ActivityFormAddProductBinding by lazy {
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

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun configureImageItem() {
        binding.imgItem.setOnClickListener {
            openDialog(url)
        }
    }

    private fun checkReceivedProduct() {
        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.i("Error", "Exception Handler $throwable")
            Toast.makeText(this, "Error Handler", Toast.LENGTH_LONG).show()
        }
        receivedId = intent.getIntExtra(SEND_ID_KEY, 0)
        MainScope().launch(handler + job) {
            val product = withContext(IO) {
                productDao.getProductId(receivedId)?.toProduct()
            }
            product?.let {
                title = getString(R.string.str_update_products)
                loadFields(product)
                url = product.image
            }
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
                price = if (binding.edtPrice.text?.isBlank() == true) {
                    BigDecimal.ZERO
                } else {
                    BigDecimal(binding.edtPrice.text.toString())
                },
                image = url
            )

            MainScope().launch {
                withContext(IO) {
                    productDao.insertProduct(product.toProductEntity())
                }
                finish()
            }
        }
    }
}