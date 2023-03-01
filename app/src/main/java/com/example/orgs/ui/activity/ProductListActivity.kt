package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.data.room.database.AppDataBase
import com.example.orgs.databinding.ActivityFruitListBinding
import com.example.orgs.model.Product
import com.example.orgs.ui.adapter.ProductAdapter
import com.example.orgs.ui.dialog.Dialogs
import com.example.orgs.util.SEND_ID_KEY
import com.example.orgs.util.toListProduct
import com.example.orgs.util.toProductEntity
import kotlinx.coroutines.*

//ultilizando lifecycleScope para chamadas assincronas
class ProductListActivity : AppCompatActivity(), ProductAdapter.Onclick {
    private val adapter by lazy {
        ProductAdapter(listener = this)
    }

    private val productDao by lazy {
        AppDataBase.getInstance(this).productDao()
    }

    private val binding by lazy {
        ActivityFruitListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureRecycler()
        configureFloat()

        lifecycleScope.launch {
            productDao.getAllProductsWhitOutOrder().collect {
                updateRecycle(it.toListProduct())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_order_items_recycle, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        makeSelectType(item)
        return super.onOptionsItemSelected(item)
    }

    private fun makeSelectType(item: MenuItem){
        lifecycleScope.launch {
            val products:List<Product> = when (item.itemId) {
                R.id.menu_name_asc -> productDao.getAllProductsNameAsc().toListProduct()
                R.id.menu_name_desc -> productDao.getAllProductsNameDesc().toListProduct()
                R.id.menu_desc_asc -> productDao.getAllProductsDescAsc().toListProduct()
                R.id.menu_desc_desc -> productDao.getAllProductsDescDesc().toListProduct()
                R.id.menu_price_asc -> productDao.getAllProductsPriceAsc().toListProduct()
                R.id.menu_price_desc -> productDao.getAllProductsPriceDesc().toListProduct()
                else -> productDao.getAllProductsNameAsc().toListProduct()
            }
            updateRecycle(products)
        }
    }

    private fun configureFloat() {
        binding.floatNewProduct.setOnClickListener {
            openFormActivity(null, AddProductActivity::class.java)
        }
    }

    private fun configureRecycler() {
        binding.recycleMain.adapter = adapter
    }

    override fun itemClickListener(product: Product) {
        openFormActivity(product.id, DetailActivity::class.java)
    }

    override fun optDeleteListener(product: Product) {
        Dialogs(this).confirmDelete {
            lifecycleScope.launch {
                productDao.deleteProduct(product.toProductEntity())
            }
        }
    }

    override fun optEditListener(product: Product) {
        openFormActivity(productId = product.id, AddProductActivity::class.java)
    }

    private fun updateRecycle(products: List<Product>) {
        adapter.update(products)
    }

    private fun openFormActivity(productId: Int?, activity: Class<*>) {
        val intent = Intent(this, activity)
        intent.putExtra(SEND_ID_KEY, productId)
        startActivity(intent)
    }
}