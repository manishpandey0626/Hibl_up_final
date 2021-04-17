package smsinfosolutions.ind.hibl.send

/**
 * Created by Manish on 04-Feb-21.
 */



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import smsinfosolutions.ind.hibl.utilities.SendUserData
import smsinfosolutions.ind.hibl.R


class SendListAdapter(val data: List<SendUserData>, val clicklistener: (SendUserData) -> Unit): RecyclerView.Adapter<SendFormViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendFormViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.send_form_item, parent, false)
        return SendFormViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SendFormViewHolder, position: Int) {
        return holder.bind(data[position], clicklistener)
    }

    fun getSelectedRecords():List<SendUserData>
    {
       return  data.filter { it.check }
    }

}

class SendFormViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


    private val proposal_no:TextView = itemView.findViewById(R.id.proposal_no)
    private val checked:CheckBox=itemView.findViewById(R.id.check)


    fun bind(form: SendUserData, clicklistener: (SendUserData) -> Unit) {

        proposal_no.text=form.proposal_no
        checked.isChecked=form.check
        checked.setOnCheckedChangeListener{ compoundButton: CompoundButton, b: Boolean ->
         form.check=b
        }
        itemView.setOnClickListener{clicklistener(form)}

    }
}



