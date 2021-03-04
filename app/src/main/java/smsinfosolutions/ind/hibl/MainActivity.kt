package smsinfosolutions.ind.hibl


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import smsinfosolutions.ind.hibl.utilities.AppPreferences

class MainActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Handler().postDelayed({ // This method will be executed once the timer is over
            // Start your app main activity
            //shortcutAdd("Mid-day", 1);
            // showMsg(" msg ${AppPreferences.isLogin}")
            if (AppPreferences.isLogin) {
                val i = Intent(this@MainActivity, DashboardActivity::class.java)
                startActivity(i)
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
}
