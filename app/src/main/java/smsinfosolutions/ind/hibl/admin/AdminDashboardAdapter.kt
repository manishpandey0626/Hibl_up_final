package smsinfosolutions.ind.hibl.admin

/**
 * Created by Manish on 25-Mar-21.
 */


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import smsinfosolutions.ind.hibl.databinding.AdminDashboardItemBinding
import smsinfosolutions.ind.hibl.utilities.AdminDashboardItem


class AdminDashboardAdapter(
    val data: List<AdminDashboardItem>,
    val clicklistener: (AdminDashboardItem) -> Unit
) : RecyclerView.Adapter<AdminDashboardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminDashboardViewHolder {
        val binding =
            AdminDashboardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminDashboardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AdminDashboardViewHolder, position: Int) {
        return holder.bind(data[position], clicklistener)
    }
}

class AdminDashboardViewHolder(val binding: AdminDashboardItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: AdminDashboardItem, clicklistener: (AdminDashboardItem) -> Unit) {
        binding.title.text = data.tile
        binding.count.text = data.nos
        when(adapterPosition){
            0 -> binding.cardveiw.setCardBackgroundColor(Color.parseColor("#008558"))
            1->  binding.cardveiw.setCardBackgroundColor(Color.parseColor("#ff862d"))
            2-> binding.cardveiw.setCardBackgroundColor(Color.parseColor("#3554ff"))
            3-> binding.cardveiw.setCardBackgroundColor(Color.parseColor("#800117"))
            4-> binding.cardveiw.setCardBackgroundColor(Color.parseColor("#ff0266"))
            5-> binding.cardveiw.setCardBackgroundColor(Color.parseColor("#2e3b54"))
            else-> binding.cardveiw.setCardBackgroundColor(Color.parseColor("#008558"))
        }


        binding.root.setOnClickListener {
            clicklistener(data)
        }
    }

}