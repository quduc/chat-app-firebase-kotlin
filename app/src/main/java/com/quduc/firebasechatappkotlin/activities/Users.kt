package com.quduc.firebasechatappkotlin.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.quduc.firebasechatappkotlin.R
import com.quduc.firebasechatappkotlin.adapters.UserAdapter
import com.quduc.firebasechatappkotlin.models.User
import kotlinx.android.synthetic.main.users.*
import kotlinx.android.synthetic.main.item_user.*
import org.jetbrains.anko.find
class Users : AppCompatActivity() {
    val userList = ArrayList<User>()
    lateinit var userRecyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.users)

        userRecyclerView = find(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)

        imgProfile.setOnClickListener {
            val intent = Intent(this,Profile::class.java)
            startActivity(intent)
        }

        getUserList()
    }
    private fun getUserList(){
        val auth: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                val currentUser = snapshot.getValue(User::class.java)
                print(currentUser)
                if(currentUser!!.userImage == ""){
                    imgProfile.setImageResource(R.drawable.profile_image)
                } else {
                    Glide.with(this@Users).load(currentUser.userImage).into(imgProfile)
                }
                for(dataSnapShot: DataSnapshot in snapshot.children){
                    val user = dataSnapShot.getValue(User::class.java)
                    if(user!!.userID != auth?.uid){
                            userList.add(user)
                    }
                }
                val userAdapter = UserAdapter(this@Users, userList)
                userRecyclerView.adapter = userAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}