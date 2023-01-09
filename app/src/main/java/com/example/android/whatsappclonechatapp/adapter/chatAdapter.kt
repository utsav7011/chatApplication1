package com.example.android.whatsappclonechatapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.android.whatsappclonechatapp.R
import com.example.android.whatsappclonechatapp.activity.chatActivity
import com.example.android.whatsappclonechatapp.databinding.ChatSuserItemLaoyutBinding
import com.example.android.whatsappclonechatapp.databinding.FragmentChatBinding
import com.example.android.whatsappclonechatapp.model.userModel

class chatAdapter(var context: Context, var list: ArrayList<userModel>) : RecyclerView.Adapter<chatAdapter.chatViewHolder>(){

    inner class chatViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var binding :  ChatSuserItemLaoyutBinding = ChatSuserItemLaoyutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): chatViewHolder {
        return chatViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_suser_item_laoyut,parent, false))
    }

    override fun onBindViewHolder(holder: chatViewHolder, position: Int) {
        var user = list[position]
        Glide.with(context).load(user.imageUrl).into(holder.binding.userDp)
        holder.binding.chatUserName.text = user.name

        holder.itemView.setOnClickListener{
            val intent  =Intent(context, chatActivity::class.java)
            intent.putExtra("uid", user.uid)
            context!!.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}