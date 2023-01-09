package com.example.android.whatsappclonechatapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.whatsappclonechatapp.R
import com.example.android.whatsappclonechatapp.adapter.chatAdapter
import com.example.android.whatsappclonechatapp.databinding.FragmentChatBinding
import com.example.android.whatsappclonechatapp.model.userModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class chatFragment : Fragment() {

    private lateinit var binding : FragmentChatBinding
    private var    database:FirebaseDatabase?=null
    lateinit var userlist:ArrayList<userModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater)

        database = FirebaseDatabase.getInstance()
        userlist = ArrayList()

        database!!.reference.child("users")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    userlist.clear()
                    for (snapshot1 in snapshot.children){
                        val user = snapshot1.getValue(userModel::class.java)
                        if (user!!.uid!=FirebaseAuth.getInstance().uid){
                            userlist.add(user)
                        }
                    }
                    binding.chatListRecyclerView.adapter = chatAdapter(requireContext(),userlist)

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        return binding.root
    }


}