package smsinfosolutions.ind.hibl.completed

/**
 * Created by Manish on 09-Feb-21.
 */

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import smsinfosolutions.ind.hibl.utilities.CompletedFormDetail
import smsinfosolutions.ind.hibl.databinding.CompletedFormItemBinding


class CompletedFormAdapter(val data: List<CompletedFormDetail>, val clicklistener:(CompletedFormDetail)->Unit): RecyclerView.Adapter<CompletedFormViewHolder>(),Filterable {
    private var filter_data: List<CompletedFormDetail>
    init {
        filter_data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedFormViewHolder {

        val binding =CompletedFormItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
     //   val view = LayoutInflater.from(parent.context).inflate(R.layout.payment_item, parent, false)
        return CompletedFormViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return filter_data.size
    }

    override fun onBindViewHolder(holder: CompletedFormViewHolder, position: Int) {
        return holder.bind(filter_data[position],clicklistener)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filter_data = data
                } else {
                    val resultList = mutableListOf<CompletedFormDetail>()
                    for (row in data) {
                        if ((row.hospital_name+row.beneficiary_name+row.proposal_no).toLowerCase().contains(charSearch.toLowerCase())) {
                            resultList.add(row)
                        }
                    }
                    filter_data = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filter_data
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filter_data = results?.values as List<CompletedFormDetail>
                notifyDataSetChanged()
            }

        }
    }

}

class CompletedFormViewHolder(val binding : CompletedFormItemBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(data: CompletedFormDetail, clicklistener:(CompletedFormDetail)->Unit) {
      binding.beneficiaryName.text=data.beneficiary_name
      binding.policyNo.text=data.policy_no
      binding.proposalNo.text=data.proposal_no
      binding.noOfAnimals.text=data.no_of_animal
      binding.hospital.text=data.hospital_name
      binding.root.setOnClickListener{
          clicklistener(data)
      }


    }

}