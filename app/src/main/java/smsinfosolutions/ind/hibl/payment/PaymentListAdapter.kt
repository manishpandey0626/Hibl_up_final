package smsinfosolutions.ind.hibl.payment

/**
 * Created by Manish on 16-Jan-21.
 */



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.healthymantra.piousvision.utilities.PaymentDetail
import org.healthymantra.piousvision.utilities.SendUserData

import smsinfosolutions.ind.hibl.R


class PaymentListAdapter(val data: List<PaymentDetail>): RecyclerView.Adapter<PaymentDetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.payment_item, parent, false)
        return PaymentDetailViewHolder(view)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PaymentDetailViewHolder, position: Int) {
        return holder.bind(data[position])
    }

    fun getSelectedRecords():List<PaymentDetail>
    {
        return  data.filter { it.checked }
    }

}

class PaymentDetailViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
    private val proposal_no:TextView = itemView.findViewById(R.id.proposal_no)
    private val amount:TextView = itemView.findViewById(R.id.amount)
    private val balance:TextView = itemView.findViewById(R.id.balance)
    private val checked:CheckBox = itemView.findViewById(R.id.checked)



    fun bind(PaymentDetail: PaymentDetail) {
        proposal_no.text=PaymentDetail.proposal_no
        amount.text=PaymentDetail.total_payment
        balance.text=PaymentDetail.balance
        checked.isChecked=PaymentDetail.checked

        checked.setOnCheckedChangeListener{ compoundButton: CompoundButton, b: Boolean ->
            PaymentDetail.checked=b
        }

    }

}