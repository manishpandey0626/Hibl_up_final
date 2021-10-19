package smsinfosolutions.ind.hibl.registration

/**
 * Created by Manish on 01-Feb-21.
 */

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import smsinfosolutions.ind.hibl.R
import smsinfosolutions.ind.hibl.utilities.AnimalImages

import smsinfosolutions.ind.hibl.databinding.AnimalImageItemBinding
import smsinfosolutions.ind.hibl.utilities.Utils


class AnimalImageAdapter(val animals: List<AnimalImages>, val clicklistener: (AnimalImages, String) -> Unit, val context:Context): RecyclerView.Adapter<AnimalImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalImageViewHolder {

        val binding =  AnimalImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimalImageViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return animals.size
    }

    override fun onBindViewHolder(holder: AnimalImageViewHolder, position: Int) {
        return holder.bind(animals[position],clicklistener,context)
    }
}

class AnimalImageViewHolder(val binding : AnimalImageItemBinding): RecyclerView.ViewHolder(binding.root){



    fun bind(animal: AnimalImages, clicklistener: (AnimalImages, String) -> Unit, context:Context) {
        if(animal.file!=null) {
            binding.animalPhoto.setImageBitmap(Utils.Base64_to_bitmap(animal.file, context))
            binding.view.visibility= View.VISIBLE
        }
        else{
            binding.view.visibility=View.GONE
            if(animal.file_name=="Front")
            binding.animalPhoto.setImageDrawable(context.resources.getDrawable(R.drawable.front))
            else if(animal.file_name=="Back")
            {
                binding.animalPhoto.setImageDrawable(context.resources.getDrawable(R.drawable.back))
            }
            else if(animal.file_name=="Right")
            {
                binding.animalPhoto.setImageDrawable(context.resources.getDrawable(R.drawable.right))
            }
            else if(animal.file_name=="Left")
            {
                binding.animalPhoto.setImageDrawable(context.resources.getDrawable(R.drawable.left))
            }
            else if(animal.file_name=="With Owner")
            {
                binding.animalPhoto.setImageDrawable(context.resources.getDrawable(R.drawable.withowner))
            }
        }
binding.fname.text=animal.file_name
        binding.root.setOnClickListener{
            clicklistener(animal,"upload")

        }
       binding.view.setOnClickListener{
            clicklistener(animal,"view")



        }
    }

}


