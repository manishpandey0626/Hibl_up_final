package smsinfosolutions.ind.hibl.utilities

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Manish on 09-Dec-20.
 */

object AppPreferences {
    private const val NAME = "Hibl"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    // list of app specific preferences
    private val IS_FIRST_RUN_PREF = Pair("is_first_run", false)
    private val IS_LOGIN = Pair("is_login", false)
    private val USER_ID=Pair("user_id","")
    private val USER_NAME=Pair("user_name","")
    private val USER_TYPE=Pair("user_type","")
    private val TOKEN = Pair("token","")
    private val EMAIL= Pair("email","")
    private val PROFILE_PHOTO=Pair("profile_photo","")
    private val APP_LANGUAGE=Pair("app_language","en")
    private val HOSPITAL_ID=Pair("hospital_id","")
    private val HOSPITAL_NAME=Pair("hospital_name","")
    private val CITY_ID=Pair("city_id","")
    private val CITY_NAME=Pair("city_name","")
    private val MOBILE=Pair("mobile","")
    private val BANK=Pair("bank","")
    private val BRANCH=Pair("branch","")
    private val ACCOUNT=Pair("account","")
    private val IFSC=Pair("ifsc","")

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var firstRun: Boolean
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getBoolean(IS_FIRST_RUN_PREF.first, IS_FIRST_RUN_PREF.second)

        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putBoolean(IS_FIRST_RUN_PREF.first, value)
        }

    var isLogin: Boolean
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getBoolean(IS_LOGIN.first, IS_LOGIN.second)

        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putBoolean(IS_LOGIN.first, value)

        }

    var userid: String
     get()= preferences.getString(USER_ID.first, USER_ID.second)!!
     set(value)= preferences.edit{
         it.putString(USER_ID.first,value)
     }

    var username: String?
        get()= preferences.getString(USER_NAME.first, USER_NAME.second)
        set(value)= preferences.edit{
            it.putString(USER_NAME.first,value)
        }

    var usertype: String
        get()= preferences.getString(USER_TYPE.first, USER_TYPE.second)!!
        set(value)= preferences.edit{
            it.putString(USER_TYPE.first,value)
        }

    var hospital_id: String
        get()= preferences.getString(HOSPITAL_ID.first, HOSPITAL_ID.second)!!
        set(value)= preferences.edit{
            it.putString(HOSPITAL_ID.first,value)
        }

    var hospital_name: String
        get()= preferences.getString(HOSPITAL_NAME.first, HOSPITAL_NAME.second)!!
        set(value)= preferences.edit{
            it.putString(HOSPITAL_NAME.first,value)
        }
    var city_name: String
        get()= preferences.getString(CITY_NAME.first, CITY_NAME.second)!!
        set(value)= preferences.edit{
            it.putString(CITY_NAME.first,value)
        }
    var city_id: String
        get()= preferences.getString(CITY_ID.first, CITY_ID.second)!!
        set(value)= preferences.edit{
            it.putString(CITY_ID.first,value)
        }
    var token: String?
        get()= preferences.getString(TOKEN.first, TOKEN.second)
        set(value)= preferences.edit{
            it.putString(TOKEN.first,value)
        }

    var email: String
        get()= preferences.getString(EMAIL.first, EMAIL.second)!!
        set(value)= preferences.edit{
            it.putString(EMAIL.first,value)
        }
    var mobile: String
        get()= preferences.getString(MOBILE.first, MOBILE.second)!!
        set(value)= preferences.edit{
            it.putString(MOBILE.first,value)
        }
    var bank: String
        get()= preferences.getString(BANK.first, BANK.second)!!
        set(value)= preferences.edit{
            it.putString(BANK.first,value)
        }
    var branch: String
        get()= preferences.getString(BRANCH.first, BRANCH.second)!!
        set(value)= preferences.edit{
            it.putString(BRANCH.first,value)
        }
    var account: String
        get()= preferences.getString(ACCOUNT.first, ACCOUNT.second)!!
        set(value)= preferences.edit{
            it.putString(ACCOUNT.first,value)
        }
    var ifsc: String
        get()= preferences.getString(IFSC.first, IFSC.second)!!
        set(value)= preferences.edit{
            it.putString(IFSC.first,value)
        }
    var profilepic: String?
        get()= preferences.getString(PROFILE_PHOTO.first, PROFILE_PHOTO.second)
        set(value)= preferences.edit{
            it.putString(PROFILE_PHOTO.first,value)
        }
    var applang: String?
        get()= preferences.getString(APP_LANGUAGE.first, APP_LANGUAGE.second)
        set(value)= preferences.edit{
            it.putString(APP_LANGUAGE.first,value)
        }
}