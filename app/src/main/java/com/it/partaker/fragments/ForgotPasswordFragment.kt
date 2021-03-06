package com.it.partaker.fragments

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.it.partaker.R
import kotlinx.android.synthetic.main.fragment_forgot_password.*

class ForgotPasswordFragment : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_forgot_password)

        btnResetPassword.setOnClickListener {

            val recEmail =  etRecoveryEmail.text.toString().trim()

            when{
                TextUtils.isEmpty(recEmail) -> Toast.makeText(baseContext,"Password Reset Email Required", Toast.LENGTH_LONG).show()
                else ->{
                    mAuth.sendPasswordResetEmail(recEmail).addOnCompleteListener {
                        if(it.isSuccessful) {
                            Toast.makeText(this,"Password Reset Email Sent: $recEmail", Toast.LENGTH_LONG).show()
                        }
                        else {
                            Toast.makeText(this,"Error: " + it.exception, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

}