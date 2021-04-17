package smsinfosolutions.ind.hibl.admin

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smsinfosolutions.ind.hibl.BaseActivity
import smsinfosolutions.ind.hibl.LoginActivity
import smsinfosolutions.ind.hibl.claim.ClaimStatusDetailActivity
import smsinfosolutions.ind.hibl.databinding.ActivityAdminDashboardBinding
import smsinfosolutions.ind.hibl.profile.ProfileActivity
import smsinfosolutions.ind.hibl.report.ReportActivity
import smsinfosolutions.ind.hibl.utilities.*

class AdminDashboardActivity : BaseActivity() {
    private lateinit var binding: ActivityAdminDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.userName.text = AppPreferences.username



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
        binding.recyclerView.layoutManager = GridLayoutManager(this@AdminDashboardActivity, 2)
        getAdminDashboard()
    }

    fun getAdminDashboard() {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getAdminDashboard(AppPreferences.mobile, AppPreferences.usertype)
        call.enqueue(object : Callback<AdminDashboard> {
            override fun onResponse(
                call: Call<AdminDashboard>,
                response: Response<AdminDashboard>
            ) {
                if (response.isSuccessful) {

                    val data = response.body()!!.data
                    binding.recyclerView.adapter = AdminDashboardAdapter(data,{itemClicked(it)})
                }
            }

            override fun onFailure(call: Call<AdminDashboard>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                // Log.d("tag...", t.message);

            }
        })
    }

    override fun onResume() {
        super.onResume()

    }

    fun itemClicked(data: AdminDashboardItem)
    {
        startActivity(Intent(this@AdminDashboardActivity, ReportActivity::class.java).apply {
            putExtra("tile_no",data.iD)
            putExtra("title",data.tile)
        })

    }
}