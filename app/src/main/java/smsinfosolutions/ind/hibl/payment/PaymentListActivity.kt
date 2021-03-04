package smsinfosolutions.ind.hibl.payment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.healthymantra.piousvision.utilities.PaymentDetailList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smsinfosolutions.ind.hibl.databinding.ActivityPaymentListBinding
import smsinfosolutions.ind.hibl.utilities.AppPreferences
import smsinfosolutions.ind.hibl.utilities.Service
import smsinfosolutions.ind.hibl.utilities.ServiceBuilder
import smsinfosolutions.ind.hibl.utilities.showMsg


class PaymentListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentListBinding
    private lateinit var adapter: PaymentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentListBinding.inflate(layoutInflater)
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


        binding.save.setOnClickListener {

            val selectedRecords = adapter.getSelectedRecords()
            if (selectedRecords.size > 0) {
                val total_bal = selectedRecords.map { (it.balance).toDouble() }.sum()
                val proposal_nos = selectedRecords.joinToString { it.proposal_no }
                val intent = Intent(
                    this@PaymentListActivity,
                    FinalPaymentActivity::class.java
                )
                intent.putExtra("total_bal", total_bal)
                intent.putExtra("proposal_nos", proposal_nos)
                startActivity(
                    intent
                )
            } else {
                showMsg("Please select atleast one record")
            }
        }



        getPaymentList()

    }


    fun getPaymentList() {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getPaymentList(AppPreferences.userid, AppPreferences.usertype)
        call.enqueue(object : Callback<PaymentDetailList> {
            override fun onResponse(
                call: Call<PaymentDetailList>,
                response: Response<PaymentDetailList>
            ) {
                if (response.isSuccessful) {

                    adapter = PaymentListAdapter(response.body()!!.payment_list)
                    binding.recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<PaymentDetailList>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                Log.d("tag...", t.message);

            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.getItemId()) {
            android.R.id.home ->onBackPressed()

        }
        return super.onOptionsItemSelected(item)
    }
}