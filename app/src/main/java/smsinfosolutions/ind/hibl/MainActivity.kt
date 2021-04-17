package smsinfosolutions.ind.hibl


import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import smsinfosolutions.ind.hibl.admin.AdminDashboardActivity
import smsinfosolutions.ind.hibl.report.ReportActivity
import smsinfosolutions.ind.hibl.utilities.AppPreferences
import smsinfosolutions.ind.hibl.utilities.Utils

class MainActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 4000
    lateinit var player:MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        player= MediaPlayer()
          Utils.play(this,player,"Buffalo.wav")
        Handler().postDelayed({ // This method will be executed once the timer is over
            // Start your app main activity
            //shortcutAdd("Mid-day", 1);
            // showMsg(" msg ${AppPreferences.isLogin}")
            if (AppPreferences.isLogin) {
               // val i = Intent(this@MainActivity, DashboardActivity::class.java)

                   if(AppPreferences.usertype=="ADMIN") {
                       val i = Intent(this@MainActivity, AdminDashboardActivity::class.java)
                       startActivity(i)
                   }
                else
                   {
                       val i = Intent(this@MainActivity, DashboardActivity::class.java)
                       startActivity(i)

                   }

                finish()
            } else {
                val i = Intent(this@MainActivity, LanguageSelectionActivity::class.java)
                startActivity(i)
                finish()
            }

            // close this activity
            finish()
        }, SPLASH_TIME_OUT.toLong())





    }

    override fun onPause() {
        super.onPause()
        Utils.stop(this,player)
    }
}
