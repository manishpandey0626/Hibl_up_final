package smsinfosolutions.ind.hibl.claim


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import smsinfosolutions.ind.hibl.utilities.Claim
import smsinfosolutions.ind.hibl.utilities.ClaimList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smsinfosolutions.ind.hibl.databinding.ActivityClaimStatusBinding
import smsinfosolutions.ind.hibl.utilities.AppPreferences
import smsinfosolutions.ind.hibl.utilities.Service
import smsinfosolutions.ind.hibl.utilities.ServiceBuilder
import smsinfosolutions.ind.hibl.utilities.showMsg


class ClaimStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClaimStatusBinding
    private lateinit var adapter: ClaimListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        binding.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )







        getClaimStatus()


    }


    fun getClaimStatus() {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getClaimList(AppPreferences.userid, AppPreferences.usertype,AppPreferences.mobile)
        call.enqueue(object : Callback<ClaimList> {
            override fun onResponse(
                call: Call<ClaimList>,
                response: Response<ClaimList>
            ) {
                if (response.isSuccessful) {
                    binding.shimmerFrameLayout.stopShimmer()
                    binding.shimmerFrameLayout.visibility= View.GONE
                    adapter = ClaimListAdapter(response.body()!!.claimList,{ c->itemClicked(c)})
                    binding.recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ClaimList>, t: Throwable) {
                binding.shimmerFrameLayout.stopShimmer()
                binding.shimmerFrameLayout.visibility= View.GONE
                showMsg("on FAilure ${t.message}")


            }
        })
    }

    fun itemClicked(data: Claim)
    {
        startActivity(Intent(this@ClaimStatusActivity, ClaimStatusDetailActivity::class.java).apply {
            putExtra("tag_no",data.tagNo)
                    })

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> onBackPressed()

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerFrameLayout.startShimmer()
    }
    override fun onPause() {
        binding.shimmerFrameLayout.stopShimmer()
        super.onPause()
    }
}
