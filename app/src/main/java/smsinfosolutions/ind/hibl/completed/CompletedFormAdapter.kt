package smsinfosolutions.ind.hibl.completed

/**
 * Created by Manish on 09-Feb-21.
 */

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.healthymantra.piousvision.utilities.CompletedFormDetail
import org.healthymantra.piousvision.utilities.SendUserData

import smsinfosolutions.ind.hibl.R
import smsinfosolutions.ind.hibl.databinding.CompletedFormItemBinding


class CompletedFormAdapter(val data: List<CompletedFormDetail>,val clicklistener:(CompletedFormDetail)->Unit): RecyclerView.Adapter<CompletedFormViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedFormViewHolder {

        val binding =CompletedFormItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
     //   val view = LayoutInflater.from(parent.context).inflate(R.layout.payment_item, parent, false)
        return CompletedFormViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CompletedFormViewHolder, position: Int) {
        return holder.bind(data[position],clicklistener)
    }



}

class CompletedFormViewHolder(val binding : CompletedFormItemBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(data: CompletedFormDetail,clicklistener:(CompletedFormDetail)->Unit) {
      binding.beneficiaryName.text=data.beneficiary_name
      binding.policyNo.text=data.policy_no
      binding.proposalNo.text=data.proposal_no
      binding.noOfAnimals.text=data.no_of_animal
      binding.root.setOnClickListener{
          clicklistener(data)
      }



    }

}