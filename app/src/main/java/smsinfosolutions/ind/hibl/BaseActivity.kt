package smsinfosolutions.ind.hibl


import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import smsinfosolutions.ind.hibl.utilities.AppPreferences


/**
 * Created by Manish on 06-Jan-21.
 */
open class BaseActivity: AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
// get chosen language from shread preference

        val localeToSwitchTo = AppPreferences.applang
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase,localeToSwitchTo!!)
        super.attachBaseContext(localeUpdatedContext)
    }



}