package com.example.myapplication13

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication13.databinding.ItemRecyclerviewBinding

//ViewRecycler는 Adapter랑 ViewHolder 필요
class MyViewHoler(val binding:ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)

//MyAdapter는 3개의 오버라이드 함숨 필요
class MyAdapter(val datas: MutableList<String>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun getItemCount(): Int {
        return datas?.size ?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = MyViewHoler(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHoler).binding
        binding.itemTodo.text = datas!![position]
    }
}