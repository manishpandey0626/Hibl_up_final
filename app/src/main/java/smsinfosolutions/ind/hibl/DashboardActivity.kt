package smsinfosolutions.ind.hibl

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smsinfosolutions.ind.hibl.claim.ClaimActivity
import smsinfosolutions.ind.hibl.claim.ClaimOptionActivity
import smsinfosolutions.ind.hibl.completed.CompletedFormActivity
import smsinfosolutions.ind.hibl.database.DatabaseHelper
import smsinfosolutions.ind.hibl.databinding.ActivityDashboardBinding
import smsinfosolutions.ind.hibl.payment.PaymentListActivity
import smsinfosolutions.ind.hibl.pending.PendingFormActivity
import smsinfosolutions.ind.hibl.profile.ProfileActivity
import smsinfosolutions.ind.hibl.registration.NewAnimalRegistrationActivity
import smsinfosolutions.ind.hibl.send.SendFormActivity
import smsinfosolutions.ind.hibl.utilities.AppPreferences
import smsinfosolutions.ind.hibl.utilities.Service
import smsinfosolutions.ind.hibl.utilities.ServiceBuilder
import smsinfosolutions.ind.hibl.utilities.showMsg

class DashboardActivity : BaseActivity() {
    private lateinit var binding: ActivityDashboardBinding

    lateinit var db: DatabaseHelper
    var completed_cnt: String = "0"
    var payment_cnt: String = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DatabaseHelper(this)

        binding.userName.text = AppPreferences.username
        binding.newAnimalRegistration.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, NewAnimalRegistrationActivity::class.java))
        }

        binding.payment.setOnClickListener {
            val intent = Intent(this@DashboardActivity, PaymentListActivity::class.java)
            startActivity(intent)
        }
        binding.pendingForm.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, PendingFormActivity::class.java))
        }
        binding.send.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, SendFormActivity::class.java))

        }
        binding.completedForm.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, CompletedFormActivity::class.java))

        }
        binding.claim.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, ClaimOptionActivity::class.java))

        }


        binding.logout.setOnClickListener {
            AppPreferences.isLogin = false
            val intent = Intent(this, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(intent)
            finish()

        }
        binding.profile.setOnClickListener {

            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        //getCount()

    }

    fun getCount() {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getCount(AppPreferences.userid, AppPreferences.usertype)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val json_data = response.body()!!.string()
                    val obj = JSONObject(json_data)

                    val data = obj.getJSONObject("data")
                    completed_cnt = data.getString("Comp_Count")
                    payment_cnt = data.getString("Pay_Count")
                    binding.paymentCount.text = payment_cnt
                    binding.completedCount.text = completed_cnt

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
               // Log.d("tag...", t.message);

            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.pendingCount.text = db.pendingCount().toString()
        binding.sendCount.text = db.pendingCount("Y").toString()
        getCount()
    }

  /*  override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ContextUtils.updateLocale(newBase!!, AppPreferences.applang!!))
    }*/
}