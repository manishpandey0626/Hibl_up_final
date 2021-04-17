package smsinfosolutions.ind.hibl.registration

import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import smsinfosolutions.ind.hibl.utilities.AnimalImages
import smsinfosolutions.ind.hibl.database.DatabaseHelper
import smsinfosolutions.ind.hibl.databinding.ActivityUploadAnimalImageBinding
import smsinfosolutions.ind.hibl.utilities.Utils
import smsinfosolutions.ind.hibl.utilities.Utils.Companion.decodeUri
import smsinfosolutions.ind.hibl.utilities.Utils.Companion.getFileName
import smsinfosolutions.ind.hibl.utilities.Utils.Companion.getPickImageChooserIntent
import smsinfosolutions.ind.hibl.utilities.showMsg
import java.util.ArrayList


class UploadAnimalImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadAnimalImageBinding
    private var db: DatabaseHelper? = null
    private var animal_images: MutableList<AnimalImages> = arrayListOf()
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var proposal_no: String
    private lateinit var tag_no: String
    private  var type: String?=null
    private  var max_limit:Int=4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadAnimalImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        db = DatabaseHelper(this)
        proposal_no = intent.getStringExtra("proposal_no")
        tag_no = intent.getStringExtra("tag_no")
         max_limit=intent.getIntExtra("max_limit",4)


        db?.let {
            animal_images = it.readAnimalImages(proposal_no, tag_no)

        }
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = AnimalImageAdapter(animal_images, { c, type ->
            itemClicked(
                c,
                type
            )
        }, this)
        binding.upload.setOnClickListener {

            if (binding.recyclerView.adapter?.itemCount!! < 4) {
                val mimetype = arrayOf("image/*")
                showFileChooser(mimetype, PICK_IMAGE_REQUEST)
            } else {
                showMsg("Maximum 4 images allowed!!")
            }
        }


    }

    private fun showFileChooser(mime_type: Array<String>, request: Int) {
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mime_type)
//        intent.type = "*/*"
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//        startActivityForResult(intent, request)

        startActivityForResult(getPickImageChooserIntent(this,true), request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

            if (data.getClipData() != null) {
               val exist_cnt= binding.recyclerView.adapter?.itemCount!!
                val count: Int = data.clipData!!.getItemCount()
                if(exist_cnt+count<=max_limit) {
                    var currentItem = 0
                    while (currentItem < count) {
                        val uri = data.clipData!!.getItemAt(currentItem).getUri()

                        val file_name = getFileName(this, uri)

                        val bitmap = decodeUri(this, uri, 200)
                        bitmap?.let {
                            val file = Utils.Bitmap_to_base64(bitmap)
                            val data = AnimalImages(proposal_no, tag_no, file_name, file)

                            db?.let {
                                it.insertAnimalImages(data)
                                animal_images.clear()
                                animal_images.addAll(it.readAnimalImages(proposal_no, tag_no))
                                binding.recyclerView.adapter?.notifyDataSetChanged()
                            }
                            currentItem = currentItem + 1
                        }
                    }
                }
                else
                {
                    showMsg("Maximum $max_limit images allowed.")
                }

            } else if (data.data != null) {
                val uri = data.data

                val file_name = getFileName(this, uri)

                val bitmap = decodeUri(this, uri, 200)
                bitmap?.let {
                    val file = Utils.Bitmap_to_base64(bitmap)
                    val data = AnimalImages(proposal_no, tag_no, file_name, file)

                    db?.let {
                        it.insertAnimalImages(data)
                        animal_images.clear()
                        animal_images.addAll(it.readAnimalImages(proposal_no, tag_no))
                        binding.recyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            }
            else
            {
              val   bitmap = data.extras?.get("data") as Bitmap?
                bitmap?.let {
                    val file = Utils.Bitmap_to_base64(bitmap)
                    val data = AnimalImages(proposal_no, tag_no, "cam${animal_images.size}", file)

                    db?.let {
                        it.insertAnimalImages(data)
                        animal_images.clear()
                        animal_images.addAll(it.readAnimalImages(proposal_no, tag_no))
                        binding.recyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> onBackPressed()

        }
        return super.onOptionsItemSelected(item)
    }

    fun itemClicked(animal: AnimalImages, type: String) {
        if (type == "view") {
            val intent = Intent(
                this@UploadAnimalImageActivity,
                ImageViewerActivity::class.java
            ).apply {
                putExtra("file", animal.file)

            }

            startActivity(intent)
        } else if (type == "delete") {
            val result =
                db?.deleteAnimalImage(animal.proposal_no, animal.tag_no!!, animal.file_name)
            if (result != 0) {

                animal_images.clear()
                animal_images.addAll(db!!.readAnimalImages(animal.proposal_no, animal.tag_no))
                binding.recyclerView.adapter?.notifyDataSetChanged()

            }
        }
    }


}