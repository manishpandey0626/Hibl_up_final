package smsinfosolutions.ind.hibl.registration

/**
 * Created by Manish on 11-Jan-21.
 */
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.healthymantra.piousvision.utilities.AnimalDetail

import smsinfosolutions.ind.hibl.R
import smsinfosolutions.ind.hibl.databinding.AnimalItemBinding


class AnimalListAdapter(
    val categories: List<AnimalDetail>,
    val clicklistener: (AnimalDetail,String) -> Unit
): RecyclerView.Adapter<AnimalDetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalDetailViewHolder {
       // val view = LayoutInflater.from(parent.context).inflate(R.layout.animal_item, parent, false)

        val binding = AnimalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AnimalDetailViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: AnimalDetailViewHolder, position: Int) {
        return holder.bind(categories[position], clicklistener)
    }
}

class AnimalDetailViewHolder(binding: AnimalItemBinding): RecyclerView.ViewHolder(binding.root){
    val binding:AnimalItemBinding=binding
  /*private val tag_no:TextView = itemView.findViewById(R.id.tag_no)
    private val animal_type:TextView = itemView.findViewById(R.id.animal_type)
    private val sum_insured:TextView = itemView.findViewById(R.id.sum_insured)
    private val upload: LinearLayout = itemView.findViewById(R.id.upload)
    private val delete: LinearLayout = itemView.findViewById(R.id.delete)*/
  //  private val catname:TextView = itemView.findViewById(R.id.cat_title)


    fun bind(animal: AnimalDetail, clicklistener: (AnimalDetail,String) -> Unit) {
        binding.tagNo.text=animal.tag_no
        binding.animalType.text=animal.animal_type
        binding.sumInsured.text=animal.sum_insured
     //   binding.uploadIcon.setColorFilter(Color.argb(255, 255, 255, 255))
       binding.upload.setOnClickListener{
         clicklistener(animal,"upload")

       }
        binding.delete.setOnClickListener{
         clicklistener(animal,"delete")

            

       }
    }

}


