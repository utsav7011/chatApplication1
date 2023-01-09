package com.example.android.whatsappclonechatapp.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.android.whatsappclonechatapp.MainActivity
import com.example.android.whatsappclonechatapp.R
import com.example.android.whatsappclonechatapp.databinding.ActivityProfileBinding
import com.example.android.whatsappclonechatapp.model.userModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class profileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var fstore: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var selectedImage: Uri
    private lateinit var dialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = AlertDialog.Builder(this)
            .setMessage("Updating Profile : ")
            .setCancelable(false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        fstore = FirebaseStorage.getInstance()


        binding.userImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)

        }
        binding.btnContinueBtn.setOnClickListener {
            if (binding.username.text!!.toString().isEmpty()) {
                Toast.makeText(this, "Please enter your name !!", Toast.LENGTH_SHORT).show()
            } else if (selectedImage == null) {
                Toast.makeText(this, "please select the profile image !! ", Toast.LENGTH_SHORT).show()
            } else {
                uploadData()
            }
        }


    }

    private fun uploadData() {
        val reference = fstore.reference.child("Profile").child(Date().time.toString())

        reference.putFile(selectedImage).addOnCompleteListener {
            if (it.isSuccessful) {
                reference.downloadUrl.addOnSuccessListener { task ->
                    uploadInfo(task.toString())
                }
            }
        }
    }

    private fun uploadInfo(imageUrl: String) {
        val user = userModel(
            auth.uid.toString(),
            binding.username.text.toString(),
            auth.currentUser!!.phoneNumber.toString(),
            imageUrl.toString()
        )

        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Data Inserted ", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (data.data != null) {
                selectedImage = data.data!!
                binding.userImage.setImageURI(selectedImage)
            }
        }
    }
}