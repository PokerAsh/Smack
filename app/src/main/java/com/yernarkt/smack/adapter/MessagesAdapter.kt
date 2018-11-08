package com.yernarkt.smack.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.yernarkt.smack.R
import com.yernarkt.smack.model.Message
import com.yernarkt.smack.vnetwork.UserDataService
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MessagesAdapter(val context: Context, val messageList: List<Message>) :
    RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>() {
    override fun onCreateViewHolder(viewGoup: ViewGroup, position: Int): MessagesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_message, viewGoup, false)
        return MessagesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messageList.count()
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        val message = messageList[position]

        holder.bind(context, message)
    }

    inner class MessagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage = itemView.findViewById<ImageView>(R.id.messageUserImage)
        val userName = itemView.findViewById<TextView>(R.id.messageUserNameLabel)
        val timeStamp = itemView.findViewById<TextView>(R.id.timeStampLabel)
        val messageBody = itemView.findViewById<TextView>(R.id.messageBodyLabel)

        fun bind(context: Context, message: Message) {
            userName.text = message.userName
            timeStamp.text = correctDate(message.timeStamp)
            messageBody.text = message.messageBody

            val resourceId = context.resources.getIdentifier(message.userAvatar, "drawable", context.packageName)
            userImage?.setImageResource(resourceId)
            userImage?.setBackgroundColor(UserDataService.returnAvatarColor(message.userAvatarColor))
        }

        private fun correctDate(isoDate: String): String {
            val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            isoFormatter.timeZone = TimeZone.getTimeZone("UTF")
            var convertedDate = Date()
            try {
                convertedDate = isoFormatter.parse(isoDate)
            } catch (e: ParseException) {
                Log.d("PARSE", "Cannot parse date")
            }

            val outDateString = SimpleDateFormat("dd.MM.yy, HH:mm", Locale.getDefault())
            return outDateString.format(convertedDate)
        }
    }
}