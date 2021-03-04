package smsinfosolutions.ind.hibl.claim

/**
 * Created by Manish on 01-Mar-21.
 */


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.healthymantra.piousvision.utilities.Claim
import org.healthymantra.piousvision.utilities.CompletedFormDetail
import smsinfosolutions.ind.hibl.databinding.ClaimListItemBinding


class ClaimListAdapter(val data: List<Claim>,val clicklistener:(Claim)->Unit): RecyclerView.Adapter<ClaimListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimListViewHolder {

        val binding =ClaimListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        //   val view = LayoutInflater.from(parent.context).inflate(R.layout.payment_item, parent, false)
        return ClaimListViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ClaimListViewHolder, position: Int) {
        return holder.bind(data[position],clicklistener)
    }



}

class ClaimListViewHolder(val binding : ClaimListItemBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(data: Claim,clicklistener:(Claim)->Unit) {
        binding.tagNo.text=data.tagNo
        binding.intimationDate.text=data.inti_Dt
        binding.proposalNo.text=data.coverNoteNo
        binding.customerName.text=data.custName
        binding.sumInsured.text=data.sumInsured
        binding.investigatorName.text=data.investigator
        binding.investigatorMobile.text=data.investigatorMobile.toString()
        binding.claimStatus.text=data.status
        binding.statusDate.text=data.statusDate

        binding.root.setOnClickListener{
            clicklistener(data)
        }



    }

}