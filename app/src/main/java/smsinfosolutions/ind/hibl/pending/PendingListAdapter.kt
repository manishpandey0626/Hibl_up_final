package smsinfosolutions.ind.hibl.pending

/**
 * Created by Manish on 28-Jan-21.
 */


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.healthymantra.piousvision.utilities.SendUserData
import org.healthymantra.piousvision.utilities.UserData
import smsinfosolutions.ind.hibl.R


class PendingListAdapter(val data: List<SendUserData>, val clicklistener: (SendUserData) -> Unit) :
    RecyclerView.Adapter<PendingFormViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingFormViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.pending_form_item, parent, false)
        return PendingFormViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PendingFormViewHolder, position: Int) {
        return holder.bind(data[position], clicklistener)
    }
}

class PendingFormViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    private val proposal_no: TextView = itemView.findViewById(R.id.proposal_no)



    fun bind(form: SendUserData, clicklistener: (SendUserData) -> Unit) {

        proposal_no.text = form.proposal_no
        itemView.setOnClickListener { clicklistener(form) }

    }
}


