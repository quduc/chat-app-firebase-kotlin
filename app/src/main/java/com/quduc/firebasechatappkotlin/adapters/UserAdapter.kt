package com.quduc.firebasechatappkotlin.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.quduc.firebasechatappkotlin.R
import com.quduc.firebasechatappkotlin.activities.Chat
import com.quduc.firebasechatappkotlin.models.User
import org.jetbrains.anko.find

class UserAdapter(private val context: Context, private val userList:ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val userName: TextView = view.find(R.id.userName)
        val temp : TextView = view.find(R.id.temp)
        val userImage : ImageView = view.find(R.id.userImage)
        val layoutUser: LinearLayout = view.find(R.id.layoutUser)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.userName.text = user.userName
        Glide.with(context).load(user.userImage).into(holder.userImage)
        holder.layoutUser.setOnClickListener{
            val intent = Intent(context,Chat::class.java)
            intent.putExtra("userID",user.userID)
            intent.putExtra("userName",user.userName)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = userList.size
}