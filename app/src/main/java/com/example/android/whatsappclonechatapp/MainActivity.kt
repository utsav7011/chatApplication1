package com.example.android.whatsappclonechatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.android.whatsappclonechatapp.activity.numberActivity
import com.example.android.whatsappclonechatapp.adapter.viewPagerAdapter
import com.example.android.whatsappclonechatapp.databinding.ActivityMainBinding
import com.example.android.whatsappclonechatapp.ui.callFragment
import com.example.android.whatsappclonechatapp.ui.chatFragment
import com.example.android.whatsappclonechatapp.ui.statusFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        val fragmentArrayList =  ArrayList<Fragment>()
        fragmentArrayList.add(chatFragment())
        fragmentArrayList.add(statusFragment())
        fragmentArrayList.add(callFragment())


        auth = FirebaseAuth.getInstance()


        val adapter = viewPagerAdapter(this ,supportFragmentManager, fragmentArrayList)
        binding!!.viewPager.adapter = adapter
        binding!!.tabs.setupWithViewPager(binding!!.viewPager)

    }
}