package com.example.orgs.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.orgs.R
import com.example.orgs.databinding.ActivityDetailBinding
import com.example.orgs.model.Product
import com.example.orgs.util.loadImage
import java.text.NumberFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    private val binding:ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }
    private val format: NumberFormat by lazy {
        NumberFormat.getCurrencyInstance(Locale("pt","br"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = getString(R.string.str_detail)

        val receivedProduct: Product? = intent.getParcelableExtra("product")

        receivedProduct?.let {product ->
            binding.imgImage.loadImage(product.image)
            binding.txvPrice.text = format.format(product.price)
            binding.txvName.text = product.name
            binding.txvDesc.text= product.description
        }
    }
}