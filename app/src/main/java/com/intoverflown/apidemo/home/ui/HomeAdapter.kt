package com.intoverflown.apidemo.home.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.intoverflown.apidemo.databinding.HomeRvItemViewBinding
import com.intoverflown.apidemo.home.data.PostModel

class HomeAdapter(var listener:HomeListener) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private var data : ArrayList<PostModel>?=null

    private lateinit var binding: HomeRvItemViewBinding

    fun setData(list: ArrayList<PostModel>){
        data = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
//        return HomeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_rv_item_view, parent, false))
        binding = HomeRvItemViewBinding.inflate(LayoutInflater.from(parent.context))
        val view = binding.root
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = data?.get(position)
        holder.bindView(item)

//        holder.itemView.img_delete
        binding.imgDelete.setOnClickListener {
            item?.let { it1 ->
                listener.onItemDeleted(it1, position)
            }
        }
    }

    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var itemVw = HomeRvItemViewBinding.bind(itemView)

        fun bindView(item: PostModel?) {
            // itemView.tv_home_item_title.text = item?.title
            // itemView.tv_home_item_body.text = item?.body
            itemVw.tvHomeItemTitle.text = item?.title
            itemVw.tvHomeItemBody.text = item?.body
        }

    }

    fun addData(postModel: PostModel) {
        data?.add(0,postModel)
        notifyItemInserted(0)
    }

    interface HomeListener{
        fun onItemDeleted(postModel: PostModel, position: Int)
    }

    fun removeData(position: Int) {
        data?.removeAt(position)
        notifyDataSetChanged()
    }
}