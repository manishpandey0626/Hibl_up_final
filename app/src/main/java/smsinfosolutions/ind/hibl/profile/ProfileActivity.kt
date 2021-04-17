package smsinfosolutions.ind.hibl.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smsinfosolutions.ind.hibl.*
import smsinfosolutions.ind.hibl.databinding.ActivityProfileBinding
import smsinfosolutions.ind.hibl.payment.PaymentListAdapter
import smsinfosolutions.ind.hibl.utilities.*

class ProfileActivity : BaseActivity() {
    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        binding.name.text = AppPreferences.username
        binding.mobileno.text = AppPreferences.mobile
        binding.email.text = AppPreferences.email
        binding.designation.text = AppPreferences.usertype
        binding.bank.text = AppPreferences.bank
        binding.branch.text = AppPreferences.branch
        binding.accountno.text = AppPreferences.account
        binding.ifsc.text = AppPreferences.ifsc
        binding.hospital.text = AppPreferences.hospital_name
        binding.district.text = AppPreferences.city_name


        binding.language.setOnClickListener{
            val items = arrayOf("हिंदी", "English")

            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.choose_language))
                .setItems(items) { dialog, which ->
                   if(which==1)
                   {
                       AppPreferences.applang = "en"

                   }
                    else
                   {
                       AppPreferences.applang = "hi"
                   }
                    startActivity(Intent(this@ProfileActivity, DashboardActivity::class.java).apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) })
                }
                .show()
        }
 /*       binding.hindi.setOnClickListener {
            AppPreferences.applang = "hi"

            //  ContextUtils.updateLocale(this, AppPreferences.applang!!)
          startActivity(Intent(this@ProfileActivity, DashboardActivity::class.java).apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) })
            finish()
        }
        binding.english.setOnClickListener {
            AppPreferences.applang = "en"
            startActivity(Intent(this@ProfileActivity, DashboardActivity::class.java).apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) })
        }*/

      if(AppPreferences.usertype!="ADMIN") {
          binding.insuredLayout.visibility= View.VISIBLE
          getInsDetails()
      }
        else
      {
          binding.insuredLayout.visibility=View.GONE
      }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun getInsDetails() {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getVoInsDetails(AppPreferences.userid, AppPreferences.usertype)
        call.enqueue(object : Callback<InsDetails> {
            override fun onResponse(
                call: Call<InsDetails>,
                response: Response<InsDetails>
            ) {
                if (response.isSuccessful) {
                 binding.noOfAnimalsInsBig.text=response.body()!!.bIG
                 binding.noOfAnimalsInsSmall.text=response.body()!!.sMALL
                 binding.paidAmt.text=response.body()!!.paid
                 binding.paidDt.text=response.body()!!.paidDt
                 binding.balanceAmt.text=response.body()!!.balance
                }
            }

            override fun onFailure(call: Call<InsDetails>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                Log.d("tag...", t.message);

            }
        })
    }

}