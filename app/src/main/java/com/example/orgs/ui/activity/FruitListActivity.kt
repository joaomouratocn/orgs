package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.dao.Dao
import com.example.orgs.databinding.ActivityFruitListBinding
import com.example.orgs.model.Product
import com.example.orgs.ui.adapter.ProductAdapter
import com.example.orgs.ui.dialog.Dialogs

class FruitListActivity : AppCompatActivity(), ProductAdapter.Onclick {
    private lateinit var dao: Dao
    private lateinit var adapter: ProductAdapter

    private val binding by lazy{
        ActivityFruitListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dao = Dao()

        configureRecycler()

        binding.floatNewProduct.setOnClickListener{
            openFormActivity(null, AddProductActivity::class.java)
        }

    }

    private fun configureRecycler() {
        adapter = ProductAdapter(dao.getAllProducts(), this)
        binding.recycleMain.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        updateRecycle()
    }

    override fun itemClickListener(product: Product) {
        openFormActivity(product, DetailActivity::class.java)
    }

    override fun optDeleteListener(product: Product) {
        Dialogs(this).confirmDelete {
            dao.removeProduct(product)
            updateRecycle()
        }
    }

    override fun optEditListener(product: Product) {
        openFormActivity(product, AddProductActivity::class.java)
    }

    private fun updateRecycle() {
        adapter.update(dao.getAllProducts())
    }

    private fun openFormActivity(product: Product?, activity: Class<*>){
        val intent = Intent(this, activity)
        intent.putExtra("product", product)
        startActivity(intent)
    }
}