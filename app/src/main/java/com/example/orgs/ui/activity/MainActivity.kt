package com.example.orgs.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.orgs.R
import com.example.orgs.dao.Dao
import com.example.orgs.model.Product
import com.example.orgs.ui.adapter.ProductAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ProductAdapter.Onclick {
    private lateinit var dao: Dao
    private lateinit var adapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dao = Dao()

        configureRecycler()

        val fab = findViewById<FloatingActionButton>(R.id.float_new_product)
        fab.setOnClickListener{
            openFormActivity(null)
        }

    }

    private fun configureRecycler() {
        recyclerView = findViewById(R.id.recycle_main)
        adapter = ProductAdapter(dao.getAllProducts(), this)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        updateRecycle()
    }

    override fun itemClickListener(product: Product) {
        openFormActivity(product)
    }

    override fun imageDeleteListener(product: Product) {
        dao.removeProduct(product)
        updateRecycle()
    }

    private fun updateRecycle() {
        adapter.update(dao.getAllProducts())
    }

    private fun openFormActivity(product: Product?){
        val intent = Intent(this, FormAddProduct::class.java)
        intent.putExtra("product", product)
        startActivity(intent)
    }
}