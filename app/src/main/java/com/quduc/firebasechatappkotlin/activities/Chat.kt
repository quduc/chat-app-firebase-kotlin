package com.quduc.firebasechatappkotlin.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.quduc.firebasechatappkotlin.R
import com.quduc.firebasechatappkotlin.adapters.ChatAdapter
import kotlinx.android.synthetic.main.chat.*
import org.jetbrains.anko.find
import com.quduc.firebasechatappkotlin.models.Chat as ChatModel

class Chat : AppCompatActivity() {
    private var firebaseUser: FirebaseUser? = null
    private var reference: DatabaseReference? = null
    private lateinit var chatRecyclerView: RecyclerView
    private var chatList = ArrayList<ChatModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat)

        chatRecyclerView = find(R.id.chatRecyclerView)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)

        val intent = intent
        val userID = intent.getStringExtra("userID")
        val userName = intent.getStringExtra("userName")

        receiverName.text = userName

        imgBack.setOnClickListener {
            onBackPressed()
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userID!!)

        btnSendMessage.setOnClickListener {
            val message: String = txtMessage.text.toString()
            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "Message is empty", Toast.LENGTH_SHORT).show()
            } else {
                sendMessage(firebaseUser!!.uid, userID, message)
                txtMessage.setText("")
            }
        }
        readMessage(firebaseUser!!.uid, userID)

    }

    private fun sendMessage(senderID: String, receiverID: String, message: String) {
        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference

        val hashMap: HashMap<String, String> = HashMap()
        hashMap["senderID"] = senderID
        hashMap["receiverID"] = receiverID
        hashMap["message"] = message

        reference.child("Chat").push().setValue(hashMap)
    }

    private fun readMessage(senderId: String, receiverId: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(ChatModel::class.java)

                    if (chat!!.senderID == senderId && chat.receiverID == receiverId ||
                        chat.senderID == receiverId && chat.receiverID == senderId
                    ) {
                        chatList.add(chat)
                    }
                }

                val chatAdapter = ChatAdapter(this@Chat, chatList)

                chatRecyclerView.adapter = chatAdapter
            }
        })
    }
}