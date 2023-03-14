package com.example.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.data.room.database.AppDataBase
import com.example.orgs.data.room.entity.ProductEntity
import com.example.orgs.data.room.entity.UserEntity
import com.example.orgs.databinding.ActivityFruitListBinding
import com.example.orgs.ui.adapter.ProductAdapter
import com.example.orgs.ui.dialog.Dialogs
import com.example.orgs.util.SEND_ID_KEY
import com.example.orgs.util.openActivity
import com.example.orgs.util.toast
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch


//ultilizando lifecycleScope para chamadas assincronas
class ProductListActivity : BaseActivity(), ProductAdapter.Onclick {
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
            launch {
                user
                    .filterNotNull()
                    .collect { user ->
                        getProducts(user.id)
                    }
            }

            launch {
                userDao.getAllUsers().collect { listUser ->
                    listUser?.let {
                        configureAutoComplete(listUser)
                    }
                }
            }
        }
    }

    private fun configureAutoComplete(list: List<UserEntity>) {
        val listStringUser: List<String> = list.map { userEntity ->
            userEntity.name
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            listStringUser
        )
        binding.autoComplete.setAdapter(adapter)

        binding.autoComplete.setOnItemClickListener { _, _, _, _ ->
            val userId = checkIfExist(list)
            if (userId == 0) {
                toast(R.string.str_user_not_found)
            } else {
                lifecycleScope.launch {
                    getProducts(userId = userId)
                    Log.i("TAG", "configureAutoComplete: UPDATE")
                }
            }
        }
    }

    private fun checkIfExist(list: List<UserEntity>): Int {
        val selectUserName = binding.autoComplete.text.toString()
        val result = 0
        list.forEach{user ->
            return if (user.name == selectUserName){
                user.id
            }else{
                0
            }
        }
        return result
    }

    private suspend fun getProducts(userId: Int) {
        productDao.getAllProductsWhitOutOrder(userId).collect { productList ->
            updateRecycle(productList)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        makeSelectType(item)
        return super.onOptionsItemSelected(item)
    }

    override fun itemClickListener(product: ProductEntity) {
        this.openActivity(DetailActivity::class.java) {
            putExtra(SEND_ID_KEY, product.id)
        }
    }

    override fun optDeleteListener(product: ProductEntity) {
        Dialogs(this).confirmDelete {
            lifecycleScope.launch {
                productDao.deleteProduct(product)
            }
        }
    }

    override fun optEditListener(product: ProductEntity) {
        openActivity(AddProductActivity::class.java) {
            putExtra(SEND_ID_KEY, product.id)
        }
    }

    private fun makeSelectType(item: MenuItem) {
        lifecycleScope.launch {
            when (item.itemId) {
                R.id.menu_user_profile -> openActivity(ProfileActivity::class.java)

                R.id.menu_name_asc -> {
                    lifecycleScope.launch {
                        user.value?.let {user ->
                            productDao.getProductsNameAsc(user.id).collect{listProductNameAsc ->
                                listProductNameAsc?.let {
                                    updateRecycle(it)
                                }
                            }
                        }
                    }
                }
                R.id.menu_name_desc -> {
                    lifecycleScope.launch {
                        user.value?.let {user ->
                            productDao.getProductsNameDesc(user.id).collect{listProductNameDesc ->
                                listProductNameDesc?.let {
                                    updateRecycle(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun configureFloat() {
        binding.floatNewProduct.setOnClickListener {
            this.openActivity(AddProductActivity::class.java)
        }
    }

    private fun configureRecycler() {
        binding.recycleMain.adapter = adapter
    }

    private fun updateRecycle(products: List<ProductEntity>) {
        adapter.update(products)
    }
}