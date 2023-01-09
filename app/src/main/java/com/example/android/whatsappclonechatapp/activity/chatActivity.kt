package com.example.android.whatsappclonechatapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import com.example.android.whatsappclonechatapp.R
import com.example.android.whatsappclonechatapp.adapter.messageAdapter
import com.example.android.whatsappclonechatapp.databinding.ActivityChatBinding
import com.example.android.whatsappclonechatapp.databinding.ActivityOtpBinding
import com.example.android.whatsappclonechatapp.model.messageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.util.Date

class chatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var senderUid: String
    private lateinit var receiverUid: String
    private lateinit var senderRoom: String
    private lateinit var receiverRoom: String
    private lateinit var list:ArrayList<messageModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        senderUid = FirebaseAuth.getInstance().uid.toString()
        receiverUid = intent.getStringExtra("uid")!!

        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid

        list = ArrayList()

        binding.sendBtn.setOnClickListener {
            if (binding.messageBox.text.isEmpty()) {
                Toast.makeText(this, "Cannot Send Empty Message : ", Toast.LENGTH_SHORT).show()
            } else {
                val message =
                    messageModel(binding.messageBox.text.toString(), senderUid, Date().time)

                var randomKey = database.reference.push().key

                database.reference
                    .child("chats")
                    .child(senderRoom)
                    .child("message")
                    .child(randomKey!!)
                    .setValue(message)
                    .addOnSuccessListener {

                        database.reference.child("chats")
                            .child(receiverRoom)
                            .child("message")
                            .child(randomKey!!)
                            .setValue(message)
                            .addOnSuccessListener {
                                binding.messageBox.text = null
                            }

                    }
            }
        }

        database.reference.child("chats")
            .child(senderRoom)
            .child("message")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (snapshot1 in snapshot.children){
                        val data = snapshot1.getValue(messageModel::class.java)
                        list.add(data!!)
                    }
                    binding.recyclerViewChats.adapter = messageAdapter(this@chatActivity, list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@chatActivity, "Error $error",Toast.LENGTH_SHORT ).show()
                }

            })
    }
}
