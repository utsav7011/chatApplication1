package com.example.android.whatsappclonechatapp.adapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.android.whatsappclonechatapp.R
import com.example.android.whatsappclonechatapp.databinding.ReceiverItemLayoutBinding
import com.example.android.whatsappclonechatapp.databinding.SentItemLayoutBinding
import com.example.android.whatsappclonechatapp.model.messageModel
import com.google.firebase.auth.FirebaseAuth

class messageAdapter(val context: Context, var list: ArrayList<messageModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var ITEM_SENT = 1
    var ITEM_RECEIVED = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return if (viewType == ITEM_SENT){
            sentViewHolder(LayoutInflater.from(context).inflate(R.layout.sent_item_layout, parent, false ))
        }else{
            receiverViewHolder(LayoutInflater.from(context).inflate(R.layout.receiver_item_layout, parent, false ))
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (FirebaseAuth.getInstance().uid == list[position].senderId) ITEM_SENT else ITEM_RECEIVED
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val messasge = list[position]

        if (holder.itemViewType == ITEM_SENT){
            val viewHolder = holder as sentViewHolder
            viewHolder.binding.sentMessage.text = messasge.message
        }else{
            val viewHolder = holder as receiverViewHolder
            viewHolder.binding.receivedMessage.text = messasge.message
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class sentViewHolder(view:View) :  RecyclerView.ViewHolder(view){
        var binding = SentItemLayoutBinding.bind(view)
    }

    inner class receiverViewHolder(view:View) :  RecyclerView.ViewHolder(view){
        var binding = ReceiverItemLayoutBinding.bind(view)
    }



}