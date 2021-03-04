package smsinfosolutions.ind.hibl


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smsinfosolutions.ind.hibl.databinding.ActivityLoginBinding
import smsinfosolutions.ind.hibl.utilities.AppPreferences
import smsinfosolutions.ind.hibl.utilities.Service
import smsinfosolutions.ind.hibl.utilities.ServiceBuilder
import smsinfosolutions.ind.hibl.utilities.showMsg
import java.util.*


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var flag = false
    private lateinit var pwd: String
    private lateinit var id: String
    private lateinit var usertype: String
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.continueLogin.setOnClickListener {

            if (flag) {
                val mobile_no = binding.mobileno.editText?.text.toString()
                var password =
                    if (pwd == "N") binding.otp.editText?.text.toString() else binding.password.editText?.text.toString()
                val login_type = if (pwd == "N") "OTP" else "PWD"
                validateLogin(id, usertype, mobile_no, login_type, password)
            } else {
                getOTP()
            }
        }


    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ContextUtils.updateLocale(newBase!!, AppPreferences.applang!!))
    }

    /*  override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
          if (Build.VERSION.SDK_INT >= 21&& Build.VERSION.SDK_INT <= 25) {
              //Use you logic to update overrideConfiguration locale
              val locale = Locale("hi")//your own implementation here;
              overrideConfiguration?.locale=locale
          }
          super.applyOverrideConfiguration(overrideConfiguration);
      }*/


    fun getOTP() {
        val request = ServiceBuilder.buildService(Service::class.java)
        val mobile_no = binding.mobileno.editText?.text.toString()
        val call = request.checkMobile(mobile_no)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val json_data = response.body()!!.string()
                    val obj = JSONObject(json_data)
                    val success = obj.getBoolean("success")
                    if (success) {
                        flag = true
                        val data = obj.getJSONObject("data")
                        pwd = data.getString("Password")
                        id = data.getString("Id")
                        usertype = data.getString("UserType")
                        if (pwd == "N") {
                            //  startActivity(Intent(this@LoginActivity,DashboardActivity::class.java))
                            TransitionManager.beginDelayedTransition(binding.transitionLayout)
                            binding.otp.visibility = View.VISIBLE
                            binding.continueLogin.text = getString(R.string.login)


                        } else {
                            binding.password.visibility = View.VISIBLE
                            binding.continueLogin.text = getString(R.string.login)

                        }

                    } else {
                        val data = obj.getString("data");
                        showMsg(data)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                Log.d("tag...", t.message);

            }
        })
    }


    fun validateLogin(
        id: String,
        user_type: String,
        mobile_no: String,
        login_type: String,
        passwod: String
    ) {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.validateLogin(id, user_type, mobile_no, login_type, passwod)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val json_data = response.body()!!.string()
                    val obj = JSONObject(json_data)
                    val success = obj.getBoolean("success")
                    if (success) {
                        val data = obj.getJSONObject("data")
                        AppPreferences.isLogin = true
                        AppPreferences.userid = id
                        AppPreferences.usertype = usertype
                        AppPreferences.username = data.getString("Name")
                        AppPreferences.city_id = data.getString("CityId")
                        AppPreferences.city_name = data.getString("District")
                        AppPreferences.hospital_id = data.getString("HospitalId")
                        AppPreferences.hospital_name = data.getString("Hospital")
                        AppPreferences.email = data.getString("EmailId")
                        AppPreferences.mobile = data.getString("Mobile")
                        AppPreferences.bank = data.getString("Bank")
                        AppPreferences.branch = data.getString("Branch")
                        AppPreferences.account = data.getString("AccountNo")
                        AppPreferences.ifsc = data.getString("IFSC")
                        if (pwd == "N") {
                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    CreatePasswordActivity::class.java
                                )
                            )
                            finish()
                        } else {
                            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                            finish()
                        }

                    } else {
                        val data = obj.getString("data");
                        showMsg(data)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                Log.d("tag...", t.message);

            }
        })
    }
}
