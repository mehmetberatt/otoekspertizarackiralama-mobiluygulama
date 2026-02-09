package com.biturver.app.feature.chat.presentation

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.biturver.app.R

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.VH>() {

    private val messages = mutableListOf<ChatMessage>()

    fun addMessage(msg: ChatMessage) {
        messages.add(msg)
        notifyItemInserted(messages.size - 1)
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val layoutBot: LinearLayout = v.findViewById(R.id.layoutBot)
        val tvBot: TextView = v.findViewById(R.id.tvBotMessage)
        val btnAction: Button = v.findViewById(R.id.btnAction) // XML'deki buton

        val layoutUser: LinearLayout = v.findViewById(R.id.layoutUser)
        val tvUser: TextView = v.findViewById(R.id.tvUserMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = messages[position]

        if (item.isUser) {

            holder.layoutUser.visibility = View.VISIBLE
            holder.layoutBot.visibility = View.GONE
            holder.tvUser.text = item.message
        } else {
            holder.layoutUser.visibility = View.GONE
            holder.layoutBot.visibility = View.VISIBLE
            holder.tvBot.text = item.message

            if (item.targetActivity != null) {
                holder.btnAction.visibility = View.VISIBLE

                val pageName = item.targetActivity.simpleName
                    .replace("Activity", "")
                    .replace("RentCar", "Araç Kirala")
                    .replace("Settings", "Ayarlar")

                holder.btnAction.text = "$pageName Sayfasına Git ➜"

                holder.btnAction.setOnClickListener {
                    val context = holder.itemView.context
                    val intent = Intent(context, item.targetActivity)
                    context.startActivity(intent)
                }
            } else {
                holder.btnAction.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = messages.size
}