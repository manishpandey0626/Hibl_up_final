package smsinfosolutions.ind.hibl.utilities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import smsinfosolutions.ind.hibl.R
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.util.*


/**
 * Created by Manish on 28-Jul-20.
 */

fun Context.showMsg(msg: String, duration: Int = Toast.LENGTH_SHORT)
{
    Toast.makeText(this, msg, duration).show()

}



class Utils
{

    companion object{
        fun Bitmap_to_base64(imagebitmap: Bitmap?): String {
            val baos = ByteArrayOutputStream()
            imagebitmap?.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            val byteArray = baos.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }

        fun Base64_to_bitmap(image: String?, context: Context): Bitmap
        {
            image?.let {
                val decodedString: ByteArray = Base64.decode(image, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                return bitmap
            }
            val icon = BitmapFactory.decodeResource(context.resources, R.drawable.upload)
            return icon
        }

        fun getFileName(context: Context, uri: Uri?): String {
            val cursor = context.contentResolver.query(uri!!, null, null, null, null)
            cursor!!.moveToFirst()
            val name =
                cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME))
            cursor.close()
            return name
        }

        @Throws(FileNotFoundException::class)
        fun decodeUri(c: Context, uri: Uri?, requiredSize: Int): Bitmap? {
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeStream(c.contentResolver.openInputStream(uri!!), null, o)
            var width_tmp = o.outWidth
            var height_tmp = o.outHeight
            var scale = 1
            while (true) {
                if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize) break
                width_tmp /= 2
                height_tmp /= 2
                scale *= 2
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            return BitmapFactory.decodeStream(c.contentResolver.openInputStream(uri), null, o2)
        }


        fun getPickImageChooserIntent(context: Context, allow_multiple: Boolean = false): Intent? {
            // Determine Uri of camera image to  save.
            //val outputFileUri: Uri? = getCaptureImageOutputUri()
            val allIntents: MutableList<Intent> = ArrayList()
            // collect all camera intents
            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val listCam = context.packageManager.queryIntentActivities(captureIntent, 0)
            for (res in listCam) {
                val intent = Intent(captureIntent)
                intent.component =
                    ComponentName(res.activityInfo.packageName, res.activityInfo.name)
                intent.setPackage(res.activityInfo.packageName)
                /*if (outputFileUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
                }*/
                allIntents.add(intent)
            }

            // collect all gallery intents
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "image/*"
            val listGallery = context.packageManager.queryIntentActivities(galleryIntent, 0)
            for (res in listGallery) {
                val intent = Intent(galleryIntent)
                intent.component =
                    ComponentName(res.activityInfo.packageName, res.activityInfo.name)
                intent.setPackage(res.activityInfo.packageName)
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allow_multiple)
                allIntents.add(intent)
            }
            // the main intent is the last in the  list  so pickup the useless one
            var mainIntent = allIntents[allIntents.size - 1]
            for (intent in allIntents) {
                if (intent.component!!.className == "com.android.documentsui.DocumentsActivity") {
                    mainIntent = intent
                    break
                }
            }
            allIntents.remove(mainIntent)
            // Create a chooser from the main  intent
            val chooserIntent = Intent.createChooser(mainIntent, "Select source")
            // Add all other intents
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toTypedArray())
            return chooserIntent
        }


        fun play(context:Context,player:MediaPlayer,fileName: String) {
            val descriptor: AssetFileDescriptor = context.getAssets().openFd(fileName)
            val start = descriptor.startOffset
            val end = descriptor.length
            player.setDataSource(descriptor.fileDescriptor, start, end)
            player.prepare()
            player.start()
        }

        fun stop(context: Context,player:MediaPlayer)
        {
            if(player.isPlaying)
            {
                player.stop()
                player.reset()
            }
        }

    }



}
