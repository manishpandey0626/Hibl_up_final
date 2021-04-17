package smsinfosolutions.ind.hibl.pending

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import smsinfosolutions.ind.hibl.utilities.SendUserData
import smsinfosolutions.ind.hibl.database.DatabaseHelper
import smsinfosolutions.ind.hibl.databinding.ActivityPendingFormBinding
import smsinfosolutions.ind.hibl.registration.NewAnimalRegistrationActivity


class PendingFormActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPendingFormBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPendingFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val db = DatabaseHelper(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerView.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.recyclerView.adapter = PendingListAdapter(db.getProposalList(), { itemClicked(it) })
    }

    private fun itemClicked(user: SendUserData) {
        val intent=Intent(this@PendingFormActivity, NewAnimalRegistrationActivity::class.java)
        intent.putExtra("proposal_no",user.proposal_no)
        startActivity(intent)




    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.getItemId()) {
            android.R.id.home ->onBackPressed()

        }
        return super.onOptionsItemSelected(item)
    }
}