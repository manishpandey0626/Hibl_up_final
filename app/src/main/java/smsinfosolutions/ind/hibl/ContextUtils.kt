package smsinfosolutions.ind.hibl

import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import java.util.*

/**
 * Created by Manish on 06-Jan-21.
 */
class ContextUtils(base: Context) : ContextWrapper(base) {

    companion object {

        fun updateLocale(c: Context, language: String): ContextWrapper {
          /*  var context = c
            val resources: Resources = context.resources
            val configuration: Configuration = resources.configuration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val localeList = LocaleList(localeToSwitchTo)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
            } else {
                //configuration.locale = localeToSwitchTo
                configuration.setLocale(localeToSwitchTo)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                context = context.createConfigurationContext(configuration)
            } else {
                resources.updateConfiguration(configuration, resources.displayMetrics)
            }
            return ContextUtils(context)*/
            var context = c
            val config = context.resources.configuration
           if (language != "") {
               val locale = Locale(language)
               Locale.setDefault(locale)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setSystemLocale(config, locale)
                } else {
                    setSystemLocaleLegacy(config, locale)
                }
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    config.setLayoutDirection(locale)
                    context = context.createConfigurationContext(config)
                } else {
                    context.resources.updateConfiguration(config, context.resources.displayMetrics)
                }
         }
            return ContextUtils(context)
        }

        @SuppressWarnings("deprecation")
        fun setSystemLocaleLegacy(config: Configuration, locale: Locale) {
            //config.locale = locale
            config.setLocale(locale)
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun setSystemLocale(config: Configuration, locale: Locale) {
            config.setLocale(locale)
        }
    }
}