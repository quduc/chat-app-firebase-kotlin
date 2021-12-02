package com.quduc.firebasechatappkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.quduc.firebasechatappkotlin.R
import kotlinx.android.synthetic.main.sign_up.*
class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)

        auth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {

            if (TextUtils.isEmpty(name.text)) {
                Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(email.text)) {
                Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(password.text)) {
                Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(cfPassword.text)) {
                Toast.makeText(this, "Confirm password is required", Toast.LENGTH_SHORT).show()
            }
            if(password.text != cfPassword.text){
                Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show()
            }
            onRegister(name.text.toString(),email.text.toString(),password.text.toString())
        }

        btnLogin.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun onRegister(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    val userID: String = user!!.uid

                    databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(userID)

                    val hashMap: HashMap<String, String> = HashMap()
                    hashMap["userID"] = userID
                    hashMap["userName"] = name
                    hashMap["profileImage"] = ""

                    databaseRef.setValue(hashMap).addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            val intent = Intent(this, Profile::class.java)
                            startActivity(intent)
                        }

                    }


                }
            }
    }

}