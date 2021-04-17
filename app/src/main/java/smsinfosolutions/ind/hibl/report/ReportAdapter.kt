package smsinfosolutions.ind.hibl.report

/**
 * Created by Manish on 25-Mar-21.
 */



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import smsinfosolutions.ind.hibl.databinding.ReportItemBinding
import smsinfosolutions.ind.hibl.utilities.Report


class ReportAdapter(val data: List<Report>, val size: Int): RecyclerView.Adapter<ReportViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {

        val binding =ReportItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ReportViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        return holder.bind(data[position],size)
    }



}

class ReportViewHolder(val binding : ReportItemBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(data: Report, size: Int) {
        binding.one.text=data.one
        binding.two.text=data.two
        if(size>2) {
            binding.three.visibility= View.VISIBLE
            binding.three.text = data.three
        }
        else
        {
            binding.three.visibility= View.GONE
        }



    }

}