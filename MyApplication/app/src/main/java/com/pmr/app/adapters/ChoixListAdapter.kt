package com.pmr.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pmr.app.R
import com.pmr.app.activities.ChoixListActivity

class ChoixListAdapter(context: Context) : RecyclerView.Adapter<ChoixListAdapter.ChoixViewHolder>() {
    var listChoix: List<String> = listOf()

    private val context = context

    fun setList(list: List<String>) {
        listChoix = list
        notifyDataSetChanged()
    }

    class ChoixViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvChoix: TextView = itemView.findViewById(R.id.tvChoix)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoixViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_choix, parent, false)
        return ChoixViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChoixViewHolder, position: Int) {
        holder.tvChoix.text = listChoix[position]
        holder.itemView.setOnClickListener {
            onItemClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return listChoix.size
    }

    fun onItemClicked(position: Int) {
        // go to other activity
        (context as ChoixListActivity).goToDetailActivity(listChoix[position])

    }

}