package com.example.orgs.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.orgs.R
import com.example.orgs.databinding.ItemProductRecycleBinding
import com.example.orgs.model.Product
import com.example.orgs.util.loadImage
import java.text.NumberFormat
import java.util.*

class ProductAdapter(products:List<Product> = emptyList(), private val listener:Onclick) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){

    private val dataSet = products.toMutableList()

        class ProductViewHolder(binding: ItemProductRecycleBinding) : RecyclerView.ViewHolder(binding.root){
            private val format:NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt","br"))
            private val txvName = binding.txvName
            private val txvDesc = binding.txvDesc
            private val txvPrice = binding.txvPrice
            private val imgProduct = binding.imgItemRecycle
            val imgDelete = binding.imgDelete

            fun bind(product: Product) {
                txvName.text = product.name
                txvDesc.text = product.description
                txvPrice.text =  format.format(product.price)
                imgProduct.loadImage(product.image)
            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductRecycleBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = dataSet[position]
        holder.itemView.setOnClickListener { listener.itemClickListener(product) }
        holder.bind(product)
        holder.imgDelete.setOnClickListener {
            showPopupMenu(it, product)
        }
    }

    private fun showPopupMenu(view: View, product: Product) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.menu_options_product)
        popupMenu.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.menu_edit ->{
                    listener.optEditListener(product)
                    true
                }
                R.id.menu_delete ->{
                    listener.optDeleteListener(product)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    fun update(allProducts: List<Product>) {
        dataSet.clear()
        dataSet.addAll(allProducts)
        notifyDataSetChanged()
    }

    interface Onclick{
        fun itemClickListener(product: Product)
        fun optDeleteListener(product: Product)
        fun optEditListener(product: Product)
    }
}