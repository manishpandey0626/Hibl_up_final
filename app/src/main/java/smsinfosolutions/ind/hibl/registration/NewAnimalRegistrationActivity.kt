package smsinfosolutions.ind.hibl.registration


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smsinfosolutions.ind.hibl.database.DatabaseHelper
import smsinfosolutions.ind.hibl.databinding.ActivityNewAnimalRegistrationBinding
import smsinfosolutions.ind.hibl.utilities.*
import smsinfosolutions.ind.hibl.utilities.Utils.Companion.Base64_to_bitmap
import smsinfosolutions.ind.hibl.utilities.Utils.Companion.Bitmap_to_base64
import smsinfosolutions.ind.hibl.utilities.Utils.Companion.decodeUri
import smsinfosolutions.ind.hibl.utilities.Utils.Companion.getPickImageChooserIntent
import java.util.*

class NewAnimalRegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewAnimalRegistrationBinding
    private val PICK_PROPOSAL_REQUEST = 11
    private val PICK_AADHAR_REQUEST = 12
    private var storage_permission = false
    private val STORAGE_PERMISSION_CODE = 123
    private var uri: Uri? = null
    private var proposal_img: String? = null
    private var aadhar_img: String? = null
    private lateinit var area_lov:List<LovData>
    private lateinit var category_lov:List<LovData>
    private lateinit var duration_lov:List<LovData>
    private lateinit var proposal_no_lov:List<ProposalNo>
    private lateinit var cities_lov:List<LovData>
    private lateinit var hospitals_lov:List<LovData>
    private lateinit var proposal_no:String
    private  var category_code:String?=null
    private  var area_code:String?=null
    private  var duration_code:String?=null
    private var city_code:String?=null
    private var hospital_code: String? = null
    private var data: UserData? = null
    var app_permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewAnimalRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        val db = DatabaseHelper(this)
       val p_no =intent.getStringExtra("proposal_no")
        getArea()
        getProposalNo()
        getDuration()
        getCategories()
        getCities()
        p_no?.let {

            data= db.readData(p_no)
            data?.let {

                binding.proposalNoTxt.setText(it.proposal_no)
                binding.proposalNoUpload.setImageBitmap(Base64_to_bitmap(it.proposal_img, this))
                binding.adharNo.editText?.setText(it.aadhar_no)
                binding.aadharNoUpload.setImageBitmap(Base64_to_bitmap(it.aadhar_img, this))
                proposal_img=it.proposal_img
                aadhar_img=it.aadhar_img
                binding.noOfAnimals.editText?.setText(it.no_of_animal)
                area_code=it.area
                binding.areaTxt.setText(it.area_name)
                category_code=it.category
                binding.casteTxt.setText(it.category_name)
                duration_code=it.duration
                binding.durationTxt.setText(it.duration_name)
                binding.cityTxt.setText(it.city_name)
                city_code=it.city_id
                binding.hospitalTxt.setText(it.hospital_name)
                hospital_code=it.hospital_id
                binding.wptd.isChecked=it.wptd.toBoolean()
                binding.age.editText?.setText(it.age)

        } }


        if("VO"==AppPreferences.usertype)
        {
            binding.hospitalTxt.setText(AppPreferences.hospital_name)
            hospital_code=AppPreferences.hospital_id
           // binding.hospital.visibility=View.GONE
            binding.cityTxt.setText(AppPreferences.city_name)
            city_code=AppPreferences.city_id
           // binding.city.visibility= View.GONE
        }
        else
        {
            binding.hospital.visibility=View.VISIBLE
            binding.city.visibility=View.VISIBLE
        }

        requestStoragePermission()



        binding.proposalNoUpload.setOnClickListener {
            val mimetype = arrayOf("image/*")
            showFileChooser(mimetype, PICK_PROPOSAL_REQUEST)
        }
        binding.aadharNoUpload.setOnClickListener {
            val mimetype = arrayOf("image/*")
            showFileChooser(mimetype, PICK_AADHAR_REQUEST)
        }

        binding.save.setOnClickListener {


            val result=insertIntoDatabase(db)

            if(result!=-1.toLong())
            {
            showMsg("Data Saved successfully!!")

            }
            else
            {
                showMsg("Something went wrong!!");
            }

        }
        binding.next.setOnClickListener {
            if(binding.proposalNoTxt.text.toString().isEmpty())
            {
                showMsg("Please Select Proposal no.");
                return@setOnClickListener
            }
            if(proposal_img.isNullOrBlank())
            {
             showMsg("Please upload proposal form image.")
                return@setOnClickListener
            }
            if(binding.adharNo.editText?.text.toString().isNullOrBlank())
            {
                showMsg("Pleae enter aadhar no.");
                return@setOnClickListener
            }
            if(aadhar_img.isNullOrBlank())
            {
                showMsg("Please upload Adhar car dimage.")
                return@setOnClickListener
            }
            if(binding.age.editText?.text.toString().isNullOrBlank())
            {
                showMsg("Please enter age.")
                return@setOnClickListener
            }

            if(binding.age.editText?.text.toString().toInt()<18)
            {
                showMsg("Age can not be less than 18")
                return@setOnClickListener
            }
            if(binding.noOfAnimals.editText?.text.toString().isNullOrBlank())
            {
                showMsg("Please enter no of animals")
                return@setOnClickListener
            }
            if(category_code.isNullOrBlank())
            {
                showMsg("Please select Caste")
                return@setOnClickListener
            }
            if(area_code.isNullOrBlank())
            {
                showMsg("Please select Area.")
                return@setOnClickListener
            }
            if(duration_code.isNullOrBlank())
            {
                showMsg("Please select Duration.")
                return@setOnClickListener
            }
            val result=insertIntoDatabase(db)

            if(result!=-1.toLong()) {
                val intent = Intent(
                    this@NewAnimalRegistrationActivity,
                    AnimalRegistraionScreen2Activity::class.java
                )
                intent.putExtra("proposal_no", binding.proposalNoTxt.text.toString())
                intent.putExtra(
                    "no_of_animal",
                    binding.noOfAnimals?.editText?.text.toString().toInt()
                )
                intent.putExtra("area_code", area_code)
                intent.putExtra("category_code", category_code)
                intent.putExtra("duration_code", duration_code)
                intent.putExtra("wptd", binding.wptd.isChecked.toString())
                startActivity(intent)

            }
            else
            {
                showMsg("Something went wrong!!");
            }

        }


        binding.proposalNoTxt.setOnItemClickListener { adapterView, view, i, l ->
            proposal_no=proposal_no_lov[i].name  }
        binding.casteTxt.setOnItemClickListener { adapterView, view, i, l ->
            category_code=category_lov[i].id  }
        binding.areaTxt.setOnItemClickListener { adapterView, view, i, l ->
            area_code=area_lov[i].id  }
        binding.durationTxt.setOnItemClickListener { adapterView, view, i, l ->
            duration_code=duration_lov[i].id  }
        binding.cityTxt.setOnItemClickListener { adapterView, view, i, l ->
            city_code=cities_lov[i].id
            city_code?.let {
                binding.hospitalTxt.setText("")
                getHospital(it)
            }
        }
        binding.hospitalTxt.setOnItemClickListener { adapterView, view, i, l ->
            hospital_code=hospitals_lov[i].id

        }
    }

    private fun showFileChooser(mime_type: Array<String>, request: Int) {
        if (storage_permission) {
//            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            intent.putExtra(Intent.EXTRA_MIME_TYPES, mime_type)
//            intent.type = "*/*"
//            startActivityForResult(intent, request)

            startActivityForResult(getPickImageChooserIntent(this), request)
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
        if (requestCode == PICK_PROPOSAL_REQUEST && resultCode == RESULT_OK && data != null) {
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
                bitmap = decodeUri(this, uri, 200)
            }

            bitmap?.let {
                binding.proposalNoUpload.setImageBitmap(it)
                proposal_img = Bitmap_to_base64(it)

            }
        } else if (requestCode == PICK_AADHAR_REQUEST && resultCode == RESULT_OK && data != null) {
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
                bitmap = decodeUri(this, uri, 200)
            }
            bitmap?.let {
                binding.aadharNoUpload.setImageBitmap(it)
                aadhar_img = Bitmap_to_base64(it)

            }

        }
    }




    fun getArea() {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getArea()
        call.enqueue(object : Callback<LovDataList> {
            override fun onResponse(call: Call<LovDataList>, response: Response<LovDataList>) {
                if (response.isSuccessful) {
                    area_lov = response.body()!!.data

                    val adapter = ArrayAdapter(
                        this@NewAnimalRegistrationActivity,
                        android.R.layout.simple_list_item_1,
                        area_lov.map { it.name })
                    (binding.lwe.editText as? AutoCompleteTextView)?.setAdapter(adapter)

                    /*  data?.let {
                         val code=it.area
                        val area_name= area_lov.filter { it.id.equals(code) }
                        binding.areaTxt.setText(area_name[0].name)
                    }*/

                }
            }

            override fun onFailure(call: Call<LovDataList>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                Log.d("tag...", t.message);

            }
        })
    }

    fun getDuration() {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getDuration()
        call.enqueue(object : Callback<LovDataList> {
            override fun onResponse(call: Call<LovDataList>, response: Response<LovDataList>) {
                if (response.isSuccessful) {
                    duration_lov = response.body()!!.data

                    val adapter = ArrayAdapter(
                        this@NewAnimalRegistrationActivity,
                        android.R.layout.simple_list_item_1,
                        duration_lov.map { it.name })
                    (binding.duration.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                }
            }

            override fun onFailure(call: Call<LovDataList>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                Log.d("tag...", t.message);

            }
        })
    }

    fun getCategories() {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getCategories()
        call.enqueue(object : Callback<LovDataList> {
            override fun onResponse(call: Call<LovDataList>, response: Response<LovDataList>) {
                if (response.isSuccessful) {
                    category_lov = response.body()!!.data

                    val adapter = ArrayAdapter(
                        this@NewAnimalRegistrationActivity,
                        android.R.layout.simple_list_item_1,
                        category_lov.map { it.name })
                    (binding.caste.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                }
            }

            override fun onFailure(call: Call<LovDataList>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                Log.d("tag...", t.message);

            }
        })
    }

    fun getProposalNo() {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getProposalNo(AppPreferences.userid, AppPreferences.usertype)
        call.enqueue(object : Callback<ProposalNoList> {
            override fun onResponse(
                call: Call<ProposalNoList>,
                response: Response<ProposalNoList>
            ) {
                if (response.isSuccessful) {
                    proposal_no_lov = response.body()!!.data

                    val adapter = ArrayAdapter(
                        this@NewAnimalRegistrationActivity,
                        android.R.layout.simple_list_item_1,
                        proposal_no_lov.map { it.name })
                    (binding.proposalNo.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                }
            }

            override fun onFailure(call: Call<ProposalNoList>, t: Throwable) {
                showMsg("on FAilure ${t.message}")
                Log.d("tag...", t.message);

            }
        })
    }

    fun getCities() {
        val request = ServiceBuilder.buildService(Service::class.java)
        val call = request.getCities(AppPreferences.userid,AppPreferences.usertype)
        call.enqueue(object : Callback<LovDataList> {
            override fun onResponse(call: Call<LovDataList>, response: Response<LovDataList>) {
                if (response.isSuccessful) {
                    cities_lov = response.body()!!.data

                    val adapter = ArrayAdapter(
                        this@NewAnimalRegistrationActivity,
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
        val call = request.getHospitals(city_id,AppPreferences.userid,AppPreferences.usertype)
        call.enqueue(object : Callback<LovDataList> {
            override fun onResponse(call: Call<LovDataList>, response: Response<LovDataList>) {
                if (response.isSuccessful) {
                    hospitals_lov = response.body()!!.data

                    val adapter = ArrayAdapter(
                        this@NewAnimalRegistrationActivity,
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.getItemId()) {
            android.R.id.home -> onBackPressed()

        }
        return super.onOptionsItemSelected(item)
    }

    fun insertIntoDatabase(db: DatabaseHelper): Long {
        val result = db.insertData(
            UserData(
                binding.proposalNoTxt.text.toString(),
                proposal_img,
                binding.adharNo.editText?.text.toString(),
                aadhar_img,
                binding.noOfAnimals.editText?.text.toString(),
                category_code,
                area_code,
                duration_code,
                binding.casteTxt.text.toString(),
                binding.areaTxt.text.toString(),
                binding.durationTxt.text.toString(),
                binding.wptd.isChecked.toString(),
                city_code,
                binding.cityTxt.text.toString(),
                binding.hospitalTxt.text.toString(),
                hospital_code,
                binding.age.editText?.text.toString()

            )
        )

        return result;
    }





    //    /**
    //     * Get URI to image received from capture  by camera.
    //     */
/*    private fun getCaptureImageOutputUri(): Uri? {
        var outputFileUri: Uri? = null
        val getImage = externalCacheDir
        if (getImage != null) {
            outputFileUri = Uri.fromFile(File(getImage.path, "pickImageResult.jpeg"))
        }
        return outputFileUri
    }*/
}