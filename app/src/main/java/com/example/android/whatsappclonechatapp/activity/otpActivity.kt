package com.example.android.whatsappclonechatapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.android.whatsappclonechatapp.R
import com.example.android.whatsappclonechatapp.databinding.ActivityOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class otpActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOtpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId:String
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val builder = AlertDialog.Builder(this)
        builder.setMessage("PLease Wait .....")
        builder.setTitle("Loading")
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
        val phoneNumber = "+91" + intent.getStringExtra("number")
        binding.otpVerifyHead.text = "Verify Number " + phoneNumber
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    dialog.dismiss()
                    Toast.makeText(this@otpActivity, "Please Try Again, You Entered Wrong OTP", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)

                    dialog.dismiss()
                    verificationId = p0
                }
            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        binding.button.setOnClickListener{
            dialog.show()
            if (binding.otpText.text!!.isEmpty()){
                Toast.makeText(this, "Please Enter the OTP ", Toast.LENGTH_SHORT).show()
            }else{

                val credential = PhoneAuthProvider.getCredential(verificationId, binding.otpText.text!!.toString())

                auth.signInWithCredential(credential)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            dialog.dismiss()
                            startActivity(Intent(this, profileActivity::class.java))
                            finish()
                        }else{
                            dialog.dismiss()
                            Toast.makeText(this,"Error ${it.exception}",Toast.LENGTH_SHORT).show()
                        }
                    }


            }
        }

    }
}