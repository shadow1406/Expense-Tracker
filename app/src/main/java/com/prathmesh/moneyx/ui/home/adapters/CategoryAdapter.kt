package com.prathmesh.moneyx.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.prathmesh.moneyx.R
import com.prathmesh.moneyx.ui.home.Category

class CategoryAdapter(
    private val list: List<Category>,
    private val mContext: Context
) : ArrayAdapter<Category>(mContext,0,list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView
            ?: LayoutInflater.from(mContext).inflate(R.layout.item_category,parent,false)
        getItem(position)?.let { item ->
            view.findViewById<ImageView>(R.id.ivLogo).setImageResource(item.icon)
            view.findViewById<TextView>(R.id.tvCategory).text = item.title
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_category,parent,false)
        getItem(position)?.let { item ->
            view.findViewById<ImageView>(R.id.ivLogo).setImageResource(item.icon)
            view.findViewById<TextView>(R.id.tvCategory).text = item.title
        }
        return view
    }

}