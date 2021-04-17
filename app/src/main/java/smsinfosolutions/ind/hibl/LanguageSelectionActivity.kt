package smsinfosolutions.ind.hibl



import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import smsinfosolutions.ind.hibl.databinding.ActivityLanguageSelectionBinding
import smsinfosolutions.ind.hibl.utilities.AppPreferences
import smsinfosolutions.ind.hibl.utilities.Utils


class LanguageSelectionActivity : AppCompatActivity() {
    var language_dialog: TextView? = null
    var text1: TextView? = null
    var lang_selected = false
    var context: Context? = null
     lateinit var player:MediaPlayer
    private lateinit var binding: ActivityLanguageSelectionBinding
   // var resources: Resources? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= ActivityLanguageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
       player= MediaPlayer()
       Utils.play(this,player,"Welcome.wav")
//       Utils.play(this,player,"select_language.wav")

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

       binding.hSpeak.setOnClickListener{
         Utils.stop(this,player)
           Utils.play(LanguageSelectionActivity@this,player,"for_hindi.wav")

       }

       binding.eSpeak.setOnClickListener{
           Utils.stop(this,player)
           Utils.play(LanguageSelectionActivity@this,player,"for_english.wav")
       }
    }
}