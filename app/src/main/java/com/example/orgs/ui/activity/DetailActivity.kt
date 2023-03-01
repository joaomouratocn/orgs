package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.data.room.dao.ProductDao
import com.example.orgs.data.room.database.AppDataBase
import com.example.orgs.databinding.ActivityDetailBinding
import com.example.orgs.model.Product
import com.example.orgs.ui.dialog.Dialogs
import com.example.orgs.util.SEND_ID_KEY
import com.example.orgs.util.loadImage
import com.example.orgs.util.toProduct
import com.example.orgs.util.toProductEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

//Activity ultilizando lifecycleScope para fazer chamadas assincronas
class DetailActivity : AppCompatActivity() {
    val job = Job()
    private lateinit var receivedProduct : Product

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }
    private val format: NumberFormat by lazy {
        NumberFormat.getCurrencyInstance(Locale("pt", "br"))
    }

    private val productDao: ProductDao by lazy {
        AppDataBase.getInstance(this).productDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = getString(R.string.str_detail)
        loadProduct()
    }

    override fun onResume() {
        super.onResume()
        loadProduct()
    }

    private fun loadProduct() {
        val receivedId = intent.getIntExtra(SEND_ID_KEY, 0)
        lifecycleScope.launch() {
            productDao.getProduct(receivedId).collect{
                it?.toProduct()?.let {product ->
                    receivedProduct = product
                    loadFields()
                }?:finish()
            }
        }
    }

    private fun loadFields() {
        receivedProduct.let {product ->
            binding.imgImage.loadImage(product.image)
            binding.txvPrice.text = format.format(product.price)
            binding.txvName.text = product.name
            binding.txvDesc.text = product.description
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options_product, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (::receivedProduct.isInitialized){
            when (item.itemId) {
                R.id.menu_edit -> {
                    openAddProductAct()
                }
                R.id.menu_delete -> {
                    Dialogs(this).confirmDelete {
                        deleteProduct()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteProduct() {
        lifecycleScope.launch {
            productDao.deleteProduct(receivedProduct.toProductEntity())
            finish()
        }
    }

    private fun openAddProductAct() {
        val intent = Intent(this, AddProductActivity::class.java)
        intent.putExtra(SEND_ID_KEY, receivedProduct.id)
        startActivity(intent)
    }
}