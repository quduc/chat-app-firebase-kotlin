package com.quduc.firebasechatappkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.quduc.firebasechatappkotlin.R
import kotlinx.android.synthetic.main.sign_in.*

class SignIn : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)


        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        if (user != null) {
            val intent = Intent(this, Users::class.java)
            startActivity(intent)
            finish()

        }
        btnLogin.setOnClickListener {
            if (TextUtils.isEmpty(email.text) || TextUtils.isEmpty(password.text)) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            email.setText("")
                            password.setText("")
                            val intent = Intent(this, Users::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }
        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }
    }

}