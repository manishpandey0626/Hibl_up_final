package smsinfosolutions.ind.hibl.claim

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import smsinfosolutions.ind.hibl.databinding.ActivityClaimOptionBinding

class ClaimOptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClaimOptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimOptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        binding.claimIntimation.setOnClickListener {
            startActivity(Intent(this@ClaimOptionActivity, ClaimActivity::class.java))
        }
        binding.claimStatus.setOnClickListener {
            startActivity(Intent(this@ClaimOptionActivity, ClaimStatusActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> onBackPressed()

        }
        return super.onOptionsItemSelected(item)
    }
}