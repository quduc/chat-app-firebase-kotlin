package com.quduc.firebasechatappkotlin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.quduc.firebasechatappkotlin.R
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.find

class ChatAdapter(private val context: Context, private val chatList: ArrayList<com.quduc.firebasechatappkotlin.models.Chat>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private var firebaseUser: FirebaseUser? = null
    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtUserName: TextView = view.find(R.id.tvMessage)
        val imgUser: CircleImageView = view.find(R.id.userImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == MESSAGE_TYPE_LEFT) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_left, parent, false)
            ViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_right, parent, false)
            ViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatList[position]
        holder.txtUserName.text = chat.message
        //Glide.with(context).load(user.profileImage).placeholder(R.drawable.profile_image).into(holder.imgUser)
    }

    override fun getItemCount(): Int = chatList.size

    override fun getItemViewType(position: Int): Int {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        return if (firebaseUser!!.uid != chatList[position].senderID) {
            MESSAGE_TYPE_LEFT
        } else {
            MESSAGE_TYPE_RIGHT
        }
    }

}
