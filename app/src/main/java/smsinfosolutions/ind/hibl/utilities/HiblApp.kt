package smsinfosolutions.ind.hibl.utilities

/**
 * Created by Manish on 02-Jan-21.
 */
import android.app.Application
import smsinfosolutions.ind.hibl.utilities.AppPreferences.init

/**
 * Created by Manish on 10-Dec-20.
 */
class HiblApp: Application() {
    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)

    }
}