package smsinfosolutions.ind.hibl.claim

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import okhttp3.ResponseBody
import smsinfosolutions.ind.hibl.utilities.AnimalImages
import smsinfosolutions.ind.hibl.utilities.LovData
import smsinfosolutions.ind.hibl.utilities.LovDataList
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smsinfosolutions.ind.hibl.DashboardActivity
import smsinfosolutions.ind.hibl.database.DatabaseHelper
import smsinfosolutions.ind.hibl.databinding.ActivityClaimBinding
import smsinfosolutions.ind.hibl.registration.UploadAnimalImageActivity
import smsinfosolutions.ind.hibl.utilities.*
import java.util.*

class ClaimActivity : AppCompatActivity() {
    lateinit var binding: ActivityClaimBinding
    private val PICK_PROPOSAL_REQUEST = 11
    private val PICK_BANK_REQUEST = 12
    private val PICK_BANK_REQUEST2 = 13
    private var storage_permission = false
    private val STORAGE_PERMISSION_CODE = 123
    private var uri: Uri? = null
    private var proposal_img: String? = null
    private var bank_images: Array<String?> = arrayOfNulls(2)
    private var animal_images: MutableList<AnimalImages> = arrayListOf()
    private lateinit var  db:DatabaseHelper

    val items = listOf("Death", "PTD")

    private lateinit var cities_lov: List<LovData>
    private lateinit var hospitals_lov: List<LovData>
    private var city_code: String? = null
    private var hospital_code: String? = null
    var app_permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        db = DatabaseHelper(this)
        binding.dateLayout.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    binding.deathDate.editText?.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)
                },
                year,
                month,
                day
            )
            dpd.show()

        }
        requestStoragePermission()

        binding.proposalNoUpload.setOnClickListener {
            val mimetype = arrayOf("image/*")
            showFileChooser(mimetype, PICK_PROPOSAL_REQUEST)
        }
        binding.bankDetail1.setOnClickListener {
            val mimetype = arrayOf("image/*")
            showFileChooser(mimetype, PICK_BANK_REQUEST)
        }
        binding.bankDetail2.setOnClickListener {
            val mimetype = arrayOf("image/*")
            showFileChooser(mimetype, PICK_BANK_REQUEST2)
        }
        binding.uploadImages.setOnClickListener {

            if (binding.proposalNo.editText?.text.toString().isBlank()) {
                showMsg("Please enter Proposal no.")
                return@setOnClickListener
            }
            if (binding.tagNo.editText?.text.toString().isNullOrBlank()) {
                showMsg("Please enter Tag no.")
                return@setOnClickListener
            }
            startActivity(Intent(this@ClaimActivity, UploadAnimalImageActivity::class.java).apply {
                putExtra("proposal_no", binding.proposalNo.editText?.text.toString())
                putExtra("tag_no", binding.tagNo.editText?.text.toString())
                putExtra("max_limit", 6)
            })
        }

        binding.save.setOnClickListener {
            if (binding.proposalNo.editText?.text.toString().isNullOrBlank()) {
                showMsg("Please enter Proposal no.")
                return@setOnClickListener
            }
            if (proposal_img.isNullOrBlank()) {
                showMsg("Please upload Proposal form image.")
                return@setOnClickListener
            }
            if (binding.tagNo.editText?.text.toString().isNullOrBlank()) {
                showMsg("Please enter Tag no.")
                return@setOnClickListener
            }
            if (binding.tagNo.editText?.text.toString().length !=12) {
                showMsg("Tag no must be 12 digits.")
                return@setOnClickListener
            }

            if (binding.deathDate.editText?.text.toString().isNullOrBlank()) {
                showMsg("Please select animal death date")
                return@setOnClickListener
            }
            if (binding.cityTxt.text.toString().isNullOrBlank()) {
                showMsg("Please select city")
                return@setOnClickListener
            }
            if (binding.hospitalTxt.text.toString().isNullOrBlank()) {
                showMsg("Please select hospital")
                return@setOnClickListener
            }
            if (binding.mobileno.editText?.text.toString().isNullOrBlank()) {
                showMsg("Please enter mobile no.")
                return@setOnClickListener
            }
            if (binding.claimForTxt.text.toString().isNullOrBlank()) {
                showMsg("Please select claim for")
                return@setOnClickListener
            }

            if(bank_images[0].isNullOrBlank())
            {
                showMsg("Please upload atleast one photo for bank detail.")
                return@setOnClickListener
            }



            db?.let {
                animal_images = it.readAnimalImages(binding.proposalNo.editText?.text.toString(), binding.tagNo.editText?.text.toString())

            }

            if(animal_images.size<1)
            {
                showMsg("Please select atleast one photo of animal")
                return@setOnClickListener
            }

            val gson = Gson()
           val animal_images_data= gson.toJson(animal_images)

           saveData(animal_images_data)


        }

        binding.cityTxt.setOnItemClickListener { adapterView, view, i, l ->
            city_code = cities_lov[i].id
            city_code?.let {
                binding.hospitalTxt.setText("")
                getHospital(it)
            }
        }
        binding.hospitalTxt.setOnItemClickListener { adapterView, view, i, l ->
            hospital_code = hospitals_lov[i].id

        }

        val adapter = ArrayAdapter(
            this@ClaimActivity,
            android.R.layout.simple_list_item_1,
            items
        )
        (binding.claimFor.editText as? AutoCompleteTextView)?.setAdapter(adapter)


        getCities()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> onBackPressed()

        }
        return super.onOptionsItemSelected(item)
    }


    private fun showFileChooser(mime_type: Array<String>, request: Int) {
       if (storage_permission) {
//            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
//            intent.putExtra(Intent.EXTRA_MIME_TYPES, mime_type)
//            intent.type = "*/*"
//            startActivityForResult(intent, request)
           startActivityForResult(Utils.getPickImageChooserIntent(this), request)
        } else {

        }
    }


    private fun requestStoragePermission(): Boolean {
        val permission_needed: MutableList<String> = ArrayList()
        for (perm in app_permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    perm
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permission_needed.add(perm)
            }
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        if (!permission_needed.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permission_needed.toTypedArray(),
                STORAGE_PERMISSION_CODE
            )
            storage_permission = false
            return false
        } else {
            /*   val filepath =
                   Environment.getExternalStorageDirectory().toString() + "/Japps/Media/Temp"
               val folder = File(filepath)
               if (!folder.exists()) {
                   folder.mkdirs()
               }*/
            storage_permission = true
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_PERMISSION_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    storage_permission = false
                } else {
                    storage_permission = true
                }
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PROPOSAL_REQUEST && resultCode == RESULT_OK && data != null ) {
            var bitmap: Bitmap?
            var isCamera = true
            if (data.data != null) {
                val action = data.action
                isCamera = action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
            }


            if (isCamera) {
                bitmap = data.extras?.get("data") as Bitmap?
            } else {
                uri = data.data
                bitmap = Utils.decodeUri(this, uri, 200)
            }

            bitmap?.let {
                binding.proposalNoUpload.setImageBitmap(bitmap)
                proposal_img = Utils.Bitmap_to_base64(bitmap)

            }
        } else if (requestCode == PICK_BANK_REQUEST && resultCode == RESULT_OK && data != null ) {

            var bitmap: Bitmap?
            var isCamera = true
            if (data.data != null) {
                val action = data.action
                isCamera = action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
            }


            if (isCamera) {
                bitmap = data.extras?.get("data") as Bitmap?
            } else {
                uri = data.data
                bitmap = Utils.decodeUri(this, uri, 200)
            }

            bitmap?.let {
                val file = Utils.Bitmap_to_base64(bitmap)
                bank_images[0] = file
                binding.bankDetail1.setImageBitmap(bitmap)
            }
        } else if (requestCode == PICK_BANK_REQUEST2 && resultCode == RESULT_OK && data != null ) {


            var bitmap: Bitmap?
            var isCamera = true
            if (data.data != null) {
                val action = data.action
                isCamera = action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
            }


            if (isCamera) {
                bitmap = data.extras?.get("data") as Bitmap?
            } else {
                uri = data.data
                bitmap = Utils.decodeUri(this, uri, 200)
            }

            bitmap?.let {
                val file = Utils.Bitmap_to_base64(bitmap)
                bank_images[1] = file
                binding.bankDetail2.setImageBitmap(bitmap)
            }
        }
    }

    fun getCities() {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getCities(AppPreferences.userid, AppPreferences.usertype)
        call.enqueue(object : Callback<LovDataList> {
            override fun onResponse(call: Call<LovDataList>, response: Response<LovDataList>) {
                if (response.isSuccessful) {
                    cities_lov = response.body()!!.data

                    val adapter = ArrayAdapter(
                        this@ClaimActivity,
                        android.R.layout.simple_list_item_1,
                        cities_lov.map { it.name })
                    (binding.city.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                }
            }

            override fun onFailure(call: Call<LovDataList>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                Log.d("tag...", t.message);

            }
        })
    }

    fun getHospital(city_id: String) {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getHospitals(city_id, AppPreferences.userid, AppPreferences.usertype)
        call.enqueue(object : Callback<LovDataList> {
            override fun onResponse(call: Call<LovDataList>, response: Response<LovDataList>) {
                if (response.isSuccessful) {
                    hospitals_lov = response.body()!!.data

                    val adapter = ArrayAdapter(
                        this@ClaimActivity,
                        android.R.layout.simple_list_item_1,
                        hospitals_lov.map { it.name })
                    (binding.hospital.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                }
            }

            override fun onFailure(call: Call<LovDataList>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                Log.d("tag...", t.message);

            }
        })
    }


    fun saveData(animal_images_data: String) {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.saveClaimRecord(
            AppPreferences.userid,
            AppPreferences.usertype,
            AppPreferences.mobile,
            binding.proposalNo.editText?.text.toString(),
            binding.tagNo.editText?.text.toString(),
            binding.deathDate.editText?.text.toString(),
            city_code!!,
        hospital_code!!,
        binding.claimForTxt.text.toString(),
        binding.mobileno.editText?.text.toString(),
        proposal_img!!,
        bank_images[0],
        bank_images[1],
        animal_images_data)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val json_data = response.body()!!.string()
                    Log.d("res","res==>"+json_data)
                    val obj = JSONObject(json_data)
                    val success = obj.getBoolean("success")
                    if (success) {
                        val data = obj.getString("data")

                        showMsg("Data saved successfully.")


                        db.deleteAnimal(binding.proposalNo.editText?.text.toString(),
                            binding.tagNo.editText?.text.toString())
                        val intent = Intent(this@ClaimActivity, DashboardActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()

                    } else {
                        val data = obj.getString("data")
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
