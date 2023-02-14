package com.example.orgs.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orgs.R
import com.example.orgs.model.Product

class ProductAdapter(products:List<Product>, private val listener:Onclick) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val dataSet = products.toMutableList()

        class ProductViewHolder(view:View) : RecyclerView.ViewHolder(view.rootView){
            private val txvName:TextView
            private val txvDesc:TextView
            private val txvPrice:TextView
            val imgDelete:ImageView

            init {
                txvName = view.findViewById(R.id.txv_name)
                txvDesc = view.findViewById(R.id.txv_desc)
                txvPrice = view.findViewById(R.id.txv_price)
                imgDelete = view.findViewById(R.id.img_delete)
            }

            fun bind(product: Product) {
                txvName.text = product.name
                txvDesc.text = product.description
                txvPrice.text = product.price.toPlainString()
            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = dataSet[position]
        holder.itemView.setOnClickListener { listener.itemClickListener(product) }
        holder.bind(product)
        holder.imgDelete.setOnClickListener {
            listener.imageDeleteListener(product)
        }
    }

    fun update(allProducts: List<Product>) {
        dataSet.clear()
        dataSet.addAll(allProducts)
        notifyDataSetChanged()
    }

    interface Onclick{
        fun itemClickListener(product: Product)
        fun imageDeleteListener(product: Product)
    }
}