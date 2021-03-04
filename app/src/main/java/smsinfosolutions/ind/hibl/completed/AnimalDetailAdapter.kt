package smsinfosolutions.ind.hibl.completed

/**
 * Created by Manish on 13-Feb-21.
 */

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.healthymantra.piousvision.utilities.ShowAnimalDetail
import smsinfosolutions.ind.hibl.databinding.AnimalItemBinding
import smsinfosolutions.ind.hibl.databinding.ShowAnimalItemBinding


class AnimalDetailAdapter(val animals: List<ShowAnimalDetail>): RecyclerView.Adapter<ShowAnimalDetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowAnimalDetailViewHolder {
        // val view = LayoutInflater.from(parent.context).inflate(R.layout.animal_item, parent, false)

        val binding = ShowAnimalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ShowAnimalDetailViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return animals.size
    }

    override fun onBindViewHolder(holder: ShowAnimalDetailViewHolder, position: Int) {
        return holder.bind(animals[position])
    }
}

class ShowAnimalDetailViewHolder(val binding: ShowAnimalItemBinding): RecyclerView.ViewHolder(binding.root){



    fun bind(animal: ShowAnimalDetail) {
        binding.tagNo.text=animal.tag_no
        binding.animalType.text=animal.animal
        binding.sumInsured.text=animal.sum_insured


        }



    }




