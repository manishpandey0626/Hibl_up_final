package smsinfosolutions.ind.hibl



import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import smsinfosolutions.ind.hibl.databinding.ActivityLanguageSelectionBinding
import smsinfosolutions.ind.hibl.utilities.AppPreferences


class LanguageSelectionActivity : AppCompatActivity() {
    var language_dialog: TextView? = null
    var text1: TextView? = null
    var lang_selected = false
    var context: Context? = null

    private lateinit var binding: ActivityLanguageSelectionBinding
   // var resources: Resources? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= ActivityLanguageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)


       binding.hindi.setOnClickListener{

           AppPreferences.applang="hi"
           startActivity(Intent(this@LanguageSelectionActivity,LoginActivity::class.java))
           finish()
       }

       binding.english.setOnClickListener{

           AppPreferences.applang="en"
           startActivity(Intent(this@LanguageSelectionActivity,LoginActivity::class.java))
           finish()
       }
    }
}