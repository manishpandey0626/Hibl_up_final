package smsinfosolutions.ind.hibl.registration

/**
 * Created by Manish on 01-Feb-21.
 */

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.healthymantra.piousvision.utilities.AnimalDetail
import org.healthymantra.piousvision.utilities.AnimalImages

import smsinfosolutions.ind.hibl.R
import smsinfosolutions.ind.hibl.databinding.AnimalImageItemBinding
import smsinfosolutions.ind.hibl.utilities.Utils


class AnimalImageAdapter(val animals: List<AnimalImages>,val clicklistener: (AnimalImages,String) -> Unit,val context:Context): RecyclerView.Adapter<AnimalImageViewHolder>() {
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



    fun bind(animal: AnimalImages,clicklistener: (AnimalImages,String) -> Unit,context:Context) {
binding.animalPhoto.setImageBitmap(Utils.Base64_to_bitmap(animal.file, context ))

        binding.root.setOnClickListener{
            clicklistener(animal,"view")

        }
        binding.delete.setOnClickListener{
            clicklistener(animal,"delete")



        }
    }

}


