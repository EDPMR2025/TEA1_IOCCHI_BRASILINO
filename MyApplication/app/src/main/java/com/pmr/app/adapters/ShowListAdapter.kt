package com.pmr.app.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pmr.app.R
import com.pmr.app.activities.ShowListActivity

class ShowListAdapter(context: Context) : RecyclerView.Adapter<ShowListAdapter.ShowViewHolder>() {
    var listItems: List<String> = listOf()

    private val context = context

    fun setList(list: List<String>) {
        listItems = list
        notifyDataSetChanged()
    }

    class ShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItem: TextView = itemView.findViewById(R.id.tvItem)
        val cbItem: CheckBox = itemView.findViewById(R.id.cbItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_item_list, parent, false)
        return ShowViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.tvItem.text = listItems[position].split("_").last()
        holder.cbItem.isChecked = listItems[position].split("_").first() == "1"
        holder.itemView.setOnClickListener {
            onItemClicked(position, holder)
        }
        holder.cbItem.setOnClickListener {
            holder.cbItem.isChecked = !holder.cbItem.isChecked
            onItemClicked(position, holder)
        }

    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun onItemClicked(position: Int, holder: ShowViewHolder) {
        // go to other activity
        holder.cbItem.isChecked = !holder.cbItem.isChecked
        val sharedPref = context.getSharedPreferences("com.pmr.tea1", Context.MODE_PRIVATE)
        val keySP = "list_${(context as ShowListActivity).currentList}_${(context as ShowListActivity).currentUser}"
        val listOfLists = sharedPref.getString(keySP, String())
        Log.d("ShowListAdapter", "listOfLists: $listOfLists")
        if (listOfLists != null) {
            val list = listOfLists.split(";").toMutableList()
            val item = list[position].split("_")
            Log.d("ShowListAdapter", "item: $item")
            list[position] = if (item.first() == "1") "0_${item.last()}" else "1_${item.last()}"
            val newListOfLists = list.joinToString(";")
            Log.d("ShowListAdapter", "newListOfLists: $newListOfLists")
            with(sharedPref.edit()) {
                putString(keySP, newListOfLists)
                apply()
            }
        }
    }
}