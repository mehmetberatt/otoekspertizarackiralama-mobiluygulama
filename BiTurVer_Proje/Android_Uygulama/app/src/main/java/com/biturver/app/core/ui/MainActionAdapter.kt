package com.biturver.app.core.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.biturver.app.R
import com.google.android.material.card.MaterialCardView

data class MainAction(
    val title: String,
    val desc: String,
    val iconRes: Int,
    val accentColor: Int,
    val onClick: () -> Unit
)

class MainActionAdapter(
    private val items: List<MainAction>
) : RecyclerView.Adapter<MainActionAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val icon: ImageView = v.findViewById(R.id.imgIcon)
        val iconBg: MaterialCardView = v.findViewById(R.id.iconBg)
        val title: TextView = v.findViewById(R.id.tvTitle)
        val desc: TextView = v.findViewById(R.id.tvDesc)
        val root: View = v.findViewById(R.id.cardRoot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_action_card, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]

        holder.icon.setImageResource(item.iconRes)
        holder.icon.setColorFilter(Color.WHITE)

        holder.iconBg.backgroundTintList = ColorStateList.valueOf(item.accentColor)

        holder.title.text = item.title
        holder.desc.text = item.desc

        holder.root.setOnClickListener { item.onClick() }
    }

    override fun getItemCount(): Int = items.size
}