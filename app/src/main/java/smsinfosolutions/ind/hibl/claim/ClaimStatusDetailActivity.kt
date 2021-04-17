package smsinfosolutions.ind.hibl.claim

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import smsinfosolutions.ind.hibl.utilities.ClaimDetailList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smsinfosolutions.ind.hibl.databinding.ActivityClaimStatusDetailBinding
import smsinfosolutions.ind.hibl.utilities.AppPreferences
import smsinfosolutions.ind.hibl.utilities.Service
import smsinfosolutions.ind.hibl.utilities.ServiceBuilder
import smsinfosolutions.ind.hibl.utilities.showMsg

class ClaimStatusDetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityClaimStatusDetailBinding
    private lateinit var adapter: ClaimStatusDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityClaimStatusDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
             val tag_no=   intent.getStringExtra("tag_no")
        getClaimStatus(tag_no)
    }

    fun getClaimStatus(tag_no:String) {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getClaimDetails(
            AppPreferences.userid, AppPreferences.usertype,
            AppPreferences.mobile,tag_no)
        call.enqueue(object : Callback<ClaimDetailList> {
            override fun onResponse(
                call: Call<ClaimDetailList>,
                response: Response<ClaimDetailList>
            ) {
                if (response.isSuccessful) {
                   binding.recyclerView.layoutManager=LinearLayoutManager(this@ClaimStatusDetailActivity,LinearLayoutManager.VERTICAL,false)
                    adapter = ClaimStatusDetailAdapter(response.body()!!.claimDetailList)
                    binding.recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ClaimDetailList>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                Log.d("tag...", t.message);

            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> onBackPressed()

        }
        return super.onOptionsItemSelected(item)
    }
}