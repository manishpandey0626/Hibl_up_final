package smsinfosolutions.ind.hibl

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smsinfosolutions.ind.hibl.databinding.ActivityCreatePasswordBinding
import smsinfosolutions.ind.hibl.utilities.AppPreferences
import smsinfosolutions.ind.hibl.utilities.Service
import smsinfosolutions.ind.hibl.utilities.ServiceBuilder
import smsinfosolutions.ind.hibl.utilities.showMsg

class CreatePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.updatePassword.setOnClickListener{

           val password= binding.password.editText?.text.toString()
            val cpassword=binding.confirmPassword.editText?.text.toString();

            if(password==cpassword)
            {
                updatePassword(AppPreferences.userid,AppPreferences.usertype,password)
            }
            else
            {
                showMsg("Password and Confirm Password not matched!!")
            }

        }
    }

    fun updatePassword(id: String, user_type: String, passwod: String) {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.updatePassword(id, user_type, passwod)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val json_data = response.body()!!.string()
                    val obj = JSONObject(json_data)
                    val success = obj.getBoolean("success")
                    if (success) {
                        val data = obj.getString("data")

                        startActivity(
                            Intent(
                                this@CreatePasswordActivity,
                                DashboardActivity::class.java
                            )
                        )
                        finish()


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